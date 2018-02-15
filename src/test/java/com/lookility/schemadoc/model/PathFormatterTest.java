package com.lookility.schemadoc.model;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

public class PathFormatterTest  {

    private Tree tree;
    private ContentNode child;
    private AttributeNode attrib;


    @BeforeEach
    public void treeSetup() throws Exception {
        tree = new Tree("unittest");
        Namespace ns = tree.registerNamespace("urn:xsd:unittest", "ut");

        ContentNode root = new ContentNode(new NName(ns, "root"));
        tree.setRoot(root);

        attrib = new AttributeNode(new NName(null, "attrib"));
        root.add(attrib);

        child = new ContentNode(new NName(ns, "child"));

        root.add(child);
    }

    @Test
    public void buildPath() {
        PathFormatter pf = new PathFormatter(PathFormatter.NamespaceRepresentation.prefixOnly, this.tree);

        assertEquals("/ut:root/ut:child", pf.formatPath(this.child));
        assertEquals("/ut:root/@attrib", pf.formatPath(this.attrib));
    }

    @Test
    public void buildPathWithoutNamespace() {
        PathFormatter pf = new PathFormatter(PathFormatter.NamespaceRepresentation.none, this.tree);

        assertEquals("/root/child", pf.formatPath(this.child));
        assertEquals("/root/@attrib", pf.formatPath(this.attrib));
    }

    @Test
    public void buildPathWithFullNamespace() {
        PathFormatter pf = new PathFormatter(PathFormatter.NamespaceRepresentation.uriAndPrefix, this.tree);

        assertEquals("/{urn:xsd:unittest}ut:root/{urn:xsd:unittest}ut:child", pf.formatPath(this.child));
        assertEquals("/{urn:xsd:unittest}ut:root/@attrib", pf.formatPath(this.attrib));
    }

    @Test
    public void buildPathWithNamespaceURI() {
        PathFormatter pf = new PathFormatter(PathFormatter.NamespaceRepresentation.uriOnly, this.tree);

        assertEquals("/{urn:xsd:unittest}root/{urn:xsd:unittest}child", pf.formatPath(this.child));
        assertEquals("/{urn:xsd:unittest}root/@attrib", pf.formatPath(this.attrib));
    }
}
