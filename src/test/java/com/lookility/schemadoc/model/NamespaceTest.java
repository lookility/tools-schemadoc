package com.lookility.schemadoc.model;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.fail;

public class NamespaceTest {

    @Test
    public void createEmptyNamespace() {
        Namespace ns;

        ns = Namespace.create("", "");
        assertSame(Namespace.NO_NAMESPACE, ns);

        ns = Namespace.create(null, null);
        assertSame(Namespace.NO_NAMESPACE, ns);

        ns = Namespace.create("", null);
        assertSame(Namespace.NO_NAMESPACE, ns);
    }

    @Test
    public void expectErrorOnCreatingEmptyNamespaceWithPrefix() {
        try {
            Namespace.create("", "prefix");
            fail("IllegalArgumentException expected");
        } catch(IllegalArgumentException e) {
            // succeeded
        }

        try {
            Namespace.create(null, "prefix");
            fail("IllegalArgumentException expected");
        } catch(IllegalArgumentException e) {
            // succeeded
        }
    }

    @Test
    public void createNonEmptyNamespace() {
        Namespace ns;

        ns = Namespace.create("uri", "prefix");
        assertEquals("uri", ns.getURI());
        assertEquals("prefix", ns.getPrefix());
    }

    @Test
    public void expectErrorOnCreatingNonEmptyNamespaceWithEmptyPrefix() {
        try {
            Namespace.create("uri", "");
            fail("IllegalArgumentException expected");
        } catch(IllegalArgumentException e) {
            // succeeded
        }

        try {
            Namespace.create("uri", null);
            fail("IllegalArgumentException expected");
        } catch(IllegalArgumentException e) {
            // succeeded
        }
    }
}
