package com.lookility.schemadoc.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SchemaDocApp extends Application{

    private Stage stage;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.stage = primaryStage;

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/javafx/schemadoc.fxml"));
        Parent root = loader.load();

        SchemaDocController controller = loader.getController();
        controller.init(primaryStage);

        Scene scene = new Scene(root, 600, 300);
        scene.getStylesheets().addAll("/javafx/schemadoc.css");

        stage.setScene(scene);
        stage.show();

    }
}
