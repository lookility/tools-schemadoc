package com.lookility.schemadoc.model;

import java.util.Optional;

public class VersionScanner implements TreeHandler {

    private Version maxVersion = null;

    public Optional<Version> getMaxVersion() {
        if (maxVersion == null) {
            return Optional.empty();
        }
        return Optional.of(this.maxVersion);
    }

    @Override
    public void onTreeBegin(String name) {
        this.maxVersion = null;
    }

    @Override
    public void onNodeBegin(ContentNode node, boolean first, boolean last) {
        determineMaxVersion(node);
    }

    @Override
    public void onNodeEnd(ContentNode node, boolean first, boolean last) {

    }

    @Override
    public void onAttribute(AttributeNode attrib, boolean first, boolean last) {
        determineMaxVersion(attrib);
    }

    @Override
    public void onTreeEnd() {

    }

    private void determineMaxVersion(Node node) {
        node.getAnnotation().getVersion().ifPresent((version) -> {
            if (this.maxVersion == null) {
                this.maxVersion = version;
            } else {
                if (this.maxVersion.compareTo(version) < 0) {
                    this.maxVersion = version;
                }
            }
        });
    }
}
