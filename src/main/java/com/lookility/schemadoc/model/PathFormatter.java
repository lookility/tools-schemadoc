package com.lookility.schemadoc.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Formatter for the path of content node within a tree.
 *
 * @see ContentNode
 */
public class PathFormatter {

    public static final char PATH_SEPARATOR = '/';
    public static final char ATTRIBUTE_INDICATOR = '@';

    private boolean suppressNamespace = false;
    private boolean suppressAttributePrefix = false;

    /**
     * Creates a new path formatter.
     */
    public PathFormatter() {
    }

    /**
     * Suppress to add the namespace prefix to the path elements.
     * <p>
     * By default the prefix of the element namespace is added to the path element.
     *
     * @param suppress <i>true</i> to suppress namespace prefixes, <i>false</i> to print namespace prefix
     * @return current path formatter for fluent interface
     */
    public PathFormatter withSuppressedNamespace(boolean suppress) {
        this.suppressNamespace = suppress;
        return this;
    }

    /**
     * Suppress to add attribute indicator.
     * <p>
     * By default path elements representing an attribute are prefixed by an {@link #ATTRIBUTE_INDICATOR}.
     *
     * @param suppress <i>true</i> to suppress attribute indicator, <i>false</i> to print attribute indicator
     * @return current path formatter for fluent interface
     * @see #ATTRIBUTE_INDICATOR
     */
    public PathFormatter withSuppressedAttributeIndicator(boolean suppress) {
        this.suppressAttributePrefix = suppress;
        return this;
    }

    public String formatPath(final ContentNode node) {
        List<ContentNode> reverseNodePath = new ArrayList<>();

        Node n = node;
        while (n != null) {
            if (n instanceof ContentNode) {
                reverseNodePath.add((ContentNode) n);
            }
            n = n.getParent();
        }

        return buildPathFromReversePathNodes(reverseNodePath);
    }

    /**
     * Build the string representation of a path by a list of reverse ordered content nodes.
     *
     * @param reverseNodePath list of path nodes from leaf to parent
     * @return string representation of path
     * @see ContentNode
     */
    private String buildPathFromReversePathNodes(final List<ContentNode> reverseNodePath) {
        StringBuilder path = new StringBuilder();

        ContentNode cn;
        int index = reverseNodePath.size();

        while (--index >= 0) {
            cn = reverseNodePath.get(index);

            path.append(PATH_SEPARATOR);

            if (!this.suppressNamespace && !cn.getNodeName().getNamespace().isNoNamespace()) {
                path.append(cn.getNodeName().getNamespace().getPrefix());
                path.append(':');
            }

            if (!this.suppressAttributePrefix && cn.isAttribute()) {
                path.append(ATTRIBUTE_INDICATOR);
            }

            path.append(cn.getNodeName().getName());
        }
        return path.toString();
    }
}
