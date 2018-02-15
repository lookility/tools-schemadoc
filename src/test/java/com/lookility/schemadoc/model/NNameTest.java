package com.lookility.schemadoc.model;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class NNameTest {

    @Test
    public void createNodeNameFromFullName() {
       NName name;

       name = NName.valueOf("test");
       assertFalse(name.getNamespace().isPresent());
       assertEquals("test", name.getName());

       name = NName.valueOf("{urn:test}test");
       assertTrue(name.getNamespace().isPresent());
       assertEquals("urn:test", name.getNamespace().get().getURI());
       assertEquals("test", name.getName());

       try {
           NName.valueOf(null);
           fail("IllegalArgumentException expected");
       } catch (IllegalArgumentException e) {
           // succeeded
       }

        try {
            NName.valueOf("");
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // succeeded
        }

        try {
            NName.valueOf("{urn:testtest");
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // succeeded
        }
    }

    @Test
    public void createNodeName() {
        Namespace ns = new Namespace("uri");

        NName n = new NName(ns, "name");
        assertEquals("name", n.getName());
        assertEquals(ns, n.getNamespace().get());
    }

    @Test
    public void createNodeWithEmptyNamespace() {
        NName n = new NName(null, "name");
        assertEquals("name", n.getName());
        assertFalse(n.getNamespace().isPresent());
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

    @Test
    public void getFullNodeNameWithoutNamespace() {
        NName nn = new NName(null, "test");
        assertEquals("test", nn.getFullName());
    }

    @Test
    public void getShortNameWithNamespace() {
        Namespace ns = new Namespace("urn:test");
        NName nn = new NName(ns, "test");
        assertEquals("{urn:test}test", nn.getFullName());
    }

    @Test
    public void jsonSerializeAndDeserialze() throws Exception {
        ObjectMapper om = new ObjectMapper();

        final String fullName = "{urn:test}test";

        NName n1 = NName.valueOf(fullName);

        String json = om.writeValueAsString(n1);

        assertEquals("\"" + fullName + "\"", json);

        NName n2 = om.readValue(json, NName.class);
        assertNotNull(n2);
        assertEquals(n1, n2);
    }
}
