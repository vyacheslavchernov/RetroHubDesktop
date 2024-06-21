package com.vych.ui.scene.controllers;

import com.vych.api.games.entities.Game;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import java.io.File;
import java.io.IOException;

import static com.vych.utils.FilesUtils.ROMS_PATH;
import static com.vych.utils.FilesUtils.buildPathString;
import static com.vych.utils.SceneComponentsUtils.getListViewSelectedIndex;
import static com.vych.utils.SceneComponentsUtils.getListViewSelectedItem;

/**
 * Controller for LaunchRomScene fxml scene.
 * Handle run selected game from specific ROM and with specific emulator
 * <p>
 * YOU SHOULD CALL {@link LaunchSceneController#init(Game)} METHOD AFTER LOAD SCENE WITH {@link FXMLLoader#load()}
 */
public class LaunchSceneController {

    private int prevIndex = 0;
    private Game game;

    @FXML
    private ListView<Label> romsListView;

    /**
     * YOU SHOULD CALL THIS METHOD AFTER LOAD SCENE WITH {@link FXMLLoader#load()}
     * <p>
     * Method fill list of available ROMs to {@link ListView} in scene
     * and select added items in it.
     */
    @SuppressWarnings("DataFlowIssue")
    // Scene would not be opened if at least one rom existed (same with game directory)
    public void init(Game game) {
        this.game = this.game == null ? game : this.game;

        ObservableList<Label> items = romsListView.getItems();
        for (File rom : new File(buildPathString(ROMS_PATH, game.getId())).listFiles()) {
            Label lbl = new Label(rom.getName());
            lbl.setId(rom.getAbsolutePath());
            items.add(lbl);
        }

        romsListView.setItems(items);
        romsListView.getSelectionModel().select(0);
    }

    /**
     * Handler for clicks on {@link ListView} in scene.
     * Prevent unnecessary actions if was clicked selected item or blank space.
     */
    @FXML
    public void handleMouseClickOnRomsListView() {
        int index = getListViewSelectedIndex(romsListView);
        if (index != prevIndex) {
            prevIndex = index;
        }
    }

    /**
     * Launch selected in {@link ListView} ROM with selected emulator
     *
     * @throws IOException ..
     */
    @FXML
    public void handleMouseClickOnLaunchButton() throws IOException {
        // actually, here only one emulator for genesis/MD by now
        Process p = Runtime.getRuntime().exec(
                "downloads\\emulators\\blastem-win32-0.6.2\\blastem.exe" +
                        " \"" + getListViewSelectedItem(romsListView).getId() + "\""
        );
    }

}
