package com.lookility.schemadoc.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Tree handler to scan for languages used in the documentation.
 */
class LanguageScanner implements TreeHandler {

    private final Set<String> availableLanguages = new HashSet<>();

    public Set<String> getAvailableLanguages() {
        return this.availableLanguages;
    }

    @Override
    public void onTreeBegin(String name) {
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
