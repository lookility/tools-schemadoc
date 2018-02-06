package com.lookility.schemadoc.model;

import java.util.*;

public class NodeAnnotation {

    private Version version = null;
    private SortedSet<Profile> profiles = new TreeSet<Profile>();

    public NodeAnnotation() {}

    public Optional<Version> getVersion() {
        return this.version == null ? Optional.empty() : Optional.of(this.version);
    }

    public void setVersion(Version version) {
        this.version = version;
    }

    public SortedSet<Profile> getProfiles() {
        return this.profiles;
    }
}
