package com.lookility.schemadoc.model;

public class DuplicateNodeException extends Exception {

    private final Node parent;
    private final Node node;

    public DuplicateNodeException(final Node parent, final Node node) {
        super("Node " + node.toString() + " already exists under node " + parent + "!");
        this.parent = parent;
        this.node = node;
    }

    public Node getParent() {
        return this.parent;
    }

    public Node getNode() {
        return this.node;
    }
}
