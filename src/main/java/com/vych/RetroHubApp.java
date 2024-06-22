package com.vych;

import atlantafx.base.theme.Dracula;
import com.vych.ui.scene.main.MainSceneWrapper;
import com.vych.ui.stage.StageManager;
import javafx.application.Application;
import javafx.stage.Stage;

public class RetroHubApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        Application.setUserAgentStylesheet(new Dracula().getUserAgentStylesheet());
        StageManager.show(StageManager.add(new MainSceneWrapper()));
    }

    public static void main(String[] args) {
        launch(args);
    }
}