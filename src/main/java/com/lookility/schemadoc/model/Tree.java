package com.lookility.schemadoc.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.SortedSet;

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
    @JsonCreator
    public Tree(@JsonProperty("name") @NotNull String name) {
        super();
        this.name = Objects.requireNonNull(name,"name must not be null");
    }

    /**
     * Returns the name of the tree.
     * @return name of tree
     */
    @NotNull
    public String getName() {
        return this.name;
    }

    /**
     * Returns the root node of the tree.
     * @return root node
     */
    @Nullable
    public ContentNode getRoot() {
        return this.root;
    }


    /**
     * Set the root node of the tree.
     *
     * @param root root node or <i>null</i> to remove the root node
     */
    public void setRoot(@Nullable ContentNode root) {
        if (root != null) {
            if (!root.isRoot()) throw new IllegalStateException("root node must not have a parent");
        }
        this.root = root;
    }

    @JsonIgnore
    @NotNull
    public SortedSet<String> getAvailableLanguages() {
        LanguageScanner ls = new LanguageScanner();
        TreeWalker tw = new TreeWalker(ls);

        tw.walk(this);
        return ls.getAvailableLanguages();
    }

    @JsonIgnore
    @NotNull
    public SortedSet<String> getAvailableProfiles() {
        ProfileScanner ps = new ProfileScanner();
        TreeWalker tw = new TreeWalker(ps);
        tw.walk(this);

        return ps.getProfileNames();
    }

    /**
     * Returns the highest version assigned to node of tree.
     *
     * @return highest version or {@link Optional#empty()} if no version assigned
     */
    @JsonIgnore
    public Optional<Version> getMaxVersion() {
        VersionScanner vs = new VersionScanner();
        TreeWalker tw = new TreeWalker(vs);
        tw.walk(this);
        return vs.getMaxVersion();
    }

    /**
     * Apply the give version to all nodes of tree which have no assigned version.
     *
     * @param version version to be assigned to non-versioned nodes
     */
    public void applyVersion(@NotNull Version version) {
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
