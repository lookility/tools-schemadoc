package com.lookility.schemadoc.model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class NodeTest {

    private Node createNode(NName name) {
        return new Node(name) {
            @Override
            public boolean isLeaf() {
                return false;
            }
        };
    }

    @Test
    public void equalNodes() {
        Node node1 = createNode(new NName(null, "node"));
        Node node2 = createNode(new NName(null, "node"));

        assertEquals(node1, node2);
    }

    @Test
    public void nonEqualDueToDifferentNamespace() {
        Node node1 = createNode(new NName(Namespace.create("ns1", "p1"), "node"));
        Node node2 = createNode(new NName(Namespace.create("ns2", "p2"), "node"));

        assertNotEquals(node1, node2);
    }

    @Test
    public void nonEqualDueToDifferentName() {
        Node node1 = createNode(new NName(null, "node1"));
        Node node2 = createNode(new NName(null, "node2"));

        assertNotEquals(node1, node2);
    }

    @Test
    public void nonEqualDueToDifferentBaseType() {
        Node node1 = createNode(new NName(null, "node"));
        node1.setBaseType(BaseType.bool);
        Node node2 = createNode(new NName(null, "node"));
        node2.setBaseType(BaseType.str);

        assertNotEquals(node1, node2);
    }

    @Test
    public void nonEqualDueToDifferentType() {
        Node node1 = createNode(new NName(null, "node"));
        node1.setType("type1");
        Node node2 = createNode(new NName(null, "node"));
        node2.setType("type2");

        assertNotEquals(node1, node2);
    }
}
