package com.lookility.schemadoc.model;

/**
 * Group nodes.
 *
 * <p>Group nodes don't represent a node of the data model. It is just used to group content nodes within the meta model.</p>
 */
public class GroupNode extends Node {

    private final GroupNodeType type;

    public GroupNode(GroupNodeType type) {
        if (type == null) throw new IllegalArgumentException("type must not be null");
        this.type = type;
    }

    public GroupNodeType getType() {
        return this.type;
    }
}
