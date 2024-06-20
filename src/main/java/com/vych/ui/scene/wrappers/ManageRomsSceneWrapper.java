package com.vych.ui.scene.wrappers;

import com.vych.api.entities.RomInfo;
import com.vych.ui.scene.controllers.ManageRomsSceneController;

import java.util.List;

/**
 * Wrapper for ManageRomsScene
 */
public class ManageRomsSceneWrapper extends SceneWrapper{
    public ManageRomsSceneWrapper(String title, List<RomInfo> romInfoList, String selectedGame) {
        super("/fxml/ManageRomsScene.fxml", title, false, ManageRomsSceneController.class);

        ManageRomsSceneController controller = loader.getController();
        controller.init(romInfoList, selectedGame);
    }
}
