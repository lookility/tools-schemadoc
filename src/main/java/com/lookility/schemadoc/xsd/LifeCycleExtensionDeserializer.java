package com.lookility.schemadoc.xsd;

import org.apache.ws.commons.schema.XmlSchemaObject;
import org.apache.ws.commons.schema.extensions.ExtensionDeserializer;
import org.apache.ws.commons.schema.extensions.ExtensionRegistry;
import org.w3c.dom.Node;

import javax.xml.namespace.QName;
import java.io.InputStream;

/**
 * Deserializer for life cycle meta-data extension.
 *
 * @see org.apache.ws.commons.schema.XmlSchemaCollection
 * @see ExtensionRegistry
 */
public class LifeCycleExtensionDeserializer implements ExtensionDeserializer {

    /**
     * Version since the global element, attribute or type is defined.
     */
    public static final String LOCAL_PART_DEFSINCE = "defsince";

    /**
     * Version since the element or attribute is added to the structure.
     */
    public static final String LOCAL_PART_SINCE = "since";

    /**
     * Version since the global element, attribute or type is deprecated.
     */
    public static final String LOCAL_PART_DEFDEPRECATED = "defdeprecated";

    /**
     * Indicates that an element, attribute or type is deprecated.
     */
    public static final String LOCAL_PART_DEPRECATED = "deprecated";

    /**
     * Default namespace URI for life cycle extensions.
     */
    public static final String DEFAULT_NAMESPACE = "urn:xsd:com.lookility.schemadoc.lifecycle:v1";

    private static final String KEY_DEFSINCE = LifeCycleExtensionDeserializer.class.getCanonicalName() + LOCAL_PART_DEFSINCE;
    private static final String KEY_SINCE = LifeCycleExtensionDeserializer.class.getCanonicalName() + LOCAL_PART_SINCE;
    private static final String KEY_DEFDEPRECATED = LifeCycleExtensionDeserializer.class.getCanonicalName() + LOCAL_PART_DEFDEPRECATED;
    private static final String KEY_DEPRECATED = LifeCycleExtensionDeserializer.class.getCanonicalName() + LOCAL_PART_DEPRECATED;


    public final QName defsince;
    public final QName since;
    public final QName defdeprecated;
    public final QName deprecated;

    public LifeCycleExtensionDeserializer(String namespaceURI) {
        if (namespaceURI == null || namespaceURI.isEmpty())
            namespaceURI = DEFAULT_NAMESPACE;
        defsince = new QName(namespaceURI, LOCAL_PART_DEFSINCE);
        since = new QName(namespaceURI, LOCAL_PART_SINCE);
        defdeprecated = new QName(namespaceURI, LOCAL_PART_DEFDEPRECATED);
        deprecated = new QName(namespaceURI, LOCAL_PART_DEPRECATED);
    }

    public void register(ExtensionRegistry registry) {
        if (registry == null) throw new IllegalArgumentException("registry must not be null");
        registry.registerDeserializer(this.defdeprecated, this);
        registry.registerDeserializer(this.deprecated, this);
        registry.registerDeserializer(this.defsince, this);
        registry.registerDeserializer(this.since, this);
    }

    @Override
    public void deserialize(XmlSchemaObject schemaObject, QName name, Node domNode) {

        if (this.since.equals(name)) {
            String version = domNode.getTextContent();
            schemaObject.addMetaInfo(KEY_SINCE, version);
        } else if (this.defsince.equals(name)) {
            String version = domNode.getTextContent();
            schemaObject.addMetaInfo(KEY_DEFSINCE, version);
        } else if (this.deprecated.equals(name)) {
            String version = domNode.getTextContent();
            schemaObject.addMetaInfo(KEY_DEPRECATED, version);
        } else if (this.defdeprecated.equals(name)) {
            String version = domNode.getTextContent();
            schemaObject.addMetaInfo(KEY_DEFDEPRECATED, version);
        }
    }

    public String getSince(XmlSchemaObject schemaObject) {
        if (schemaObject.getMetaInfoMap() != null) {
            return (String) schemaObject.getMetaInfoMap().get(KEY_SINCE);
        }
        return null;
    }

    public String getDefSince(XmlSchemaObject schemaObject) {
        if (schemaObject.getMetaInfoMap() != null) {
            return (String) schemaObject.getMetaInfoMap().get(KEY_DEFSINCE);
        }
        return null;
    }

    public String getDefDeprecated(XmlSchemaObject schemaObject) {
        if (schemaObject.getMetaInfoMap() != null) {
            return (String) schemaObject.getMetaInfoMap().get(KEY_DEFDEPRECATED);
        }
        return null;
    }

    public String getDeprecated(XmlSchemaObject schemaObject) {
        if (schemaObject.getMetaInfoMap() != null) {
            return (String) schemaObject.getMetaInfoMap().get(KEY_DEPRECATED);
        }
        return null;
    }

    /**
     * Return the input stream for the XML Schema definition of the life cycle meta-data.
     *
     * @return input stream of XML Schema
     */
    public static InputStream getSchema() {
        return LifeCycleExtensionDeserializer.class.getResourceAsStream("/xsd/lifecycle.xsd");
    }
}
