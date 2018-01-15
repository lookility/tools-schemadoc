package com.lookility.schemadoc.model;

/**
 * Group nodes.
 *
 * <p>Group nodes don't represent a node of the data model. It is just used to group content nodes within the meta model.</p>
 */
public class GroupNode extends ContentNode {
    public static final String GROUP_URI = "urn:ns:com.lookility.schemadoc.group:v1";
    public static final String GROUP_PREFIX = "group";

    private static final Namespace GROUP_NAMESPACE = Namespace.create(GROUP_URI, GROUP_PREFIX);

    private final GroupNodeType type;

    public GroupNode(GroupNodeType type) {
        super(new NName(GROUP_NAMESPACE, type.name()));
        this.type = type;
        setBaseType(BaseType.complex);
    }

    public GroupNodeType getGroupType() {
        return this.type;
    }
}
