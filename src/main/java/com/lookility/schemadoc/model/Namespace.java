package com.lookility.schemadoc.model;

/**
 * Namespace of a node name.
 *
 * @see NName
 */
public class Namespace {

    /**
     * Constant representing an empty namespace.
     */
    public static final Namespace NO_NAMESPACE = new Namespace();

    private final String uri;
    private final String prefix;

    /**
     * Creates a new namespace.
     *
     * @param uri URI identifying the namespace, or <i>null</i> or <i>empty</i> to indicate a not defined  namespace
     * @param prefix prefix used as an abbreviation for the namespace, or <i>null</i> or <i>empty</i> if no namespace is defined
     * @return created namespace or {@link #NO_NAMESPACE} if namespace is not defined
     *
     * @throws IllegalArgumentException if prefix is defined for an empty namespace
     * @throws IllegalArgumentException if prefix is empty for non empty namespace
     */
    public static Namespace create(String uri, String prefix) {
        if (uri == null || uri.isEmpty()) {
            if (prefix != null && !prefix.isEmpty())
                throw new IllegalArgumentException("prefix must not be specified for empty namespaces");
            return NO_NAMESPACE;
        }
        if (prefix == null || prefix.isEmpty()) {
            throw new IllegalArgumentException("prefix must not be empty");
        }
        return new Namespace(uri, prefix);
    }

    private Namespace() {
        this.uri = "";
        this.prefix = "";
    }

    /**
     * Constructs a namespace.
     * @param uri URI identifying the namespace
     * @param prefix prefix used for node names as an abbreviation for the namespace.
     */
    private Namespace(String uri, String prefix) {
        assert uri != null;
        assert !uri.isEmpty();

        if (prefix == null) prefix = "";

        this.uri = uri;
        this.prefix = prefix;
    }


    /**
     * Checks if namespace represents an empty namespace.
     *
     * @return <i>true</i> if namespace represents empty namespace, <i>false</i> otherwise
     */
    public boolean isNoNamespace() {
        return NO_NAMESPACE.equals(this);
    }

    /**
     * Returns the URI of the namespace.
     *
     * @return URI of namespace or empty string if namespace represents empty namespace
     */
    public String getURI() {
        return this.uri;
    }

    /**
     * Return the prefix of the namespace.
     *
     * @return prefix of namespace or empty string if namespace represents empty namespace
     */
    public String getPrefix() {
        return this.prefix;
    }

    /**
     * Checks if namespace are equal.
     *
     * <p>Namespace are equal if their namespace URIs are equal.</p>
     * @param namespace
     * @return <i>true</i> if namespaces are equal, <i>false</i> otherwise
     */
    @Override
    public boolean equals(Object namespace) {
        if (this == namespace) return true;
        if (!(namespace instanceof Namespace)) return false;

        Namespace ns = (Namespace) namespace;

        return uri.equals(ns.uri);
    }

    @Override
    public int hashCode() {
        return uri.hashCode();
    }

    @Override
    public String toString() {
        if (this == NO_NAMESPACE) return "";

        return "{" + this.uri + "}";
    }
}
