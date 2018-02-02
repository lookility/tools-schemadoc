package com.lookility.schemadoc.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class ContentNodeTest {

    @Test
    public void addUniqueChild() throws Exception {
        ContentNode node = new ContentNode(new NName(null, "parent"));
        ContentNode child = new ContentNode(new NName(null, "child"));

        node.add(child);
        assertTrue(node.containsChild(child.getName()));
    }

    @Test
    public void allowToAddEqualDuplicateEqualChild() throws Exception {
        ContentNode node = new ContentNode(new NName(null, "parent"));
        ContentNode child1 = new ContentNode(new NName(null, "child"));
        ContentNode child2 = new ContentNode(new NName(null, "child"));

        node.add(child1);
        assertTrue(node.containsChild(child1.getName()));
        node.add(child2);
        assertTrue(node.containsChild(child2.getName()));
    }

    @Test
    public void throwDuplicateErrorInCaseOfSameButNotEqualChild() throws Exception {
        ContentNode node = new ContentNode(new NName(null, "parent"));
        ContentNode child1 = new ContentNode(new NName(null, "child"));
        ContentNode child2 = new ContentNode(new NName(null, "child"));
        child2.setBaseType(BaseType.str);

        node.add(child1);
        assertTrue(node.containsChild(child1.getName()));

        try {
            node.add(child2);
            fail("DuplicateNodeException expected");
        } catch(DuplicateNodeException e) {
            // succeeded
        }
    }

    @Test
    public void addUniqueAttribute() throws Exception {
        ContentNode node = new ContentNode(new NName(null, "parent"));
        AttributeNode attib1 = new AttributeNode(new NName(null, "a1"));
        AttributeNode attib2 = new AttributeNode(new NName(null, "a2"));

        node.add(attib1);
        assertTrue(node.containsAttribute(attib1.getName()));
        node.add(attib2);
        assertTrue(node.containsAttribute(attib2.getName()));
    }

    @Test
    public void failDueToDuplicateAttribute() throws Exception {
        ContentNode node = new ContentNode(new NName(null, "parent"));
        AttributeNode attib1 = new AttributeNode(new NName(null, "a"));
        AttributeNode attib2 = new AttributeNode(new NName(null, "a"));

        node.add(attib1);
        assertTrue(node.containsAttribute(attib1.getName()));

        try {
            node.add(attib2);
            fail("DuplicateNodeException expected");
        } catch(DuplicateNodeException e) {
            // succeeded
        }
    }
}
