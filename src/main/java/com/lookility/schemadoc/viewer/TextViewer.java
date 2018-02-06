package com.lookility.schemadoc.viewer;

import com.lookility.schemadoc.model.*;

import java.util.Optional;

public class TextViewer implements TreeHandler {

    private final StringBuilder text = new StringBuilder();
    private final String language;

    private String currentIndent = "";

    public TextViewer(Tree tree, String language) {
        this.language = language;
        TreeWalker walker = new TreeWalker(this);
        walker.walk(tree);
    }


    public String view() {
        return this.text.toString();
    }

    @Override
    public void onTreeBegin(String name) {
    }


    @Override
    public void onNodeBegin(ContentNode node, boolean first, boolean last) {
        addNode(node, "", first, last);

        if (!node.isRoot()) {
            increaseIndent(last);
        }
    }

    @Override
    public void onNodeEnd(ContentNode node, boolean first, boolean last) {
        decreaseIndent();
    }

    @Override
    public void onAttribute(AttributeNode attrib, boolean first, boolean last) {
        addNode(attrib, "@", first, last);
    }

    @Override
    public void onTreeEnd() {

    }

    private void addNode(Node node, String namePrefix, boolean first, boolean last) {
        appendIndent();
        if (!node.isRoot()) {
            appendNodeMarker(last, node.getOccurrence());
        }
        this.text.append(namePrefix);
        this.text.append(node.getName());
        this.text.append(" [").append(node.getType().orElse("")).append("]");

        appendVersion(node.getAnnotation().getVersion());

        String doc = node.getDocumentation().getText(this.language);
        if (doc.length() > 0) {
            this.text.append(' ');
            appendText(doc);
        }

        this.text.append('\n');
    }

    private void increaseIndent(boolean last) {
        if (last) {
            this.currentIndent += "  ";
        } else {
            this.currentIndent += "| ";
        }
    }

    private void decreaseIndent() {
        if (this.currentIndent.length() >= 2) {
            this.currentIndent = this.currentIndent.substring(0, this.currentIndent.length() - 2);
        }
    }

    private void appendIndent() {
        this.text.append(this.currentIndent);
    }

    private void appendNodeMarker(boolean last, Occurrence occurrence) {
        if (last) {
            this.text.append("`-");
        } else {
            this.text.append("|-");
        }

        appendOccurrence(occurrence);
    }

    private void appendOccurrence(Occurrence o) {
        switch (o) {
            case mandatory:
                this.text.append(' ');
                break;
            case optional:
                this.text.append('?');
                break;
            case repeatable:
                this.text.append('*');
                break;
            case moreThanOne:
                this.text.append('+');
                break;
        }
    }

    private void appendVersion(Optional<Version> version) {
        if (version == null || !version.isPresent())
            return;

        this.text.append('{');
        this.text.append(version.get().toString());
        this.text.append('}');
    }

    private void appendText(String txt) {
        txt = txt.replace("\n", "\\n");
        txt = txt.replace("\r", "\\r");
        this.text.append(txt);
    }
}
