package com.lookility.schemadoc.model;

public class AttributeNode extends Node {

    public AttributeNode(NName name) {
        super(name);
    }

    @Override
    public boolean isLeaf() {
        return true;
    }

    @Override
    public String toString() {
        return getName().getNamespace() + "@" + getName().getName();
    }
}
