package com.lookility.schemadoc.viewer;

import com.lookility.schemadoc.model.Tree;
import com.lookility.schemadoc.model.TreeHandler;
import com.lookility.schemadoc.model.TreeWalker;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

public abstract class AbstractFileWriterViewer implements TreeHandler {

    private final TreeWalker walker;
    private Writer writer;
    private String language;

    public AbstractFileWriterViewer() { this.walker = new TreeWalker(this);}

    public synchronized void write(File file, Tree tree, String language) throws IOException {
        if (tree == null) throw new IllegalArgumentException("tree must not be null");
        if (file == null) throw new IllegalArgumentException("file must not be null");

        this.language = language;

        this.writer = new FileWriter(file);
        this.walker.walk(tree);
        this.writer.flush();
        this.writer.close();
        this.writer = null;
    }

    protected String getLanguage() {
        return this.language;
    }

    protected Writer getWriter() {
        return this.writer;
    }
}
