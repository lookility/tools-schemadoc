package com.lookility.schemadoc.model;

import java.util.Optional;
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

    public Set<String> getAvailableProfiles() {
        ProfileScanner ps = new ProfileScanner();
        TreeWalker tw = new TreeWalker(ps);
        tw.walk(this);

        return ps.getProfileNames();
    }

    public Optional<Version> getMaxVersion() {
        VersionScanner vs = new VersionScanner();
        TreeWalker tw = new TreeWalker(vs);
        tw.walk(this);
        return vs.getMaxVersion();
    }

    public void applyVersion(Version version) {
        if (version == null) throw new IllegalArgumentException("version must not be null");
        Optional<Version> maxVersion = getMaxVersion();
        if (maxVersion.isPresent()) {
            if (maxVersion.get().compareTo(version) > 0) {
                throw new IllegalArgumentException("version '" + version + "' has to be greater than max. tree version '"+ maxVersion.get() + "'");
            }
        }
        VersionSetter vs = new VersionSetter(version);
        TreeWalker tw = new TreeWalker(vs);
        tw.walk(this);
    }
}
