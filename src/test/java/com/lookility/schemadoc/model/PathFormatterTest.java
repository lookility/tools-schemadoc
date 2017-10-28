package com.lookility.schemadoc.model;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

public class PathFormatterTest  {

    private Tree tree;
    private ContentNode child;
    private ContentNode attrib;


    @BeforeEach
    public void treeSetup() {
        tree = new Tree("unittest");
        Namespace ns = tree.registerNamespace("urn:xsd:unittest", "ut");

        ContentNode root = new ContentNode(new NName(ns, "root"));
        tree.setRoot(root);

        GroupNode attributeGroup = new GroupNode(GroupNodeType.attributes);
        attrib = new ContentNode(new NName(tree.registerNamespace(null), "attrib"), true);
        attributeGroup.addChild(attrib);

        GroupNode sequenceGroup = new GroupNode(GroupNodeType.sequence);
        child = new ContentNode(new NName(ns, "child"));
        sequenceGroup.addChild(child);

        root.addChild(attributeGroup);
        root.addChild(sequenceGroup);
    }

    @Test
    public void constructPathFormatter() {
        PathFormatter pf = new PathFormatter();

        assertSame(pf, pf.withSuppressedAttributeIndicator(false));
        assertSame(pf, pf.withSuppressedAttributeIndicator(false));
    }

    @Test
    public void buildPath() {
        PathFormatter pf = new PathFormatter();

        assertEquals("/ut:root/ut:child", pf.formatPath(this.child));
        assertEquals("/ut:root/@attrib", pf.formatPath(this.attrib));
    }

    @Test
    public void buildPathWithoutNamespace() {
        PathFormatter pf = new PathFormatter().withSuppressedNamespace(true);

        assertEquals("/root/child", pf.formatPath(this.child));
        assertEquals("/root/@attrib", pf.formatPath(this.attrib));
    }

    @Test
    public void buildPathWithoutAttributeIndicator() {
        PathFormatter pf = new PathFormatter().withSuppressedAttributeIndicator(true);

        assertEquals("/ut:root/ut:child", pf.formatPath(this.child));
        assertEquals("/ut:root/attrib", pf.formatPath(this.attrib));
    }

    @Test
    public void buildPathWithoutNamespaceAndAttributeIndicator() {
        PathFormatter pf = new PathFormatter().withSuppressedNamespace(true).withSuppressedAttributeIndicator(true);

        assertEquals("/root/child", pf.formatPath(this.child));
        assertEquals("/root/attrib", pf.formatPath(this.attrib));
    }
}
