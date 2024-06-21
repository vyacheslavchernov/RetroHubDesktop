package com.vych.ui.scene.wrappers;

import com.vych.api.games.entities.Game;
import com.vych.ui.scene.controllers.LaunchSceneController;

/**
 * Wrapper for LaunchScene
 */
public class LaunchSceneWrapper extends SceneWrapper {
    public LaunchSceneWrapper(String title, Game game) {
        super("/fxml/LaunchRomScene.fxml", title, false, LaunchSceneController.class);
        LaunchSceneController controller = loader.getController();
        controller.init(game);
    }
}
