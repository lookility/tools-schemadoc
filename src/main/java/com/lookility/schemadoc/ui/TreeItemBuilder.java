package com.lookility.schemadoc.ui;

import com.lookility.schemadoc.model.*;
import javafx.scene.control.TreeItem;

public class TreeItemBuilder implements TreeHandler {

    private TreeItem<Node> root;
    private TreeItem<Node> currentParent;


    public TreeItemBuilder() {
    }

    public TreeItem<Node> getRoot() {
        return this.root;
    }

    @Override
    public void onTreeBegin(String name) {

    }

    @Override
    public void onNodeBegin(ContentNode node, boolean first, boolean last) {
        TreeItem<Node> item = new TreeItem<>(node);

        if (this.root == null) {
            this.root = item;
            this.currentParent = item;
        } else {
            this.currentParent.getChildren().add(item);
            if (!node.isLeaf()) {
                item.setExpanded(true);
                this.currentParent = item;
            }
        }
    }

    @Override
    public void onNodeEnd(ContentNode node, boolean first, boolean last) {
        if (!node.isLeaf()) {
            this.currentParent = this.currentParent.getParent();
        }
    }

    @Override
    public void onAttribute(AttributeNode attrib, boolean first, boolean last) {
        TreeItem<Node> item = new TreeItem<>(attrib);
        this.currentParent.getChildren().add(item);
    }

    @Override
    public void onTreeEnd() {

    }
}
