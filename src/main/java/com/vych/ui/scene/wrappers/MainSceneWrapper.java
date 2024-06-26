package com.vych.ui.scene.wrappers;

import com.vych.api.games.GamesApi;
import com.vych.api.games.entities.GameTitle;
import com.vych.ui.scene.controllers.MainSceneController;
import com.vych.ui.stage.StageManager;

import java.io.IOException;

/**
 * Wrapper for MainScene
 */
public class MainSceneWrapper extends SceneWrapper {
    public MainSceneWrapper() throws IOException {
        super("/fxml/MainScene.fxml", "RetroHub", false, MainSceneController.class);

        MainSceneController controller = loader.getController();

        // filling titles list with data from repo
        for (GameTitle gameTitle : GamesApi.getAllTitles()) {
            controller.addElementToGamesListView(gameTitle);
        }
        controller.setSelectedGamesListViewElement(0);
        controller.fillSettingsFromDB();
    }

    /**
     * Override basic behavior.
     * If this scene closed, all other scenes should be closed too
     */
    @Override
    public void onCloseRequest() {
        super.onCloseRequest();
        StageManager.closeAll();
    }
}
