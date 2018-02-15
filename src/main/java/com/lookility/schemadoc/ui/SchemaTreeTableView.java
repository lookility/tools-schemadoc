package com.lookility.schemadoc.ui;

import com.lookility.schemadoc.model.Node;
import com.lookility.schemadoc.model.PathFormatter;
import com.lookility.schemadoc.model.Tree;
import com.lookility.schemadoc.model.TreeWalker;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;

public class SchemaTreeTableView extends TreeTableView<Node> {

    private static final String FIXED_COLUMN_PREFIX = "_";
    private static final String HIDABLE_COLUMN_PREFIX = "#";

    private final Tree tree;
    private String language;
    private final PathFormatter pathFormatter;
    private final TreeItemCellFormatter cellFormatter;


    public SchemaTreeTableView(Tree tree, String language) {
        if (tree == null) throw new IllegalArgumentException("tree must not be null");
        this.language = language;
        this.tree = tree;
        this.pathFormatter = new PathFormatter(PathFormatter.NamespaceRepresentation.prefixOnly, this.tree);
        this.cellFormatter = new TreeItemCellFormatter(this.pathFormatter);


        buildTreeTableView();
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

    private final TreeItem<Node> buildTree() {
        TreeItemBuilder tib = new TreeItemBuilder();
        new TreeWalker(tib).walk(this.tree);
        return tib.getRoot();
    }

}
