package com.vych.ui.scene.wrappers;

import com.vych.ui.stage.StageManager;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;

import java.net.URL;

/**
 * SceneWrapper wrap javaFx {@link Scene} and contain additional information about it.
 * Wrapper load scene and clean {@link StageManager} after scene was closed
 */
@Getter
@Setter
public abstract class SceneWrapper {
    protected boolean canBeMany;
    protected String title;

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    protected FXMLLoader loader;

    @Setter(AccessLevel.NONE)
    protected Scene scene;

    @Setter(AccessLevel.NONE)
    protected Class<?> controllerClass;

    @Setter(AccessLevel.NONE)
    protected Parent root;

    @SneakyThrows
    public SceneWrapper(String fxml, String title, boolean canBeMany, Class<?> controllerClass) {
        this.canBeMany = canBeMany;
        this.title = title;
        this.controllerClass = controllerClass;

        loader = new FXMLLoader();
        URL xmlUrl = getClass().getResource(fxml);
        loader.setLocation(xmlUrl);
        root = loader.load();
        scene = new Scene(root);
    }

    /**
     * Delete this scene from {@link StageManager} before closing
     */
    public void onCloseRequest() {
        StageManager.delete(title);
    }
}
