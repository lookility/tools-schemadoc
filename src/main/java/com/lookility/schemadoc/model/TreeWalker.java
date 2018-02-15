package com.lookility.schemadoc.model;

import javax.swing.text.AbstractDocument;
import java.util.List;
import java.util.Optional;

public class TreeWalker {

    private final TreeHandler handler;

    public TreeWalker(TreeHandler handler) {
        if (handler == null) throw new IllegalArgumentException("handler must not be null");
        this.handler = handler;
    }

    public void walk(Tree tree) {
        if (tree == null) throw new IllegalArgumentException("tree must not be null");
        this.handler.onTreeBegin(tree);
        handleContentNode(tree.getRoot(), true, true);
        this.handler.onTreeEnd();
    }

    private void handleChildren(Optional<List<ContentNode>> children) {
        if (children.isPresent()) {
            int index = 0;
            int lastIndex = children.get().size() - 1;
            Node child;
            boolean first;
            boolean last;

            while(index <= lastIndex) {
                child = children.get().get(index);
                first = (index == 0);
                last = (index == lastIndex);
                if (child instanceof ContentNode) {
                    handleContentNode((ContentNode) child, first, last);
                } else {
                    throw new IllegalStateException("unsupported node type: " + child.getClass().getCanonicalName());
                }
                index++;
            }
        }
    }

    private void handleContentNode(ContentNode node, boolean first, boolean last) {
        this.handler.onNodeBegin(node, first, last);

        handleAttributes(node.getAttributes());
        handleChildren(node.getChildren());

        this.handler.onNodeEnd(node, first, last);
    }

    private void handleAttributes(Optional<List<AttributeNode>> attributes) {
        if (attributes.isPresent()) {
            int index = 0;
            int lastIndex = attributes.get().size() - 1;
            AttributeNode attribute;
            boolean first;
            boolean last;

            while(index <= lastIndex) {
                attribute = attributes.get().get(index);
                first = (index == 0);
                last = (index == lastIndex);
                this.handler.onAttribute(attribute, first, last);
                index++;
            }
        }
    }

}
