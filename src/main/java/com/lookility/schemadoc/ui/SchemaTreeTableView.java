package com.lookility.schemadoc.ui;

import com.lookility.schemadoc.model.Node;
import com.lookility.schemadoc.model.PathFormatter;
import com.lookility.schemadoc.model.Tree;
import com.lookility.schemadoc.model.TreeWalker;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class SchemaTreeTableView extends TreeTableView<Node> implements ChangeListener<TreeItem<Node>> {

    private static final String FIXED_COLUMN_PREFIX = "_";
    private static final String HIDABLE_COLUMN_PREFIX = "#";

    private final Tree tree;
    private final PathFormatter pathFormatter;
    private final TreeItemCellFormatter cellFormatter;

    private final List<SelectedPathChangedListener> listeners = new ArrayList<>();

    /**
     * Currently selected language for descriptions.
     */
    private String language;


    /**
     * Create a schema tree table view.
     *
     * <p>The schema tree table view displays a hierarchical tree model.</p>
     * @param tree tree to be displayed
     * @param language documentation language or <i>null</i> if default language should be used
     */
    public SchemaTreeTableView(@NotNull Tree tree, @Nullable String language) {
        if (tree == null) throw new IllegalArgumentException("tree must not be null");
        this.language = language;
        this.tree = tree;
        this.pathFormatter = new PathFormatter(PathFormatter.NamespaceRepresentation.prefixOnly, this.tree);
        this.cellFormatter = new TreeItemCellFormatter(this.pathFormatter);

        buildTreeTableView();

        getSelectionModel().selectedItemProperty().addListener(this);
    }

    private final void buildTreeTableView() {
        TreeTableColumn<Node, String> colName = new TreeTableColumn<Node, String>("Name");
        colName.setCellValueFactory((TreeTableColumn.CellDataFeatures<Node, String> param) -> cellFormatter.formatName(param.getValue().getValue()));
        colName.setSortable(false);
        colName.setId(FIXED_COLUMN_PREFIX + "_name");

        TreeTableColumn<Node, String> colOccurrence = new TreeTableColumn<Node, String>("Occurrence");
        colOccurrence.setCellValueFactory((TreeTableColumn.CellDataFeatures<Node, String> param) -> cellFormatter.formatOccurrence(param.getValue().getValue()));
        colOccurrence.setSortable(false);
        colOccurrence.setMinWidth(30);
        colOccurrence.setMaxWidth(30);
        colOccurrence.setStyle("-fx-alignment: center");
        colOccurrence.setId(FIXED_COLUMN_PREFIX + "_occur");

        TreeTableColumn<Node, String> colBaseType = new TreeTableColumn("Base Type");
        colBaseType.setCellValueFactory((TreeTableColumn.CellDataFeatures<Node, String> param) -> cellFormatter.formatBaseType(param.getValue().getValue()));
        colBaseType.setSortable(false);
        colBaseType.setId(FIXED_COLUMN_PREFIX + "_baseType");

        TreeTableColumn<Node, String> colType = new TreeTableColumn("Type");
        colType.setCellValueFactory((TreeTableColumn.CellDataFeatures<Node, String> param) -> cellFormatter.formatType(param.getValue().getValue()));
        colType.setSortable(false);
        colType.setId(HIDABLE_COLUMN_PREFIX + "_type");

        TreeTableColumn<Node, String> colVer = new TreeTableColumn("Version");
        colVer.setCellValueFactory((TreeTableColumn.CellDataFeatures<Node, String> param) -> cellFormatter.formatVersion(param.getValue().getValue()));
        colVer.setSortable(false);
        colVer.setMinWidth(50);
        colVer.setMaxWidth(100);
        colVer.setId(HIDABLE_COLUMN_PREFIX + "_version");

        TreeTableColumn<Node, String> colDesc = new TreeTableColumn("Description");
        colDesc.setCellValueFactory((TreeTableColumn.CellDataFeatures<Node, String> param) -> cellFormatter.formatDescription(param.getValue().getValue(), language));
        colDesc.setSortable(false);
        colDesc.setId(HIDABLE_COLUMN_PREFIX + "_descr");

        setRoot(buildTree());
        getColumns().addAll(colName, colOccurrence, colBaseType, colType, colVer, colDesc);
        getRoot().setExpanded(true);
        setColumnResizePolicy(TreeTableView.CONSTRAINED_RESIZE_POLICY);
    }

    /**
     * Return the path of the currently selected item.
     * @return path of selected item or {@link Optional#empty()} if no item selected
     */
    public Optional<String> getSelectedItemPath() {
        String path = null;
        TreeItem<Node> selectedItem = getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            path = this.pathFormatter.formatPath(selectedItem.getValue());
        }

        return Optional.ofNullable(path);
    }

    public void addListener(@NotNull final SelectedPathChangedListener listener) {
        this.listeners.add(Objects.requireNonNull(listener));
    }

    @Override
    public void changed(ObservableValue<? extends TreeItem<Node>> observable, TreeItem<Node> oldValue, TreeItem<Node> newValue) {
        if (!this.listeners.isEmpty()) {
            Optional<String> optionalPath = Optional.empty();
            if (newValue != null) {
                optionalPath = Optional.of(this.pathFormatter.formatPath(newValue.getValue()));
            }

            for(SelectedPathChangedListener listener : this.listeners) {
                listener.changed(optionalPath);
            }
        }
    }

    private final TreeItem<Node> buildTree() {
        TreeItemBuilder tib = new TreeItemBuilder();
        new TreeWalker(tib).walk(this.tree);
        return tib.getRoot();
    }

}
