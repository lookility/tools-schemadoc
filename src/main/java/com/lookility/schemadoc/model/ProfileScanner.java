package com.lookility.schemadoc.model;

import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

public class ProfileScanner implements TreeHandler {

    private final SortedSet<String> profileNames = new TreeSet<>();

    @NotNull
    public SortedSet<String> getProfileNames() {
        return this.profileNames;
    }

    @Override
    public void onTreeBegin(Tree tree) {
        this.profileNames.clear();
    }

    @Override
    public void onNodeBegin(ContentNode node, boolean first, boolean last) {
        addProfileNames(node);
    }

    @Override
    public void onNodeEnd(ContentNode node, boolean first, boolean last) {
    }

    @Override
    public void onAttribute(AttributeNode attrib, boolean first, boolean last) {
        addProfileNames(attrib);
    }

    @Override
    public void onTreeEnd() {
    }

    private void addProfileNames(Node node) {
        for (Profile profile : node.getAnnotation().getProfiles()) {
            this.profileNames.add(profile.getName());
        }
    }
}
