package com.vych;

import atlantafx.base.theme.Dracula;
import com.vych.api.RequestsApi;
import com.vych.api.entities.Title;
import com.vych.scene.controllers.MainSceneController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class RetroHubApp extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        Application.setUserAgentStylesheet(new Dracula().getUserAgentStylesheet());

        FXMLLoader loader = new FXMLLoader();
        URL xmlUrl = getClass().getResource("/fxml/MainScene.fxml");
        loader.setLocation(xmlUrl);
        Parent root = loader.load();

        MainSceneController controller = loader.getController();

        for (Title title : RequestsApi.getAllTitles()) {
            controller.addElementToGamesListView(title);
        }
        controller.setSelectedGamesListViewElement(0);
        controller.fillSettingsFromDB();


        primaryStage.setTitle("RetroHub");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}