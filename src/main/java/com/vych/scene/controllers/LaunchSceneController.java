package com.vych.scene.controllers;

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
 */
public class LaunchSceneController {

    private int prevIndex = 0;
    private final String title;

    @FXML
    private ListView<Label> romsListView;

    /**
     * * YOU SHOULD CALL {@link LaunchSceneController#init()} AFTER LOAD SCENE WITH {@link FXMLLoader#load()}
     *
     * @param title Selected game dir path
     */
    public LaunchSceneController(String title) {
        this.title = title;
    }

    /**
     * YOU SHOULD CALL THIS METHOD AFTER LOAD SCENE WITH {@link FXMLLoader#load()}
     * <p>
     * Method fill list of available ROMs to {@link ListView} in scene
     * and select added items in it.
     */
    @SuppressWarnings("DataFlowIssue")
    // Scene would not be opened if at least one rom existed (same with game directory)
    public void init() {
        ObservableList<Label> items = romsListView.getItems();
        for (File rom : new File(buildPathString(ROMS_PATH, title)).listFiles()) {
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
                "emulators\\blastem-win32-0.6.2\\blastem.exe" +
                        " \"" + getListViewSelectedItem(romsListView).getId() + "\""
        );
    }

}
