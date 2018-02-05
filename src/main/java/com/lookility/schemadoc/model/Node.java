package com.lookility.schemadoc.model;

import java.util.Objects;
import java.util.Optional;


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
    private Optional<Version> version = Optional.empty();

    protected Node(NName name) {
        if (name == null) throw new IllegalArgumentException("node name must not be null");
        this.name = name;
    }

    public void setVersion(Version version) {
        if (version == null) {
            this.version = Optional.empty();
        } else {
            this.version = Optional.of(version);
        }
    }

    public Optional<Version> getVersion() {
        return this.version;
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

    /**
     * Check if node is equal to other node.
     * <p>Two nodes are equal if they have an equal name and an equal base type and type.</p>
     * @param o other node to compare with
     * @return <i>true</i> if nodes are equal, <i>false</i> otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        return Objects.equals(name, node.name) &&
                baseType == node.baseType &&
                Objects.equals(type, node.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, baseType, type);
    }
}
