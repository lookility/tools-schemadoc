package com.lookility.schemadoc.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Content node represents the meta data of a node in a hierarchical data model.
 */
public class ContentNode extends Node {

    private static final Logger LOGGER = LogManager.getLogger(ContentNode.class);

    private static final Optional<ContentNode> NO_CHILD = Optional.empty();
    private static final Optional<AttributeNode> NO_ATTRIB = Optional.empty();

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

        LOGGER.debug("group node {} added to {}", group.getName(), getName());
    }

    public void add(ContentNode node) throws DuplicateNodeException {
        if (!this.children.isPresent()) {
            this.children = Optional.of(new ArrayList<ContentNode>());
        }

        if (!(node instanceof GroupNode)) {
            Optional<ContentNode> child = getChild(node.getName());
            if (child.isPresent()) {
                if (child.get().equals(node)) {
                    LOGGER.warn("equal child node {} already exists; node ignored");
                } else {
                    throw new DuplicateNodeException(this, node);
                }
            }
        }

        node.setParent(this);

        this.children.get().add(node);

        LOGGER.debug("content noded {} added to {}", node.getName(), getName());
    }

    public boolean containsChild(final NName name) {
        return getChild(name).isPresent();
    }

    public Optional<ContentNode> getChild(final NName name) {
        if (name == null) return NO_CHILD;
        if (!this.children.isPresent()) return NO_CHILD;

        for(ContentNode node : this.children.get()) {
            if (node.getName().equals(name))
                return Optional.of(node);
        }
        return NO_CHILD;
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

        LOGGER.debug("attribute {} added to {}", attrib.getName(), getName());
    }

    public boolean containsAttribute(final NName name) {
        return getAttribute(name).isPresent();
    }

    public Optional<AttributeNode> getAttribute(final NName name) {
        if (name == null) return NO_ATTRIB;
        if (!this.attributes.isPresent()) return NO_ATTRIB;

        for(AttributeNode attrib : this.attributes.get()) {
            if (attrib.getName().equals(name))
                return Optional.of(attrib);
        }
        return NO_ATTRIB;
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
