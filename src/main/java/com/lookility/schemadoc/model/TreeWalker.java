package com.lookility.schemadoc.model;

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
        this.handler.onTreeBegin(tree.getName());
        handleContentNode(tree.getRoot(), true, true);
        this.handler.onTreeEnd();
    }

    private void handleChildren(Optional<List<Node>> children) {
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
                } else if (child instanceof GroupNode) {
                    handleGroupNode((GroupNode) child, first, last);
                } else {
                    throw new IllegalStateException("unsupported node type: " + child.getClass().getCanonicalName());
                }
                index++;
            }
        }
    }

    private void handleContentNode(ContentNode node, boolean first, boolean last) {
        this.handler.onContentNodeBegin(node, first, last);

        handleChildren(node.getChildren());

        this.handler.onContentNodeEnd(node, first, last);
    }

    private void handleGroupNode(GroupNode group, boolean first, boolean last) {
        this.handler.onGroupNodeBegin(group, first, last);

        handleChildren(group.getChildren());

        this.handler.onGroupNodeEnd(group, first, last);
    }
}
