package com.lookility.schemadoc.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

public class TreeTest {

    private Tree buildTree() throws Exception {
        ContentNode root = new ContentNode(new NName(null, "root"));
        root.setVersion(new Version(0,1,0));

        ContentNode child1 = new ContentNode(new NName(null, "child1"));
        child1.setVersion(new Version(0,2,0));

        ContentNode child2 = new ContentNode(new NName(null, "child2"));

        AttributeNode attrib1 = new AttributeNode(new NName(null, "a1"));
        attrib1.setVersion(new Version(0,2,0 ));

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
    public void applyVersiomn() throws Exception {
        Tree tree = buildTree();
        ContentNode child2 = tree.getRoot().getChild(new NName(null, "child2")).get();
        AttributeNode attrib2 = child2.getAttribute(new NName(null, "a2")).get();

        Version version = new Version(1);

        assertFalse(child2.getVersion().isPresent());
        assertFalse(attrib2.getVersion().isPresent());

        tree.applyVersion(version);

        assertEquals(version, child2.getVersion().get());
        assertEquals(version, attrib2.getVersion().get());
    }
}
