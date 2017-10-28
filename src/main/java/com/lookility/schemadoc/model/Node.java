package com.lookility.schemadoc.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


/**
 * Node of the hierarchical data model.
 */
public abstract class Node {

    private Node parent;
    private Occurrence occurrence = Occurrence.mandatory;
    private int level = 0;
    private Optional<List<Node>> children = Optional.empty();
    private String id = "0";
    private Documentation documentation;
    private LifeCycleMetaData lifeCycle = LifeCycleMetaData.DEFAULT_LIFECYCLE;

    Node() {
    }

    /**
     * Returns unique identifier of the node within the data model.
     * @return unique identifier
     */
    public String getId() {
        return this.id;
    }

    private void setId(String parentId, int position) {
        this.id = parentId + "." + position;
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

    public void addChild(Node node) {
        if (!this.children.isPresent()) {
            this.children = Optional.of(new ArrayList<Node>());
        }
        node.setParent(this);
        node.setId(this.id, this.children.get().size());

        this.children.get().add(node);
    }

    public Optional<List<Node>> getChildren() {
        return this.children;
    }

    public Node getParent() {
        return this.parent;
    }

    private void setParent(Node parent) {
        this.parent = parent;
        if (this.parent == null) {
            this.level = 0;
        } else {
            this.level = this.parent.level + 1;
        }
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

    public boolean isLeaf() {
        return !this.children.isPresent() || this.children.get().isEmpty();
    }

    public int getLevel() {
        return this.level;
    }
}
