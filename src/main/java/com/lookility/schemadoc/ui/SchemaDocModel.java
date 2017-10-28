package com.lookility.schemadoc.ui;

import com.lookility.schemadoc.xsd.ModelBuilderException;
import com.lookility.schemadoc.model.TreeContainer;
import com.lookility.schemadoc.xsd.ModelBuilder;

import java.io.File;
import java.io.IOException;
import java.util.SortedSet;

public class SchemaDocModel {

    private ModelBuilder currentSchema = null;
    private TreeContainer treeContainer = null;

    public void openFile(File file) throws IOException, ModelBuilderException {
        close();
        this.currentSchema = new ModelBuilder(file, null);
        this.treeContainer = this.currentSchema.buildAll(null);
    }

    public void close() {
        this.currentSchema = null;
        this.treeContainer = null;
    }

    public void reload() throws IOException, ModelBuilderException {
        this.treeContainer = null;
        this.currentSchema.reload();
        this.treeContainer = this.currentSchema.buildAll(null);
    }

    public TreeContainer getTreeContainer() {
        return this.treeContainer;
    }

    public boolean isLoaded() {
        return this.treeContainer != null;
    }

    public SortedSet<String> getAvailableLanguages() {
        if (this.treeContainer == null) return null;

        return this.treeContainer.getAvailableLanguages();
    }
}
