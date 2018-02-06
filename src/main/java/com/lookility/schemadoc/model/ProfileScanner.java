package com.lookility.schemadoc.model;

import java.util.HashSet;
import java.util.Set;

public class ProfileScanner implements TreeHandler {

    private final Set<String> profileNames = new HashSet<>();

    public Set<String> getProfileNames() {
        return this.profileNames;
    }

    @Override
    public void onTreeBegin(String name) {
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
