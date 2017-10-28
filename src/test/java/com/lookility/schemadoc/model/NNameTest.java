package com.lookility.schemadoc.model;


import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.fail;

public class NNameTest {

    @Test
    public void createNodeName() {
        Namespace ns = Namespace.create("uri", "prefix");

        NName n = new NName(ns, "name");
        assertEquals("name", n.getName());
        assertEquals(ns, n.getNamespace());
    }

    @Test
    public void createNodeWithEmptyNamespace() {
        NName n = new NName(null, "name");
        assertEquals("name", n.getName());
        assertSame(Namespace.NO_NAMESPACE, n.getNamespace());
    }

    @Test
    public void expectErrorOnCreatingNameWithEmptyName() {
        try {
            new NName(null, "");
            fail("IllegalArgumentException expected");
        } catch(IllegalArgumentException e) {
            // succeeded
        }
        try {
            new NName(null, null);
            fail("IllegalArgumentException expected");
        } catch(IllegalArgumentException e) {
            // succeeded
        }
    }
}
