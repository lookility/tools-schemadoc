package com.lookility.schemadoc.model;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractPathFormatter {

    private static final char PATH_SEPARATOR = '/';

    public String formatPath(final Node node) {
        StringBuilder path = new StringBuilder();

        List<Node> reverseNodePath = buildReversePath(node);

        int index = reverseNodePath.size();

        while (--index >= 0) {
            path.append(PATH_SEPARATOR);
            appendPathNode(path, reverseNodePath.get(index));
        }
        return path.toString();
    }

    public String getLocalName(final Node node) {
        return node.getName().getName();
    }

    protected abstract void appendPathNode(StringBuilder path, final Node node);

    protected List<Node> buildReversePath(Node node) {
        List<Node> reverseNodePath = new ArrayList<>();

        while (node != null) {
            if (!(node instanceof GroupNode)) {
                reverseNodePath.add(node);
            }
            node = node.getParent();
        }

        return reverseNodePath;
    }
}
