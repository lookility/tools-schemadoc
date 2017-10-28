package com.lookility.schemadoc.ui;

import com.lookility.schemadoc.model.ContentNode;
import com.lookility.schemadoc.model.GroupNode;
import com.lookility.schemadoc.model.PathFormatter;
import com.lookility.schemadoc.model.TreeHandler;
import javafx.scene.control.TreeItem;

public class TreeItemBuilder implements TreeHandler {

    private final PathFormatter pathFormatter = new PathFormatter().withSuppressedAttributeIndicator(false).withSuppressedNamespace(false);

    private final String language;
    private final boolean includeGroups;

    private TreeItem<UINode> root;
    private TreeItem<UINode> currentParent;


    public TreeItemBuilder(String language, boolean includeGroups) {
        this.language = language;
        this.includeGroups = includeGroups;
    }

    public TreeItem<UINode> getRoot() {
        return this.root;
    }

    @Override
    public void onTreeBegin(String name) {

    }

    @Override
    public void onContentNodeBegin(ContentNode node, boolean first, boolean last) {
        UINode n = new UINode();
        if (!this.includeGroups && node.isAttribute()) {
            n.name = "@" + node.getNodeName().getName();
        } else {
            n.name = node.getNodeName().getName();
        }
        n.occurrence = node.getOccurrence().getShortName();

        StringBuilder version = new StringBuilder();
        node.getLifeCycle().getSinceVersion().ifPresent(version::append);
        node.getLifeCycle().getDeprecated().ifPresent(v -> {
            if (version.length() > 0) version.append(' ');
            version.append("(! ").append(v).append(')');
        });
        n.version = version.toString();
        n.description = node.getDocumentation().getText(this.language);
        n.currentPath = this.pathFormatter.formatPath(node);
        n.type = node.getType();
        n.baseType = node.getBaseType().name();

        TreeItem<UINode> item = new TreeItem<>(n);
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
    public void onContentNodeEnd(ContentNode node, boolean first, boolean last) {
        if (!node.isLeaf()) {
            this.currentParent = this.currentParent.getParent();
        }
    }

    @Override
    public void onGroupNodeBegin(GroupNode group, boolean first, boolean last) {
        if (this.includeGroups) {
            UINode n = new UINode();
            n.name = group.getType().toString();
            n.occurrence = group.getOccurrence().toString();

            TreeItem<UINode> item = new TreeItem<>(n);
            item.setExpanded(true);
            this.currentParent.getChildren().add(item);
            this.currentParent = item;
        }
    }

    @Override
    public void onGroupNodeEnd(GroupNode group, boolean first, boolean last) {
        if (this.includeGroups) {
            this.currentParent = this.currentParent.getParent();
        }
    }

    @Override
    public void onTreeEnd() {

    }
}
