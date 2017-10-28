package com.lookility.schemadoc.xsd;

import javax.xml.namespace.QName;

/**
 * Listener for errors occurred during building a tree model.
 */
public interface ErrorHandler {

    /**
     * Will be invoked if XSD has uses an unsupported feature.
     *
     * @param root qualified name of the root element of the tree.
     * @param feature name of the unsupported feature
     * @param location location of the unsupported feature in the XSD
     */
    void onUnsupportedFeature(QName root, String feature, Location location);
}
