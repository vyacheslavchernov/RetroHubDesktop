package com.vych.ui.scene.controllers;

import com.vych.api.RequestsApi;
import com.vych.api.entities.RomInfo;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static com.vych.utils.FilesUtils.*;
import static com.vych.utils.RomsUtils.checkIsRomExist;
import static com.vych.utils.RomsUtils.getRomInfoByPath;
import static com.vych.utils.SceneComponentsUtils.getListViewSelectedIndex;
import static com.vych.utils.SceneComponentsUtils.getListViewSelectedItem;

/**
 * Controller for MainScene fxml scene.
 * Handle downloading and deleting ROMs for specific games.
 * <p>
 * YOU SHOULD CALL {@link ManageRomsSceneController#init(List, String)} METHOD AFTER LOAD SCENE WITH {@link FXMLLoader#load()}
 * <p>
 */
public class ManageRomsSceneController {

    private int prevIndex = 0;
    private List<RomInfo> romInfoList;
    private String gameTitle;

    // region: Scene components linking
    @FXML
    private ListView<Label> romsListView;

    @FXML
    private Label romTitle;

    @FXML
    private Label romRegion;

    @FXML
    private Label romType;

    @FXML
    private Button deleteButton;

    @FXML
    private Button downloadButton;
    // endregion: Scene components linking

    /**
     * YOU SHOULD CALL THIS METHOD AFTER LOAD SCENE WITH {@link FXMLLoader#load()}
     * <p>
     * Method fill list of available ROMs to {@link ListView} in scene
     * and select added items in it and display info about it.
     * After, call method that check if any of these ROMs already downloaded and mark it
     */
    public void init(List<RomInfo> romInfoList, String gameTitle) {
        this.romInfoList = this.romInfoList == null ? romInfoList : this.romInfoList;
        this.gameTitle = this.gameTitle == null ? gameTitle : this.gameTitle;

        ObservableList<Label> items = romsListView.getItems();
        for (RomInfo rom : romInfoList) {
            Label lbl = new Label(rom.getPath());
            lbl.setTextFill(Color.RED);
            items.add(lbl);
        }
        romsListView.setItems(items);
        romsListView.getSelectionModel().select(0);
        loadInfoAboutSelectedRomsListViewElement();
        checkAndMarkLocalExistenceOfRoms();
    }

    /**
     * Handler for clicks on {@link ListView} in scene.
     * Update info about selected item.
     * Prevent unnecessary actions if was clicked selected item or blank space.
     */
    @FXML
    public void handleMouseClickOnRomsListView() {
        int index = getListViewSelectedIndex(romsListView);
        if (index != prevIndex) {
            prevIndex = index;
            loadInfoAboutSelectedRomsListViewElement();
        }
    }

    /**
     * Display info about selected ROM and manage controls based on ROM local existence
     */
    private void loadInfoAboutSelectedRomsListViewElement() {
        RomInfo romInfo = getRomInfoByPath(
                getListViewSelectedItem(romsListView).getText(),
                this.romInfoList
        );

        // linter complain about possibility of NullPointerException
        // (it can't be happens as I see, but well, will be highlighted it otherwise)
        if (romInfo == null) {
            romInfo = new RomInfo();
        }

        romTitle.setText("Title: " + romInfo.getTitle());
        romRegion.setText("Region: " + romInfo.getRegion());
        romType.setText("Type: " + (romInfo.isOfficial() ? "official" : "unofficial"));

        if (checkIsRomExist(gameTitle, romInfo.getPath())) {
            downloadButton.setVisible(false);
            deleteButton.setVisible(true);
        } else {
            downloadButton.setVisible(true);
            deleteButton.setVisible(false);
        }
    }

    /**
     * Update information in {@link ListView} with ROMs.
     * If ROM downloaded, mark it by GREEN color in list, otherwise RED color.
     * Manage controls for selected ROM.
     * Delete ROMs local folder if zero ROMs existed locally.
     */
    private void checkAndMarkLocalExistenceOfRoms() {
        Path path = Paths.get(buildPathString(ROMS_PATH, gameTitle));
        if (!Files.exists(path)) {
            return;
        }

        boolean isAnyExist = false;
        ObservableList<Label> items = romsListView.getItems();
        int lblIndex = 0;
        for (Label lbl : items) {
            path = Paths.get(buildPathString(ROMS_PATH, gameTitle, lbl.getText()));
            if (Files.exists(path)) {
                isAnyExist = true;
                lbl.setTextFill(Color.GREEN);
                if (lblIndex == getListViewSelectedIndex(romsListView)) {
                    downloadButton.setVisible(false);
                    deleteButton.setVisible(true);
                }
            } else {
                lbl.setTextFill(Color.RED);
                if (lblIndex == getListViewSelectedIndex(romsListView)) {
                    downloadButton.setVisible(true);
                    deleteButton.setVisible(false);
                }
            }
            lblIndex++;
        }
        romsListView.setItems(items);

        if (!isAnyExist) {
            deleteFile(buildPathString(ROMS_PATH, gameTitle));
        }
    }

    /**
     * Delete local copy of ROM selected in {@link ListView}.
     * Then call method for update information in {@link ListView}.
     */
    @FXML
    private void deleteSelectedRom() {
        deleteFile(buildPathString(ROMS_PATH, gameTitle, getListViewSelectedItem(romsListView).getText()));
        checkAndMarkLocalExistenceOfRoms();
    }

    /**
     * Download copy of ROM selected in {@link ListView}.
     * Then call method for update information in {@link ListView}.
     *
     * @throws IOException ...
     */
    @FXML
    private void downloadSelectedRom() throws IOException {
        RequestsApi.getRom(gameTitle, getListViewSelectedItem(romsListView).getText());
        checkAndMarkLocalExistenceOfRoms();
    }
}
