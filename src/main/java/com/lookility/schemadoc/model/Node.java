package com.lookility.schemadoc.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;


/**
 * Node of the hierarchical data model.
 */
public abstract class Node {

    private final NName name;
    private Node parent;
    private Occurrence occurrence = Occurrence.mandatory;
    private BaseType baseType = BaseType.anonymous;
    private Optional<String> type = Optional.empty();
    private Documentation documentation;
    private LifeCycleMetaData lifeCycle = LifeCycleMetaData.DEFAULT_LIFECYCLE;

    protected Node(NName name) {
        if (name == null) throw new IllegalArgumentException("node name must not be null");
        this.name = name;
    }

    public NName getName() {
        return this.name;
    }

    public BaseType getBaseType() {
        return this.baseType;
    }

    public void setBaseType(BaseType baseType) {
        this.baseType = baseType;
    }

    public Optional<String> getType() {
        return this.type;
    }

    public void setType(String type) {
        if (type == null || type.isEmpty()) this.type = Optional.empty();
        this.type = Optional.of(type);
    }

    /**
     * Returns life cycle meta data of the node.
     * @return life cycle meta data
     */
    public LifeCycleMetaData getLifeCycle() {
        return this.lifeCycle;
    }

    /**
     * Set the life cycle meta data of the node.
     * @param lifeCycle life cycle meta data
     */
    public void setLifeCycle(LifeCycleMetaData lifeCycle) {
        this.lifeCycle = (lifeCycle != null) ? lifeCycle : LifeCycleMetaData.DEFAULT_LIFECYCLE;
    }

    /**
     * Returns the documentation of the node.
     * @return documentation
     */
    public Documentation getDocumentation() {
        if (this.documentation == null) {
            this.documentation = new Documentation();
        }
        return this.documentation;
    }

    public Node getParent() {
        return this.parent;
    }

    protected void setParent(Node parent) {
        this.parent = parent;
    }

    public Occurrence getOccurrence() {
        return this.occurrence;
    }

    public void setOccurrence(Occurrence occurrence) {
        this.occurrence = occurrence;
    }

    public boolean isRoot() {
        return this.parent == null;
    }

    public abstract boolean isLeaf();

    @Override
    public String toString() {
        return this.name.toString();
    }
}
