package com.lookility.schemadoc.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * Set of registered namespaces.
 *
 * @see Namespace
 */
public class NamespaceSet {

    /**
     * Empty namespace prefix.
     */
    public static final String EMPTY_PREFIX = "";

    private int nextPrefixIndex = 1;

    private final SortedMap<Namespace, String> namespaces = new TreeMap<Namespace, String>();

    NamespaceSet() {
    }

    /**
     * Register a new namespace with a predefined prefix.
     * <p>
     * <p>If the namespace is already registered the existing namespace is returned. In this case the prefix of the existing namespace has to be equal to the specified prefix.</p>
     *
     * @param namespaceURI URI of namespace
     * @param prefix       predefined prefix
     * @return registered namespace
     *
     * @throws IllegalArgumentException if prefix is already defined or differs from existing namespace
     */
    public Namespace registerNamespace(String namespaceURI, String prefix) {
        if (namespaceURI == null) namespaceURI = "";
        if (prefix == null) prefix = "";

        Namespace ns = getNamespaceByURI(namespaceURI);

        if (ns != null) {
            if (!prefix.equals(getPrefix(ns))) {
                throw new IllegalArgumentException("prefix '" + prefix + "' differs from existing prefix '" + getPrefix(ns) + "' for namespace '" + ns.getURI() + "'");
            }
        } else {
            ns = getNamespaceByPrefix(prefix);
            if (ns != null) {
                throw new IllegalArgumentException("prefix '" + prefix + "' already defined for namespace '" + ns.getURI() + "', it can't be used for namespace '" + namespaceURI + "'");
            }
            ns = new Namespace(namespaceURI);

            this.namespaces.put(ns, prefix);
        }
        return ns;
    }


    /**
     * Returns the prefix of a given namespace URI.
     *
     * <p>If the URI is unknown the new URI will be added to the namespace set.</p>
     *
     * @param uri namespace URI to get the prefix for
     * @return prefix or {@link #EMPTY_PREFIX} if URI is null or empty
     *
     * @see #getPrefix(Namespace)
     */
    @NotNull
    public String getPrefix(@Nullable String uri) {
        if (uri == null || uri.isEmpty()) return EMPTY_PREFIX;

        return getPrefix(new Namespace(uri));
    }

    /**
     * Returns the prefix of given namespace.
     *
     * <p>If the namespace is unknown the new namespace will be added to the namespace set.</p>
     *
     * @param namespace namespace to get the prefix for
     * @return prefix or {@link #EMPTY_PREFIX} if namespace is null
     */
    @NotNull
    public String getPrefix(@Nullable Namespace namespace) {
        if (namespace == null) return EMPTY_PREFIX;

        String prefix = this.namespaces.get(namespace);
        if (prefix == null) {
            prefix = generatePrefix();
            this.namespaces.put(namespace, prefix);
        }

        return prefix;
    }

    /**
     * Returns a namespace by a given URI.
     *
     * @param namespaceURI URI of namespace
     * @return namespace or <i>null</i> if namespace doesn't exists
     */
    @JsonIgnore
    private Namespace getNamespaceByURI(String namespaceURI) {
        for(Map.Entry<Namespace, String> entry : this.namespaces.entrySet()) {
            if (entry.getKey().getURI().equals(namespaceURI)) {
                return entry.getKey();
            }
        }
        return null;
    }

    /**
     * Returns a namespace by a given prefix.
     *
     * @param prefix prefix of namespace
     * @return namespace or <i>null</i> if namespace doesn't exists
     */
    @JsonIgnore
    private Namespace getNamespaceByPrefix(String prefix) {
        for(Map.Entry<Namespace, String> entry : this.namespaces.entrySet()) {
            if (entry.getValue().equals(prefix)) {
                return entry.getKey();
            }
        }
        return null;
    }

    /**
     * Returns the next index to generate prefix.
     * @return next index
     */
    public int getNextPrefixIndex() {
        return this.nextPrefixIndex;
    }

    /**
     * Returns registered namespaces and according prefixes.
     *
     * @return map of namespaces and according prefix
     */
    @NotNull
    public SortedMap<Namespace, String> getNamespaces() {
        return this.namespaces;
    }

    /**
     * Generates a new unique prefix.
     *
     * @return generated prefix
     */
    private String generatePrefix() {
        return "ns" + this.nextPrefixIndex++;
    }
}
