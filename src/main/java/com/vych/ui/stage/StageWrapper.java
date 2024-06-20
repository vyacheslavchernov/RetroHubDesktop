package com.vych.ui.stage;

import com.vych.ui.scene.wrappers.SceneWrapper;
import javafx.stage.Stage;

/**
 * StageWrapper wrap javaFx {@link Stage}.
 * Wrapper create stage from provided {@link SceneWrapper} and manage basic behavior.
 */
public class StageWrapper {
    private final SceneWrapper sceneWrapper;
    private final Stage stage = new Stage();

    public StageWrapper(SceneWrapper sceneWrapper) {
        this.sceneWrapper = sceneWrapper;

        stage.setTitle(sceneWrapper.getTitle());
        stage.setScene(sceneWrapper.getScene());

        stage.setOnCloseRequest(windowEvent -> sceneWrapper.onCloseRequest());
    }

    public String getTitle() {
        return sceneWrapper.getTitle();
    }

    public void show() {
        stage.show();
    }

    public void focus() {
        stage.requestFocus();
    }

    public void close() {
        stage.close();
    }
}
