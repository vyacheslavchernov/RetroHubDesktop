package com.vych.ui.scene.main;

import com.vych.ui.scene.SceneWrapper;
import com.vych.ui.stage.StageManager;

/**
 * Wrapper for MainScene
 */
public class MainSceneWrapper extends SceneWrapper {
    public MainSceneWrapper() {
        super("/fxml/MainScene.fxml", "RetroHub", false, MainSceneController.class);
        MainSceneController controller = loader.getController();
        controller.init();
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
