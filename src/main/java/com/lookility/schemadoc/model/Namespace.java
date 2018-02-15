package com.lookility.schemadoc.model;

import com.fasterxml.jackson.annotation.JsonValue;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

/**
 * Namespace of a node name.
 *
 * @see NName
 */
public class Namespace implements Comparable<Namespace>{

    private static final String EMPTY_URI = "";
    private static final HashMap<String, String> cachedURIs = new HashMap<>();

    private final String uri;

    public Namespace() {
        this(null);
    }

    /**
     * Constructs a namespace.
     * @param uri URI identifying the namespace
     */
    public Namespace(String uri) {
        if (uri == null || uri.isEmpty()) {
            this.uri = EMPTY_URI;
        } else {
            String cachedURI = cachedURIs.get(uri);
            if (cachedURI == null) {
                cachedURIs.put(uri, uri);
                cachedURI = uri;
            }
            this.uri = cachedURI;
        }
    }


    /**
     * Checks if namespace represents an empty namespace.
     *
     * @return <i>true</i> if namespace represents empty namespace, <i>false</i> otherwise
     */
    public boolean isNoNamespace() {
        return this.uri.isEmpty();
    }

    /**
     * Returns the URI of the namespace.
     *
     * @return URI of namespace or empty string if namespace represents empty namespace
     */
    @NotNull
    @JsonValue
    public String getURI() {
        return this.uri;
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
        if (this.uri.isEmpty()) return this.uri;

        return "{" + this.uri + "}";
    }

    @Override
    public int compareTo(@NotNull Namespace o) {
        return this.uri.compareTo(o.uri);
    }
}
