package com.lookility.schemadoc.model;

public interface TreeHandler {

    void onTreeBegin(Tree tree);

    void onNodeBegin(ContentNode node, boolean first, boolean last);

    void onNodeEnd(ContentNode node, boolean first, boolean last);

    void onAttribute(AttributeNode attrib, boolean first, boolean last);

    void onTreeEnd();
}
