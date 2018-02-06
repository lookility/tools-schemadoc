package com.lookility.schemadoc.model;

import java.util.Optional;

public class VersionSetter implements TreeHandler {

    private final Version version;

    public VersionSetter(final Version version) {
        if (version == null) throw new IllegalArgumentException("version must not be null");
        this.version = version;
    }

    @Override
    public void onTreeBegin(String name) {

    }

    @Override
    public void onNodeBegin(ContentNode node, boolean first, boolean last) {
        setVersionIfNotSpecified(node);
    }

    @Override
    public void onNodeEnd(ContentNode node, boolean first, boolean last) {

    }

    @Override
    public void onAttribute(AttributeNode attrib, boolean first, boolean last) {
        setVersionIfNotSpecified(attrib);
    }

    @Override
    public void onTreeEnd() {

    }

    private void setVersionIfNotSpecified(Node node) {
        Optional<Version> nodeVersion = node.getAnnotation().getVersion();
        if (!nodeVersion.isPresent()) {
            node.getAnnotation().setVersion(this.version);
        }
    }
}
