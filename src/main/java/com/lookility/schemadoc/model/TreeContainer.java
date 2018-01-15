package com.lookility.schemadoc.model;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

public class TreeContainer {

    private final List<Tree> trees = new ArrayList<>();

    public TreeContainer() {

    }

    public void add(Tree tree) {
        this.trees.add(tree);
    }

    public List<Tree> trees() {
        return this.trees;
    }

    public Tree get(NName root) {
        for(Tree tree : this.trees) {
            if (tree.getRoot().getName().equals(root))
                return tree;
        }
        return null;
    }

    public SortedSet<String> getAvailableLanguages() {
        SortedSet<String> languages = new TreeSet<>();

        for(Tree tree: this.trees) {
            languages.addAll(tree.getAvailableLanguages());
        }

        return languages;
    }
}
