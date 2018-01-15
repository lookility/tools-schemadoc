package com.lookility.schemadoc.model;

/**
 * Qualified node name.
 *
 * A qualified node name consists of a namespace and a name.
 *
 * @see Namespace
 */
public class NName {

    private final Namespace namespace;
    private final String name;

    /**
     * Constructs a node name.
     * @param namespace namespace of node, or <i>null</i> if node has no namespace
     * @param name name of the node
     */
    public NName(Namespace namespace, String name) {
        if (name == null || name.isEmpty()) throw new IllegalArgumentException("name must not be null or empty");
        if (namespace == null) namespace = Namespace.NO_NAMESPACE;

        this.namespace = namespace;
        this.name = name;
    }

    /**
     * Returns the namespace of the node or {@link Namespace#NO_NAMESPACE} if node name has no namespace.
     * @return namespace or {@link Namespace#NO_NAMESPACE} if node name has no namespace
     */
    public Namespace getNamespace() {
        return this.namespace;
    }

    /**
     * Returns the name of a node.
     * @return node name
     */
    public String getName() {
        return this.name;
    }

    /**
     *
     * @return
     */
    @Override
    public String toString() {
        return this.namespace.toString() + this.name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NName nName = (NName) o;

        if (!namespace.equals(nName.namespace)) return false;
        return name.equals(nName.name);
    }

    @Override
    public int hashCode() {
        int result = namespace.hashCode();
        result = 31 * result + name.hashCode();
        return result;
    }
}
