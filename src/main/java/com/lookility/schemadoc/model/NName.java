package com.lookility.schemadoc.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonValue;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/**
 * Qualified node name.
 *
 * A qualified node name consists of a namespace and a name.
 *
 * @see Namespace
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class NName {

    private final Namespace namespace;
    private final String name;

    /**
     * Creates a node name from a string representation (full name).
     *
     * @param fullName full name of the node name
     * @return created node name
     *
     * @see #getFullName()
     */
    @JsonCreator
    public static NName valueOf(final String fullName) {
        if (fullName == null || fullName.isEmpty()) throw new IllegalArgumentException("full name must not be null or empty");

        Namespace namespace = null;
        String name = null;

        if (fullName.startsWith("{")) {
            int namespaceEndIndex = fullName.indexOf("}");
            if (namespaceEndIndex < 0) throw new IllegalArgumentException("missing closing '}' for namespace in full name '" + fullName + "'");

            namespace = new Namespace(fullName.substring(1, namespaceEndIndex));
            name = fullName.substring(namespaceEndIndex + 1);
        } else {
            name = fullName;
        }
        return new NName(namespace, name);
    }

    /**
     * Constructs a node name.
     * @param namespace namespace of node, or <i>null</i> if node has no namespace
     * @param name name of the node
     */
    public NName(Namespace namespace, String name) {
        if (!isValidName(name)) throw new IllegalArgumentException("invalid node name: '" + name + "'");

        this.namespace = (namespace == null || namespace.isNoNamespace()) ? null : namespace;
        this.name = name;
    }

    /**
     * Returns the namespace of the node.
     * @return namespace or {@link Optional#empty()} if node name has no namespace
     */
    @NotNull
    public Optional<Namespace> getNamespace() {
        return Optional.ofNullable(this.namespace);
    }

    /**
     * Returns the name of a node.
     * @return node name
     */
    @NotNull
    public String getName() {
        return this.name;
    }

    /**
     * Return the full name representation of the node name.
     *
     * @return full name
     */
    @JsonValue
    public String getFullName() {
        if (this.namespace != null) {
            return this.namespace + this.name;
        }
        return this.name;
    }

    /**
     *
     * @return
     */
    @Override
    public String toString() {
        if (this.namespace == null)
            return this.name;
        return this.namespace.toString() + this.name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NName nName = (NName) o;

        if (!this.name.equals(nName.name)) return false;
        if (this.namespace == null) {
            if (nName.namespace == null)
                return true;
            return false;
        }

        return this.namespace.equals(nName.namespace);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        if (this.namespace != null) {
            result = 31 * result + name.hashCode();
        }
        return result;
    }

    public static boolean isValidName(String name) {
        if (name == null || name.isEmpty()) return false;
        if (name.contains(":")) return false;
        if (name.contains("{")) return false;
        if (name.contains("}")) return false;
        return true;
    }
}
