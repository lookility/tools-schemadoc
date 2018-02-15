package com.lookility.schemadoc.model;

import java.util.Objects;
import java.util.Optional;

public class PathFormatter extends AbstractPathFormatter {


    private static final char ATTRIBUTE_PREFIX = '@';

    private final NamespaceSet namespaceSet;
    private final NamespaceRepresentation namespaceRepresentation;

    public PathFormatter(NamespaceRepresentation namespaceRepresentation, NamespaceSet namespaceSet) {
        this.namespaceSet = Objects.requireNonNull(namespaceSet);
        this.namespaceRepresentation = Objects.requireNonNull(namespaceRepresentation);
    }

    @Override
    protected void appendPathNode(StringBuilder path, Node node) {
        Optional<Namespace> namespace = node.getName().getNamespace();

        namespace.ifPresent((ns) -> {
            switch (this.namespaceRepresentation) {
                case none:
                    break;
                case uriOnly:
                    appendURI(path, ns);
                    break;
                case uriAndPrefix:
                    appendURI(path, ns);
                    appendPrefix(path, ns);
                    break;
                case prefixOnly:
                    appendPrefix(path, ns);
                    break;
            }
        });
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
        if (ns == null || ns.isNoNamespace()) return;
        String prefix = this.namespaceSet.getPrefix(ns);
        path.append(prefix).append(':');
    }

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
}
