package com.vych.ui.scene.wrappers;

import com.vych.api.games.entities.Game;
import com.vych.ui.scene.controllers.ManageRomsSceneController;

/**
 * Wrapper for ManageRomsScene
 */
public class ManageRomsSceneWrapper extends SceneWrapper {
    public ManageRomsSceneWrapper(String title, Game game, String selectedGame) {
        super("/fxml/ManageRomsScene.fxml", title, false, ManageRomsSceneController.class);

        ManageRomsSceneController controller = loader.getController();
        controller.init(game);
    }
}
