package com.lookility.schemadoc.model;

public interface TreeHandler {

    void onTreeBegin(String name);

    void onContentNodeBegin(ContentNode node, boolean first, boolean last);

    void onContentNodeEnd(ContentNode node, boolean first, boolean last);

    void onGroupNodeBegin(GroupNode group, boolean first, boolean last);

    void onGroupNodeEnd(GroupNode group, boolean first, boolean last);

    void onTreeEnd();
}
