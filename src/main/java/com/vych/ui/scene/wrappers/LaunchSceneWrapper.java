package com.vych.ui.scene.wrappers;

import com.vych.ui.scene.controllers.LaunchSceneController;

/**
 * Wrapper for LaunchScene
 */
public class LaunchSceneWrapper extends SceneWrapper {
    public LaunchSceneWrapper(String title, String selectedGame) {
        super("/fxml/LaunchRomScene.fxml", title, false, LaunchSceneController.class);
        LaunchSceneController controller = loader.getController();
        controller.init(selectedGame);
    }
}
