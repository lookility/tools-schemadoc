package com.lookility.schemadoc.ui;

import com.lookility.schemadoc.model.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;

public class SchemaDocController {

    private static final String FIXED_COLUMN_PREFIX = "_";
    private static final String HIDABLE_COLUMN_PREFIX = "#";

    private static final PathFormatter PATH_FORMATTER = new PathFormatter(PathFormatter.NamespaceRepresentation.prefixOnly, null);
    private static final TreeItemCellFormatter CELL_FORMATTER = new TreeItemCellFormatter(PATH_FORMATTER);

    private static final String LANG_PREFIX = "menuLanguage#";

    private Stage stage;

    @FXML
    private MenuBar menuBar;

    @FXML
    private MenuItem menuOpen;

    @FXML
    private MenuItem menuClose;

    @FXML
    private MenuItem menuReload;

    @FXML
    private TabPane tabPane;

    @FXML
    private Menu menuLang;

    @FXML
    private Menu menuColumns;

    @FXML
    private TextField textField;

    private SchemaDocModel model = new SchemaDocModel();

    private String currentLanguage = Documentation.DEFAULT_LANGUAGE;

    public SchemaDocController() {

    }

    public void init(Stage stage) {
        this.stage = stage;
        this.menuBar.setUseSystemMenuBar(true);
        this.tabPane.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Tab>() {
            @Override
            public void changed(ObservableValue<? extends Tab> observable, Tab oldValue, Tab newValue) {
                TreeTableView<Node> ttv = null;
                if (newValue != null) {
                    ttv = (TreeTableView) newValue.getContent();

                    TreeItem<Node> selectedItem = ttv.getSelectionModel().getSelectedItem();
                    if (selectedItem != null) {
                        textField.setText(PATH_FORMATTER.formatPath(selectedItem.getValue()));
                    } else {
                        textField.setText(null);
                    }
                }

                buildColumnsMenu(ttv);
            }
        });
    }

    @FXML
    protected void handleMenuOpen(ActionEvent event) {
        FileChooser fc = new FileChooser();
        fc.setTitle("Open XML Schema File");
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML Schema Files", "*.xsd"));
        File file = fc.showOpenDialog(this.stage);

        if (file != null) {
            try {
                this.model.openFile(file);
                buildLanguageMenu();
                buildTabPane();
            } catch (Exception e) {
                showError("Error opening XML Schema file", e);
            }
        }
        updateUI();
    }

    @FXML
    protected void handleMenuClose(ActionEvent event) {
        this.model.close();
        updateUI();
    }

    @FXML
    protected void handleMenuReload(ActionEvent event) {
        try {
            this.model.reload();
            this.currentLanguage = Documentation.DEFAULT_LANGUAGE;
            buildLanguageMenu();
            buildTabPane();
        } catch (Exception e) {
            showError("Error reloading XML Schema file", e);

        }
        updateUI();
    }

    @FXML
    protected void handleMenuExit(ActionEvent event) {
        stage.close();
    }

    @FXML
    protected void handleAbout(ActionEvent event) {
        showAbout();
    }

    @FXML
    protected void handleLanguage(ActionEvent event) {
        CheckMenuItem cmi = (CheckMenuItem) event.getTarget();
        this.currentLanguage = extractLanguageFromMenuItem(cmi);
        updateLanguageMenu();
        buildTabPane();
    }

    @FXML
    protected void handleShowColumn(ActionEvent event) {
        CheckMenuItem cmi = (CheckMenuItem) event.getTarget();

        TreeTableColumn col = (TreeTableColumn) cmi.getUserData();
        col.setVisible(cmi.isSelected());
    }

    private String extractLanguageFromMenuItem(MenuItem mi) {
        return mi.getId().substring(LANG_PREFIX.length());
    }

    private void buildLanguageMenu() {
        CheckMenuItem lm;

        this.menuLang.getItems().clear();

        lm = new CheckMenuItem();
        lm.setText("Default");
        lm.setId(LANG_PREFIX);
        lm.setSelected(true);
        lm.setOnAction(this::handleLanguage);
        this.menuLang.getItems().add(lm);

        // add all languages
        for (String language : this.model.getAvailableLanguages()) {
            if (Documentation.DEFAULT_LANGUAGE.equals(language)) {
                continue;
            }
            lm = new CheckMenuItem();
            lm.setSelected(false);
            lm.setText(language);
            lm.setId(LANG_PREFIX + language);
            lm.setOnAction(this::handleLanguage);
            this.menuLang.getItems().add(lm);
        }
    }

    private void buildColumnsMenu(TreeTableView<Node> ttv) {
        this.menuColumns.getItems().clear();
        this.menuColumns.setDisable(true);

        if (ttv == null) return;

        CheckMenuItem cm;

        // add all hideable columns
        TreeTableColumn col;
        for(int i = 0; i < ttv.getColumns().size();i++) {
            col = ttv.getColumns().get(i);

            if (!col.getId().startsWith(HIDABLE_COLUMN_PREFIX))
                continue;

            cm = new CheckMenuItem();
            cm.setSelected(true);
            cm.setText(col.getText());
            cm.setId(col.getId());
            cm.setOnAction(this::handleShowColumn);
            cm.setUserData(col);

            this.menuColumns.getItems().add(cm);
        }

        this.menuColumns.setDisable(false);
    }

    private void updateLanguageMenu() {
        CheckMenuItem cmi;
        boolean selected = false;

        for (MenuItem mi : this.menuLang.getItems()) {
            cmi = (CheckMenuItem) mi;
            selected = this.currentLanguage.equals(extractLanguageFromMenuItem(cmi));

            cmi.setSelected(selected);
        }
    }

    private void buildTabPane() {
        this.tabPane.getTabs().clear();

        for (Tree tree : this.model.getTreeContainer().trees()) {
            this.tabPane.getTabs().add(buildTab(tree, this.currentLanguage));
        }
    }

    private Tab buildTab(Tree tree, String language) {
        Tab tab = new Tab();
        tab.setText(tree.getName());

        TreeItem<Node> root = buildTree(tree, language);

        TreeTableColumn<Node, String> colName = new TreeTableColumn<Node, String>("Name");
        colName.setCellValueFactory((TreeTableColumn.CellDataFeatures<Node, String> param) -> CELL_FORMATTER.formatName(param.getValue().getValue()));
        colName.setSortable(false);
        colName.setId(FIXED_COLUMN_PREFIX + "_name");

        TreeTableColumn<Node, String> colOccurrence = new TreeTableColumn<Node, String>("Occurrence");
        colOccurrence.setCellValueFactory((TreeTableColumn.CellDataFeatures<Node, String> param) -> CELL_FORMATTER.formatOccurrence(param.getValue().getValue()));
        colOccurrence.setSortable(false);
        colOccurrence.setMinWidth(30);
        colOccurrence.setMaxWidth(30);
        colOccurrence.setStyle("-fx-alignment: center");
        colOccurrence.setId(FIXED_COLUMN_PREFIX + "_occur");

        TreeTableColumn<Node, String> colBaseType = new TreeTableColumn("Base Type");
        colBaseType.setCellValueFactory((TreeTableColumn.CellDataFeatures<Node, String> param) -> CELL_FORMATTER.formatBaseType(param.getValue().getValue()));
        colBaseType.setSortable(false);
        colBaseType.setId(FIXED_COLUMN_PREFIX + "_baseType");

        TreeTableColumn<Node, String> colType = new TreeTableColumn("Type");
        colType.setCellValueFactory((TreeTableColumn.CellDataFeatures<Node, String> param) -> CELL_FORMATTER.formatType(param.getValue().getValue()));
        colType.setSortable(false);
        colType.setId(HIDABLE_COLUMN_PREFIX + "_type");

        TreeTableColumn<Node, String> colVer = new TreeTableColumn("Version");
        colVer.setCellValueFactory((TreeTableColumn.CellDataFeatures<Node, String> param) -> CELL_FORMATTER.formatVersion(param.getValue().getValue()));
        colVer.setSortable(false);
        colVer.setMinWidth(50);
        colVer.setMaxWidth(100);
        colVer.setId(HIDABLE_COLUMN_PREFIX + "_version");

        TreeTableColumn<Node, String> colDesc = new TreeTableColumn("Description");
        colDesc.setCellValueFactory((TreeTableColumn.CellDataFeatures<Node, String> param) -> CELL_FORMATTER.formatDescription(param.getValue().getValue(), language));
        colDesc.setSortable(false);
        colDesc.setId(HIDABLE_COLUMN_PREFIX + "_descr");

        TreeTableView<Node> ttv = new TreeTableView<>(root);
        ttv.getColumns().addAll(colName, colOccurrence, colBaseType, colType, colVer, colDesc);
        ttv.getRoot().setExpanded(true);
        ttv.setColumnResizePolicy(TreeTableView.CONSTRAINED_RESIZE_POLICY);
        ttv.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<TreeItem<Node>>() {
            @Override
            public void changed(ObservableValue<? extends TreeItem<Node>> observable, TreeItem<Node> oldValue, TreeItem<Node> newValue) {
                if (newValue != null) {
                    textField.setText(PATH_FORMATTER.formatPath(newValue.getValue()));
                } else {
                    textField.setText(null);
                }
            }
        });

        tab.setContent(ttv);
        return tab;
    }

    private TreeItem<Node> buildTree(Tree tree, String language) {
        TreeItemBuilder tib = new TreeItemBuilder();
        new TreeWalker(tib).walk(tree);
        return tib.getRoot();
    }

    private void updateUI() {
        boolean loaded = this.model.isLoaded();

        menuOpen.setDisable(loaded);
        menuReload.setDisable(!loaded);
        menuClose.setDisable(!loaded);
        menuLang.setDisable(!loaded);
        this.tabPane.setVisible(loaded);
    }

    private void showError(String title, Exception e) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        if (e.getMessage() != null) {
            alert.setContentText(e.getMessage());
        } else {
            PrintWriter w = new PrintWriter(new StringWriter());
            e.printStackTrace(w);
            alert.setContentText(w.toString());
        }
        alert.showAndWait();
    }

    private void showAbout() {
        Alert about = new Alert(Alert.AlertType.INFORMATION);
        about.setHeaderText(null);
        about.setContentText("(C) by Martin Look");
        about.showAndWait();
    }
}
