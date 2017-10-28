package com.lookility.schemadoc.ui;

import com.lookility.schemadoc.model.Documentation;
import com.lookility.schemadoc.model.Tree;
import com.lookility.schemadoc.model.TreeWalker;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class SchemaDocController {

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
                if (newValue != null) {
                    TreeTableView<UINode> ttv = (TreeTableView) newValue.getContent();

                    TreeItem<UINode> selectedItem = ttv.getSelectionModel().getSelectedItem();
                    if (selectedItem != null) {
                        textField.setText(selectedItem.getValue().currentPath);
                    } else {
                        textField.setText(null);
                    }
                }
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
                buildLanguageMenue();
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
            buildLanguageMenue();
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

    private String extractLanguageFromMenuItem(MenuItem mi) {
        return mi.getId().substring(LANG_PREFIX.length());
    }

    private void buildLanguageMenue() {
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

        TreeItem<UINode> root = buildTree(tree, language);

        TreeTableColumn<UINode, String> colName = new TreeTableColumn<UINode, String>("Name");
        colName.setCellValueFactory((TreeTableColumn.CellDataFeatures<UINode, String> param) -> new ReadOnlyStringWrapper(param.getValue().getValue().name));
        colName.setSortable(false);

        TreeTableColumn<UINode, String> colOccurrence = new TreeTableColumn<UINode, String>("Occurrence");
        colOccurrence.setCellValueFactory((TreeTableColumn.CellDataFeatures<UINode, String> param) -> new ReadOnlyStringWrapper(param.getValue().getValue().occurrence));
        colOccurrence.setSortable(false);
        colOccurrence.setMinWidth(30);
        colOccurrence.setMaxWidth(30);
        colOccurrence.setStyle("-fx-alignment: center");

        TreeTableColumn<UINode, String> colType = new TreeTableColumn("Type");
        colType.setCellValueFactory((TreeTableColumn.CellDataFeatures<UINode, String> param) -> new ReadOnlyStringWrapper(param.getValue().getValue().type));
        colType.setSortable(false);

        TreeTableColumn<UINode, String> colBaseType = new TreeTableColumn("Base Type");
        colBaseType.setCellValueFactory((TreeTableColumn.CellDataFeatures<UINode, String> param) -> new ReadOnlyStringWrapper(param.getValue().getValue().baseType));
        colBaseType.setSortable(false);


        TreeTableColumn<UINode, String> colVer = new TreeTableColumn("Version");
        colVer.setCellValueFactory((TreeTableColumn.CellDataFeatures<UINode, String> param) -> new ReadOnlyStringWrapper(param.getValue().getValue().version));
        colVer.setSortable(false);
        colVer.setMinWidth(50);
        colVer.setMaxWidth(100);

        TreeTableColumn<UINode, String> colDesc = new TreeTableColumn("Description");
        colDesc.setCellValueFactory((TreeTableColumn.CellDataFeatures<UINode, String> param) -> new ReadOnlyStringWrapper(param.getValue().getValue().description));
        colDesc.setSortable(false);

        TreeTableView<UINode> ttv = new TreeTableView<>(root);
        ttv.getColumns().addAll(colName, colOccurrence, colType, colBaseType, colVer, colDesc);
        ttv.getRoot().setExpanded(true);
        ttv.setColumnResizePolicy(TreeTableView.CONSTRAINED_RESIZE_POLICY);
        ttv.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<TreeItem<UINode>>() {
            @Override
            public void changed(ObservableValue<? extends TreeItem<UINode>> observable, TreeItem<UINode> oldValue, TreeItem<UINode> newValue) {
                if (newValue != null) {
                    textField.setText(newValue.getValue().currentPath);
                } else {
                    textField.setText(null);
                }
            }
        });

        tab.setContent(ttv);

        return tab;
    }

    private TreeItem<UINode> buildTree(Tree tree, String language) {
        TreeItemBuilder tib = new TreeItemBuilder(language, false);
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
        if (e.getMessage() != null)
            alert.setContentText(e.getMessage());
        alert.showAndWait();
    }

    private void showAbout() {
        Alert about = new Alert(Alert.AlertType.INFORMATION);
        about.setHeaderText(null);
        about.setContentText("(C) by Martin Look");
        about.showAndWait();
    }
}
