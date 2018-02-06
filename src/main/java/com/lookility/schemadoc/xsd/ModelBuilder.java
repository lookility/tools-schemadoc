package com.lookility.schemadoc.xsd;

import com.lookility.schemadoc.model.Tree;
import com.lookility.schemadoc.model.TreeContainer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.ws.commons.schema.*;
import org.apache.ws.commons.schema.extensions.ExtensionRegistry;

import javax.xml.namespace.QName;
import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;

/**
 * Build tree model for an element of a XML Schema definition.
 */
public class ModelBuilder {

    private static final Logger LOGGER = LogManager.getLogger(ModelBuilder.class);

    private final File xsdFile;

    private XmlSchemaCollection collection = null;
    private XmlSchema schema = null;

    /**
     * Creates a tree model builder for a XML Schema.
     * <p>
     * The given XML Schema will be loaded.</p>
     *
     * @param xsdFile XML Schema file
     * @throws IOException if error occurred on loading XML Schema
     */
    public ModelBuilder(File xsdFile) throws IOException {
        this.xsdFile = xsdFile;

        loadSchema();
    }

    public void reload() throws IOException {
        loadSchema();
    }

    /**
     * Build a tree model for an element of the given XML Schema.
     *
     * @param rootQName    root element for which the tree model has to be build
     * @param errorHandler handler for unsupported feature errors
     * @return tree model for specified root element
     */
    public Tree buildTree(QName rootQName, ErrorHandler errorHandler) throws ModelBuilderException {
        TreeBuilder treeBuilder = new TreeBuilder(this.collection, rootQName, errorHandler);
        return treeBuilder.getTree();
    }

    /**
     * Build a tree model for global complex elements of the given XML Schema.
     *
     * @param errorHandler
     * @return container with generated trees
     * @throws ModelBuilderException
     */
    public TreeContainer buildAll(ErrorHandler errorHandler) throws ModelBuilderException {
        TreeContainer container = new TreeContainer();

        Tree tree;

        for(Map.Entry<QName, XmlSchemaElement> element : this.schema.getElements().entrySet()) {
            XmlSchemaType type = element.getValue().getSchemaType();
            if (type != null && type instanceof XmlSchemaComplexType) {
                tree = buildTree(element.getKey(), errorHandler);
                container.add(tree);
                LOGGER.debug("tree created for element " + element.getKey());
            }
        }

        return container;
    }

    private void loadSchema() throws IOException {
        LOGGER.debug("loading XML Schema file: " + this.xsdFile);

        this.collection = new XmlSchemaCollection();
        this.collection.setBaseUri(this.xsdFile.getParentFile().toURI().toString());
        this.schema = this.collection.read(new StreamSource(new FileInputStream(this.xsdFile)));

        LOGGER.debug("schema loaded: " + this.schema.getTargetNamespace());
    }
}
