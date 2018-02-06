package com.lookility.schemadoc.xsd;

import com.lookility.schemadoc.model.*;
import org.apache.ws.commons.schema.*;
import org.apache.ws.commons.schema.utils.XmlSchemaObjectBase;
import org.w3c.dom.NodeList;

import javax.xml.XMLConstants;
import javax.xml.namespace.QName;
import java.util.List;

class TreeBuilder {
    private static final String XSD_URI = XMLConstants.W3C_XML_SCHEMA_NS_URI;
    private static final String XSD_PREFIX = "xsd";

    private final QName rootElementName;
    private final XmlSchemaCollection collection;
    private final Tree tree;
    private final ErrorHandler errorHandler;

    /**
     * Constructs a tree model for the specified root element.
     *
     * @param collection      collection of XML Schemas containing the specified root element
     * @param rootElementName root element
     * @param errorHandler    error handler which will be called in case of unsupported XML Schema features, or <i>null</i> if error handler shouldn't be used
     * @throws ModelBuilderException       if an error occurs on building the tree
     * @throws UnsupportedFeatureException if error handler is not specified and XML Schema has unsupported features
     * @see #getTree()
     */
    TreeBuilder(XmlSchemaCollection collection, QName rootElementName, ErrorHandler errorHandler) throws ModelBuilderException {
        if (collection == null) throw new IllegalArgumentException("collection must not be null");
        if (rootElementName == null) throw new IllegalArgumentException("root element must not be null");

        this.rootElementName = rootElementName;
        this.errorHandler = errorHandler;
        this.collection = collection;

        XmlSchemaElement rootElement = this.collection.getElementByQName(rootElementName);
        if (rootElement == null) throw new ModelBuilderException("root element not found: " + rootElementName);

        this.tree = new Tree(rootElementName.getLocalPart());
        this.tree.registerNamespace(XSD_URI, XSD_PREFIX);
        this.tree.setRoot(buildContentNode(rootElement));
    }

    /**
     * Returns the generated tree.
     *
     * @return generated tree
     */
    Tree getTree() {
        return this.tree;
    }

    /**
     * Build content node representing the structure of an XML element.
     *
     * @param element XSD definition of element
     * @return content node representing the structure of the XML element
     * @throws ModelBuilderException if an error occurs on building the tree
     */
    private ContentNode buildContentNode(XmlSchemaElement element) throws ModelBuilderException {
        if (element.getRef() != null && element.getRef().getTarget() != null) {
            element = element.getRef().getTarget();
        }
        ContentNode node = new ContentNode(buildName(element.getQName()));

        node.setOccurrence(Occurrence.fromMinMax(element.getMinOccurs(), element.getMaxOccurs()));

        XmlSchemaType elementType = element.getSchemaType();
        if (elementType != null) {
            if (elementType instanceof XmlSchemaSimpleType) {
                enhanceNode(node, (XmlSchemaSimpleType) elementType);
            } else if (elementType instanceof XmlSchemaComplexType) {
                enhanceNode(node, (XmlSchemaComplexType) elementType);
            } else {
                handleUnsupportedFeature("element type", elementType);
            }
        }

        if (!addDocumentation(node, element)) {
            addDocumentation(node, elementType, element.getSchemaTypeName());
        }

        return node;
    }

    /**
     * Build node name from qualified name.
     * <p>
     * <p>Namespaces not known by the tree are automatically registered.</p>
     *
     * @param qname qualified name
     * @return node name
     */
    private NName buildName(QName qname) {
        Namespace ns = this.tree.registerNamespace(qname.getNamespaceURI());
        return new NName(ns, qname.getLocalPart());
    }

    /**
     * Enhance the content node with the simple type definition of the element.
     *
     * @param node              content node representing the element which has to be enhanced
     * @param simpleElementType simple type definition of the element
     */
    private void enhanceNode(ContentNode node, XmlSchemaSimpleType simpleElementType) {
        node.setType(buildTypeName(simpleElementType.getQName()));
        node.setBaseType(determineSimpleBaseType(simpleElementType.getQName()));
    }

    /**
     * Enhance the content node with the complex type definition of the element.
     *
     * @param node               content node representing the element which has to be enhanced
     * @param complexElementType complex type definition of the element
     * @throws ModelBuilderException if an error occurs on building the tree
     */
    private void enhanceNode(ContentNode node, XmlSchemaComplexType complexElementType) throws ModelBuilderException {
        QName baseTypeQName = complexElementType.getBaseSchemaTypeName();

        if (complexElementType.getQName() != null) {
            node.setType(buildTypeName(complexElementType.getQName()));
        } else if (baseTypeQName != null) {
            node.setType(buildTypeName(baseTypeQName));
        }

        // Base type
        BaseType baseType = BaseType.complex;
        XmlSchemaContentModel contentModel = complexElementType.getContentModel();
        if (contentModel != null && contentModel instanceof XmlSchemaSimpleContent) {
            XmlSchemaContent content = contentModel.getContent();

            if (content instanceof XmlSchemaSimpleContentExtension) {
                baseType = determineSimpleBaseType(((XmlSchemaSimpleContentExtension) content).getBaseTypeName());
            } else if (content instanceof  XmlSchemaSimpleContentRestriction) {
                baseType = determineSimpleBaseType(((XmlSchemaSimpleContentRestriction) content).getBaseTypeName());
            } else {
                baseType = BaseType.unknown;
            }
        }
        node.setBaseType(baseType);

        // Attributes
        addAttributeGroup(node, complexElementType);

        // Particles
        addParticle(node, complexElementType);
    }

    private void addParticle(ContentNode node, XmlSchemaComplexType complexType) throws ModelBuilderException {
        if (complexType.getBaseSchemaTypeName() != null) {
            XmlSchemaType baseType = this.collection.getTypeByQName(complexType.getBaseSchemaTypeName());
            if (baseType == null)
                throw new ModelBuilderException("type not found: " + complexType.getBaseSchemaTypeName());

            addParticle(node, (XmlSchemaComplexType) baseType);
        }

        XmlSchemaContentModel contentModel = complexType.getContentModel();
        if (contentModel != null) {
            if (contentModel instanceof XmlSchemaComplexContent) {
                XmlSchemaContent content = contentModel.getContent();
                if (content instanceof XmlSchemaComplexContentExtension) {
                    addParticle(node, ((XmlSchemaComplexContentExtension) content).getParticle());
                } else {
                    handleUnsupportedFeature("complex content", content);
                }
            }
        }

        addParticle(node, complexType.getParticle());
    }

    private void addParticle(ContentNode node, XmlSchemaParticle particle) throws ModelBuilderException {
        if (particle == null)
            return;

        if (particle instanceof XmlSchemaSequence) {
            for (XmlSchemaSequenceMember sm : ((XmlSchemaSequence) particle).getItems()) {
                if (sm instanceof XmlSchemaElement) {
                    XmlSchemaElement element = (XmlSchemaElement) sm;
                    try {
                        node.add(buildContentNode(element));
                    } catch (DuplicateNodeException e) {
                        handleDuplicateChild(element.getQName(), element);
                    }
                } else if (sm instanceof XmlSchemaParticle) {
                    addParticle(node, (XmlSchemaParticle) sm);
                } else {
                    handleUnsupportedFeature("sequence member", sm);
                }
            }
        } else if (particle instanceof XmlSchemaChoice) {
            for (XmlSchemaChoiceMember cm : ((XmlSchemaChoice) particle).getItems()) {
                if (cm instanceof XmlSchemaElement) {
                    XmlSchemaElement element = (XmlSchemaElement) cm;
                    try {
                        node.add(buildContentNode(element));
                    } catch (DuplicateNodeException e) {
                        handleDuplicateChild(element.getQName(), element);
                    }
                } else if (cm instanceof XmlSchemaParticle) {
                    addParticle(node, (XmlSchemaParticle) cm);
                } else {
                    handleUnsupportedFeature("choice member", cm);
                }
            }
        } else if (particle instanceof XmlSchemaAll) {
            for (XmlSchemaAllMember am : ((XmlSchemaAll) particle).getItems()) {
                if (am instanceof XmlSchemaElement) {
                    XmlSchemaElement element = (XmlSchemaElement) am;
                    try {
                        node.add(buildContentNode(element));
                    } catch (DuplicateNodeException e) {
                        handleDuplicateChild(element.getQName(), element);
                    }
                } else if (am instanceof XmlSchemaParticle) {
                    addParticle(node, (XmlSchemaParticle) am);
                } else {
                    handleUnsupportedFeature("all member", am);
                }
            }
        } else {
            handleUnsupportedFeature("complex type particle", particle);
        }
    }


    private void addAttributeGroup(ContentNode node, XmlSchemaComplexType complexType) throws ModelBuilderException {
        if (complexType.getBaseSchemaTypeName() != null) {
            XmlSchemaType baseType = this.collection.getTypeByQName(complexType.getBaseSchemaTypeName());
            if (baseType == null)
                throw new ModelBuilderException("type not found: " + complexType.getBaseSchemaTypeName());

            addAttributeGroup(node, (XmlSchemaComplexType) baseType);
        }

        XmlSchemaContentModel contentModel = complexType.getContentModel();
        if (contentModel != null) {
            if (contentModel instanceof XmlSchemaComplexContent) {
                XmlSchemaContent content = contentModel.getContent();
                if (content instanceof XmlSchemaComplexContentExtension) {
                    XmlSchemaComplexContentExtension ext = (XmlSchemaComplexContentExtension) content;
                    addAttributeGroup(node, ext.getAttributes());
                } else {
                    handleUnsupportedFeature("complex content", content);
                }
            } else if (contentModel instanceof XmlSchemaSimpleContent) {
                XmlSchemaContent content = contentModel.getContent();
                if (content instanceof XmlSchemaSimpleContentExtension) {
                    XmlSchemaSimpleContentExtension ext = (XmlSchemaSimpleContentExtension) content;
                    addAttributeGroup(node, ext.getAttributes());
                }
            }
        }

        addAttributeGroup(node, complexType.getAttributes());
    }

    private void addAttributeGroup(ContentNode node, List<XmlSchemaAttributeOrGroupRef> attributes) {
        assert node != null;
        assert attributes != null;

        if (attributes.isEmpty()) return;

        for (XmlSchemaAttributeOrGroupRef aog : attributes) {
            if (aog instanceof XmlSchemaAttribute) {
                XmlSchemaAttribute attribute = (XmlSchemaAttribute) aog;
                AttributeNode attributeNode = new AttributeNode(buildName(attribute.getQName()));
                if (!addDocumentation(attributeNode, attribute)) {
                    addDocumentation(attributeNode, attribute.getSchemaType(), attribute.getSchemaTypeName());
                }

                attributeNode.setType(buildTypeName(attribute.getSchemaTypeName()));
                attributeNode.setBaseType(determineSimpleBaseType(attribute.getSchemaTypeName()));

                try {
                    node.add(attributeNode);
                }  catch (DuplicateNodeException e) {
                    handleDuplicateChild(attribute.getQName(), attribute);
                }
            } else {
                handleUnsupportedFeature("attributes", aog);
            }
        }
    }

    private String buildTypeName(QName type) {
        String typePrefix = this.tree.registerNamespace(type.getNamespaceURI()).getPrefix();
        String typeName = type.getLocalPart();

        return (typePrefix.isEmpty()) ? typeName : typePrefix + ":" + typeName;
    }

    private BaseType determineSimpleBaseType(QName simpleTypeName) {
        XmlSchemaSimpleType simpleType = (XmlSchemaSimpleType) this.collection.getTypeByQName(simpleTypeName);
        QName typeName = simpleType.getQName();

        if (XSD_URI.equals(typeName.getNamespaceURI())) {
            return SimpleType2BaseTypeMapper.map(typeName);
        } else {
            XmlSchemaSimpleTypeContent stc = simpleType.getContent();
            if (stc != null) {
                if (stc instanceof XmlSchemaSimpleTypeRestriction) {
                    XmlSchemaSimpleTypeRestriction scr = (XmlSchemaSimpleTypeRestriction) stc;
                    return determineSimpleBaseType(scr.getBaseTypeName());
                } else {
                    handleUnsupportedFeature("simpleContentType", stc);
                }
            }
        }
        return BaseType.unknown;
    }

    private boolean addDocumentation(Node node, XmlSchemaType type, QName schemaTypeName) {
        if (type != null) {
            return addDocumentation(node, type);
        }
        return schemaTypeName != null && addDocumentation(node, this.collection.getTypeByQName(schemaTypeName));
    }

    private boolean addDocumentation(Node node, XmlSchemaAnnotated annotated) {
        return annotated != null && addDocumentation(node, annotated.getAnnotation());
    }

    /**
     * Add the documentation from the annotated XML schema object to the node.
     *
     * @param node       node to which the documentation has to be added
     * @param annotation annotation of the XML schema object
     * @return <i>true</i> if documentation added, <i>false</i> if no documentation was added
     */
    private boolean addDocumentation(Node node, XmlSchemaAnnotation annotation) {
        boolean documentationAdded = false;
        if (annotation == null) return false;
        for (XmlSchemaAnnotationItem item : annotation.getItems()) {
            if (item instanceof XmlSchemaDocumentation) {
                XmlSchemaDocumentation d = (XmlSchemaDocumentation) item;
                String lang = d.getLanguage();
                node.getDocumentation().append(lang, renderMarkup(d.getMarkup()));
                documentationAdded = true;
            }
        }
        return documentationAdded;
    }

    private String renderMarkup(NodeList nl) {
        StringBuilder text = new StringBuilder();

        for (int i = 0; i < nl.getLength(); i++) {
            text.append(nl.item(i).getTextContent());
        }
        return text.toString();
    }

    private void handleUnsupportedFeature(String feature, XmlSchemaObjectBase base) {
        Location location = new Location(base);

        if (this.errorHandler == null) {
            throw new UnsupportedFeatureException(feature, location);
        }
        this.errorHandler.onUnsupportedFeature(this.rootElementName, feature, location);
    }

    private void handleDuplicateChild(QName qname, XmlSchemaObjectBase base) {
        Location location = new Location(base);

        if (this.errorHandler == null) {
            throw new DuplicateChildException(qname, location);
        }

        this.errorHandler.onDuplicateNote(this.rootElementName, qname, location);
    }
}
