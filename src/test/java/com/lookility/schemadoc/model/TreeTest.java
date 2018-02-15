package com.lookility.schemadoc.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.Optional;

public class TreeTest {

    private Tree buildTree() throws Exception {
        ContentNode root = new ContentNode(new NName(new Namespace("uri:root"), "root"));
        root.getAnnotation().setVersion(new Version(0,1,0));

        ContentNode child1 = new ContentNode(new NName(null, "child1"));
        child1.getAnnotation().setVersion(new Version(0,2,0));

        ContentNode child2 = new ContentNode(new NName(null, "child2"));

        AttributeNode attrib1 = new AttributeNode(new NName(null, "a1"));
        attrib1.getAnnotation().setVersion(new Version(0,2,0 ));

        AttributeNode attrib2 = new AttributeNode(new NName(null, "a2"));

        Tree tree = new Tree("root");
        tree.setRoot(root);
        root.add(child1);
        root.add(child2);

        child1.add(attrib1);
        child2.add(attrib2);

        return tree;
    }

    @Test
    public void createTree() {
        Tree tree = new Tree("test");
        assertEquals("test", tree.getName());
    }

    @Test
    public void maxVersion() throws Exception {
        Tree tree = buildTree();

        Optional<Version> maxVersion = tree.getMaxVersion();

        assertTrue(maxVersion.isPresent());
        assertEquals(new Version(0,2,0), maxVersion.get());
    }

    @Test
    public void failToApplyVerisionDueToSmalerVersion() throws Exception {
        Tree tree = buildTree();

        try {
            tree.applyVersion(new Version(0,1,0));
            fail("IllegalArgumentException expected");
        } catch(IllegalArgumentException e) {
            // succeeded
        }
    }

    @Test
    public void applyVersion() throws Exception {
        Tree tree = buildTree();
        ContentNode child2 = tree.getRoot().getChild(new NName(null, "child2")).get();
        AttributeNode attrib2 = child2.getAttribute(new NName(null, "a2")).get();

        Version version = new Version(1);

        assertFalse(child2.getAnnotation().getVersion().isPresent());
        assertFalse(attrib2.getAnnotation().getVersion().isPresent());

        tree.applyVersion(version);

        assertEquals(version, child2.getAnnotation().getVersion().get());
        assertEquals(version, attrib2.getAnnotation().getVersion().get());
    }

    @Test
    public void jsonSerializeAndDeserialize() throws Exception {
        Tree t1 = buildTree();
        t1.registerNamespace("urn:test", "t");

        ObjectMapper om = new ObjectMapper();
        om.registerModule(new Jdk8Module());

        String json = om.writeValueAsString(t1);

        Tree t2 = om.readValue(json, Tree.class);

        assertNotNull(t2);
        assertEquals(t1.getName(), t2.getName());
        assertEquals(t1.getRoot().getName(), t2.getRoot().getName());
        assertEquals("t", t2.getPrefix("urn:test"));
    }
}
