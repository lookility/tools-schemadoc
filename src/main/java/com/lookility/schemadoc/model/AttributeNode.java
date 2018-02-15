package com.lookility.schemadoc.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class AttributeNode extends Node {

    @JsonCreator
    protected AttributeNode(@JsonProperty("name") String fullName) {
        this(NName.valueOf(fullName));
    }

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
