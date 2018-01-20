package com.lookility.schemadoc.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

/**
 * Content node represents the meta data of a node in a hierarchical data model.
 */
public class ContentNode extends Node {

    private Optional<List<ContentNode>> children = Optional.empty();
    private Optional<List<AttributeNode>> attributes = Optional.empty();

    public ContentNode(NName name) {
        super(name);
    }

    public void add(GroupNode group) {
        if (!this.children.isPresent()) {
            this.children = Optional.of(new ArrayList<ContentNode>());
        }
        group.setParent(this);

        this.children.get().add(group);
    }

    public void add(ContentNode node) throws DuplicateNodeException {
        if (!this.children.isPresent()) {
            this.children = Optional.of(new ArrayList<ContentNode>());
        }

        if (!(node instanceof GroupNode) && containsChild(node.getName())) throw new DuplicateNodeException(this, node);

        node.setParent(this);

        this.children.get().add(node);
    }

    public boolean containsChild(final NName name) {
        if (name == null) throw new IllegalArgumentException("name must not be null");
        if (!this.children.isPresent())
            return false;

        for(ContentNode node : this.children.get()) {
            if (node.getName().equals(name))
                return true;
        }
        return false;
    }

    public Optional<List<ContentNode>> getChildren() {
        return this.children;
    }

    public void add(AttributeNode attrib) throws DuplicateNodeException {
        if (attrib == null) throw new IllegalArgumentException("attribute must not be null");
        if (!this.attributes.isPresent()) {
            this.attributes = Optional.of(new ArrayList<AttributeNode>());
        }

        if (containsAttribute(attrib.getName())) throw new DuplicateNodeException(this, attrib);

        attrib.setParent(this);

        this.attributes.get().add(attrib);
    }

    public boolean containsAttribute(final NName name) {
        if (name == null) throw new IllegalArgumentException("name must not be null");
        if (!this.attributes.isPresent())
            return false;

        for(AttributeNode attrib : this.attributes.get()) {
            if (attrib.getName().equals(name))
                return true;
        }
        return false;
    }

    public Optional<List<AttributeNode>> getAttributes() {
        return this.attributes;
    }

    @Override
    public boolean isLeaf() {
        return (!this.children.isPresent() || this.children.get().isEmpty())
                && (!this.attributes.isPresent() || this.attributes.get().isEmpty());
    }
}
