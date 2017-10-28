package com.lookility.schemadoc.model;

import java.util.Optional;

/**
 * Life cycle meta data for nodes.
 */
public class LifeCycleMetaData {

    /**
     * Default meta data.
     *
     * <p>No version and no deprecated version specified.</p>
     */
    public static final LifeCycleMetaData DEFAULT_LIFECYCLE = new LifeCycleMetaData(null, null);

    private final Optional<String> sinceVersion;
    private final Optional<String> deprecatedSince;

    /**
     * Build new immutable life cycle meta data.
     *
     * @param version version since the node is available
     * @param deprecated version since the node is deprecated
     * @return life cycle meta data
     */
    public static LifeCycleMetaData build(String version, String deprecated) {
        if (version == null && deprecated == null)
            return DEFAULT_LIFECYCLE;
        return new LifeCycleMetaData(version, deprecated);
    }

    /**
     * Check if default life cycle.
     *
     * @return <i>true</i> if default life cycle, <i>false</i> otherwise
     */
    public boolean isDefault() {
        return this == DEFAULT_LIFECYCLE;
    }

    /**
     * Constructs new life cycle meta data.
     *
     * <p>Private to enforce usage static build method.</p>
     *
     * @param sinceVersion version since the node is available or <i>null</i> if unspecified
     * @param deprecatedSince version since the node is deprecated or <i>null</i> if not deprecated
     *
     * @see #build(String, String)
     */
    private LifeCycleMetaData(String sinceVersion, String deprecatedSince) {
        this.sinceVersion = sinceVersion == null ? Optional.empty() : Optional.of(sinceVersion);
        this.deprecatedSince = deprecatedSince == null ? Optional.empty() : Optional.of(deprecatedSince);
    }

    private String mix(Optional<String> parent, Optional<String> child) {
        return parent.orElse(child.orElse(null));
    }

    public LifeCycleMetaData mix(LifeCycleMetaData md) {
        return build(mix(this.sinceVersion, md.sinceVersion), mix(this.deprecatedSince, md.deprecatedSince));
    }

    /**
     * Returns the version since the node is available.
     *
     * @return version since the node is available or empty if not specified
     */
    public Optional<String> getSinceVersion() {
        return this.sinceVersion;
    }

    /**
     * Returns the version since then node is deprecated.
     *
     * @return version since the node is deprecated or empty if not deprecated
     */
    public Optional<String> getDeprecated() {
        return this.deprecatedSince;
    }
}
