package com.lookility.schemadoc.model;

public class PathFormatter extends AbstractPathFormatter {


    private static final char ATTRIBUTE_PREFIX = '@';

    private final NamespaceRepresentation namespaceRepresentation;

    public static enum NamespaceRepresentation {
        /**
         * Namespace is not represented.
         */
        none,

        /**
         * Namespace is represented by the prefix.
         */
        prefixOnly,

        /**
         * Namespace is represented by the URI.
         */
        uriOnly,

        /**
         * Namespace is represented by prefix and URI.
         */
        uriAndPrefix
    }

    public PathFormatter(NamespaceRepresentation namespaceRepresentation) {
        this.namespaceRepresentation = namespaceRepresentation;
    }

    @Override
    protected void appendPathNode(StringBuilder path, Node node) {
        if (!node.getName().getNamespace().isNoNamespace()) {
            switch (this.namespaceRepresentation) {
                case none:
                    break;
                case uriOnly:
                    appendURI(path, node.getName().getNamespace());
                    break;
                case uriAndPrefix:
                    appendURI(path, node.getName().getNamespace());
                    appendPrefix(path, node.getName().getNamespace());
                    break;
                case prefixOnly:
                    appendPrefix(path, node.getName().getNamespace());
                    break;
            }
        }
        path.append(getLocalName(node));
    }

    @Override
    public String getLocalName(final Node node) {
        if (node instanceof AttributeNode)
            return ATTRIBUTE_PREFIX + node.getName().getName();
        else
            return node.getName().getName();
    }

    private void appendURI(StringBuilder path, Namespace ns) {
        path.append('{').append(ns.getURI()).append('}');
    }

    private void appendPrefix(StringBuilder path, Namespace ns) {
        path.append(ns.getPrefix()).append(':');
    }
}
