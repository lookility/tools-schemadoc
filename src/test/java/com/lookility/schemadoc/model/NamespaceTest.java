package com.lookility.schemadoc.model;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class NamespaceTest {

    @Test
    public void createEmptyNamespace() {
        Namespace ns = new Namespace();
        assertNotNull(ns.getURI());
        assertTrue(ns.getURI().isEmpty());
    }
}
