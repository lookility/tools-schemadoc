package com.lookility.schemadoc.model;

import java.util.*;

/**
 * Tree handler to scan for languages used in the documentation.
 */
class LanguageScanner implements TreeHandler {

    private final SortedSet<String> availableLanguages = new TreeSet<>();

    public SortedSet<String> getAvailableLanguages() {
        return this.availableLanguages;
    }

    @Override
    public void onTreeBegin(Tree tree) {
        this.availableLanguages.clear();

    }

    @Override
    public void onNodeBegin(ContentNode node, boolean first, boolean last) {
        this.availableLanguages.addAll(node.getDocumentation().getAvailableLanguages());
    }

    @Override
    public void onNodeEnd(ContentNode node, boolean first, boolean last) {

    }

    @Override
    public void onAttribute(AttributeNode attrib, boolean first, boolean last) {
        this.availableLanguages.addAll(attrib.getDocumentation().getAvailableLanguages());
    }

    @Override
    public void onTreeEnd() {

    }
}
