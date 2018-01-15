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
        StringBuilder version = new StringBuilder();
        node.getLifeCycle().getSinceVersion().ifPresent(version::append);
        node.getLifeCycle().getDeprecated().ifPresent(v -> {
            if (version.length() > 0) version.append(' ');
            version.append("(! ").append(v).append(')');
        });
        return new ReadOnlyStringWrapper(version.toString());
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
