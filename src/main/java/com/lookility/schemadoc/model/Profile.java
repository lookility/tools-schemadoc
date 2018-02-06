package com.lookility.schemadoc.model;

import java.util.Objects;

public class Profile implements Comparable<Profile> {

    private final String name;
    private boolean included = true;

    public Profile(final String name) {
        if (name == null || name.isEmpty()) throw new IllegalArgumentException("profile name is null or empty");
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public boolean isIncluded() {
        return this.included;
    }

    public void setIncluded(boolean included) {
        this.included = included;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Profile profile = (Profile) o;
        return this.name.equals(profile.name);
    }

    @Override
    public int hashCode() {
        return this.name.hashCode();
    }

    @Override
    public int compareTo(Profile profile) {
        return this.name.compareTo(profile.name);
    }
}
