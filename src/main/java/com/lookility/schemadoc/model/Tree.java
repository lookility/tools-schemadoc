package com.lookility.schemadoc.model;

import java.util.Set;

/**
 * Tree representing a hierarchical data model.
 */
public class Tree extends NamespaceSet {

    private final String name;

    private ContentNode root;

    /**
     * Constructs a tree.
     *
     * @param name name of the tree
     */
    public Tree(String name) {
        super();
        this.name = name;
    }

    /**
     * Returns the name of the tree.
     * @return name of tree
     */
    public String getName() {
        return this.name;
    }

    /**
     * Returns the root node of the tree.
     * @return root node
     */
    public ContentNode getRoot() {
        return this.root;
    }


    /**
     * Set the root node of the tree.
     *
     * @param root root node
     */
    public void setRoot(ContentNode root) {
        if (root != null) {
            if (!root.isRoot()) throw new IllegalStateException("root node must not have a parent");
        }
        this.root = root;
    }

    public Set<String> getAvailableLanguages() {
        LanguageScanner ls = new LanguageScanner();
        TreeWalker tw = new TreeWalker(ls);

        tw.walk(this);
        return ls.getAvailableLanguages();
    }
}
