package com.lookility.schemadoc.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Set of registered namespaces.
 *
 * @see Namespace
 */
public class NamespaceSet {

    private int genericPrefixCount = 1;

    private Map<String, Namespace> namespacesByPrefix = new HashMap<>();
    private Map<String, Namespace> namespacesByURI = new HashMap<>();

    NamespaceSet() {
    }

    /**
     * Register a new namespace.
     * <p>
     * <p>If the namespace is already registered the exiting namespace is returned. The prefix for the newly registered namespace will be generated automatically and will be unique within this namespace set.</p>
     *
     * @param namespaceURI URI of namespace
     * @return registered namespace
     */
    public Namespace registerNamespace(String namespaceURI) {
        if (namespaceURI == null) namespaceURI = "";

        Namespace ns = this.namespacesByURI.get(namespaceURI);
        if (ns == null) {
            String prefix = namespaceURI.isEmpty() ? "" : generatePrefix();
            ns = Namespace.create(namespaceURI, prefix);

            this.namespacesByURI.put(ns.getURI(), ns);
            this.namespacesByPrefix.put(ns.getPrefix(), ns);
        }

        return ns;
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
        Namespace ns = this.namespacesByURI.get(namespaceURI);
        if (ns != null) {
            if (!prefix.equals(ns.getPrefix())) {
                throw new IllegalArgumentException("prefix '" + prefix + "' differs from existing prefix '" + ns.getPrefix() + "' for namespace '" + ns.getURI() + "'");
            }
        } else {
            ns = this.namespacesByPrefix.get(prefix);
            if (ns != null) {
                throw new IllegalArgumentException("prefix '" + prefix + "' already defined for namespace '" + ns.getURI() + "', it can't be used for namespace '" + namespaceURI + "'");
            }
            ns = Namespace.create(namespaceURI, prefix);
            this.namespacesByURI.put(ns.getURI(), ns);
            this.namespacesByPrefix.put(ns.getPrefix(), ns);
        }
        return ns;
    }

    /**
     * Returns a namespace by a given URI.
     *
     * @param namespaceURI URI of namespace
     * @return namespace or <i>null</i> if namespace doesn't exists
     */
    public Namespace getNamespaceByURI(String namespaceURI) {
        return this.namespacesByURI.get(namespaceURI);
    }

    /**
     * Generates a new unique prefix.
     *
     * @return generated prefix
     */
    private String generatePrefix() {
        return "ns" + this.genericPrefixCount++;
    }
}
