package com.lookility.schemadoc.model;

import java.util.HashSet;
import java.util.Set;

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
    public void onContentNodeBegin(ContentNode node, boolean first, boolean last) {
        this.availableLanguages.addAll(node.getDocumentation().getAvailableLanguages());
    }

    @Override
    public void onContentNodeEnd(ContentNode node, boolean first, boolean last) {

    }

    @Override
    public void onGroupNodeBegin(GroupNode group, boolean first, boolean last) {

    }

    @Override
    public void onGroupNodeEnd(GroupNode group, boolean first, boolean last) {

    }

    @Override
    public void onTreeEnd() {

    }
}
