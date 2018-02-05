package com.lookility.schemadoc.ui;

import com.lookility.schemadoc.model.AbstractPathFormatter;
import com.lookility.schemadoc.model.Node;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ObservableValue;

public class TreeItemCellFormatter {

    private final AbstractPathFormatter pathFormatter;

    public TreeItemCellFormatter(AbstractPathFormatter pathFormatter) {
        if (pathFormatter == null)
            throw new IllegalArgumentException("path formatter must not be null");
        this.pathFormatter = pathFormatter;
    }

    public ObservableValue<String> formatName(final Node node) {
        return new ReadOnlyStringWrapper(this.pathFormatter.getLocalName(node));
    }

    public ObservableValue<String> formatOccurrence(final Node node) {
        return new ReadOnlyStringWrapper(node.getOccurrence().getShortName());
    }

    public ObservableValue<String> formatVersion(final Node node) {
        if (!node.getVersion().isPresent()) {
            return new ReadOnlyStringWrapper("");
        }
        StringBuilder version = new StringBuilder();
        return new ReadOnlyStringWrapper(node.getVersion().get().toString());
    }

    public ObservableValue<String> formatDescription(final Node node, String language) {
        return new ReadOnlyStringWrapper(node.getDocumentation().getText(language));
    }

    public ObservableValue<String> formatType(final Node node) {
        return new ReadOnlyStringWrapper(node.getType().orElse(""));
    }

    public ObservableValue<String> formatBaseType(final Node node) {
        return new ReadOnlyStringWrapper(node.getBaseType().name());
    }
}
