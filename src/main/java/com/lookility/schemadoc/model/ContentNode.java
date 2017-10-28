package com.lookility.schemadoc.model;

/**
 * Content node represents the meta data of a node in a hierarchical data model.
 */
public class ContentNode extends Node {

    private final NName name;
    private final boolean attribute;
    private BaseType baseType = BaseType.anonymous;
    private String type;

    public ContentNode(NName name) {
        this(name, false);
    }

    public ContentNode(NName name, boolean attribute) {
        if (name == null)
            throw new IllegalArgumentException("content node name must not be null");
        this.attribute = attribute;
        this.name = name;
    }

    public boolean isAttribute() {
        return this.attribute;
    }

    public NName getNodeName() {
        return this.name;
    }

    public BaseType getBaseType() {
        return this.baseType;
    }

    public void setBaseType(BaseType baseType) {
        this.baseType = baseType;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
