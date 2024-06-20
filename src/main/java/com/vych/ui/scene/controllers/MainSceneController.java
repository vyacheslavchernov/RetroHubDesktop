package com.vych.ui.scene.controllers;

import com.vych.api.RequestsApi;
import com.vych.api.entities.Title;
import com.vych.api.entities.TitleAbout;
import com.vych.api.entities.TitleFullInfo;
import com.vych.database.AppDatabase;
import com.vych.database.accessors.SettingsAccessor;
import com.vych.ui.scene.wrappers.LaunchSceneWrapper;
import com.vych.ui.scene.wrappers.ManageRomsSceneWrapper;
import com.vych.ui.stage.StageManager;
import com.vych.utils.RomsUtils;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import lombok.SneakyThrows;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.vych.utils.SceneComponentsUtils.getListViewSelectedIndex;
import static com.vych.utils.SceneComponentsUtils.getListViewSelectedItem;

/**
 * Controller for MainScene fxml scene.
 * Handle, load and display information about available in repo titles.
 */
public class MainSceneController {

    private int prevIndex = -1;
    private TitleFullInfo titleFullInfo = null;
    private List<TextField> touchedFields = new ArrayList<>();

    // region: Scene components linking
    // region:Library tab
    @FXML
    private ListView<Label> gamesListView;

    @FXML
    private Label selectedGameTitle;

    @FXML
    private Label selectedGamePlatform;

    @FXML
    private Label selectedGameRelease;

    @FXML
    private ImageView selectedGameCover;

    @FXML
    private Label selectedGameRating;

    @FXML
    private Label selectedGameDeveloper;

    @FXML
    private Label selectedGamePlayers;

    @FXML
    private TextArea descriptionArea;
    // endregion:Library tab

    // region:Settings tab
    @FXML
    private TextField repository_ip;
    // endregion:Settings tab

    // endregion: Scene components linking

    /**
     * Get settings from local DB and put in fields on settings tab
     */
    @FXML
    public void fillSettingsFromDB() {
        repository_ip.setText(AppDatabase.getSettings().getString(SettingsAccessor.REPOSITORY_IP_SETTING));
    }

    /**
     * Handle all changes fields in settings tab.
     * Collect them for saving.
     *
     * @param e ..
     */
    @FXML
    private void handleSettingsFields(Event e) {
        TextField target = (TextField) e.getTarget();
        if (!touchedFields.contains(target)) {
            touchedFields.add(target);
        }
    }

    /**
     * Override all changes in fields on settings tab by data from local DB
     */
    @FXML
    private void revertChanges() {
        fillSettingsFromDB();
        touchedFields.clear();
    }

    /**
     * Save all changes in fields on settings tab to local DB
     */
    @FXML
    private void saveChanges() {
        for (TextField field : touchedFields) {
            AppDatabase.getSettings().setString(field.getId(), field.getText());
        }
        touchedFields.clear();
    }

    /**
     * Add item to {@link ListView} of available in repo games
     *
     * @param element title to add
     */
    public void addElementToGamesListView(Title element) {
        ObservableList<Label> items = gamesListView.getItems();
        Label lbl = new Label(element.getTitle());
        lbl.setId(element.getPath());
        items.add(lbl);
        gamesListView.setItems(items);
    }

    /**
     * Set selected element in {@link ListView} of available titles.
     * Wrap updating information about selected title in side panel.
     *
     * @param index index to set
     * @throws IOException ..
     */
    public void setSelectedGamesListViewElement(int index) throws IOException {
        if (index != prevIndex) {
            prevIndex = getListViewSelectedIndex(gamesListView);
            gamesListView.getSelectionModel().select(index);
            loadInfoAboutSelectedGameListViewElement();
        }
    }

    /**
     * Update information about selected game in side panel
     *
     * @throws IOException ..
     */
    public void loadInfoAboutSelectedGameListViewElement() throws IOException {
        Label item = getListViewSelectedItem(gamesListView);
        this.titleFullInfo = RequestsApi.getTitleFullInfo(item.getId());
        String coverPath = RequestsApi.getTitleCover(item.getId());

        selectedGameCover.setImage(new Image(new FileInputStream(coverPath)));

        TitleAbout about = this.titleFullInfo.getAbout();
        selectedGameTitle.setText(about.getTitle());
        selectedGamePlatform.setText("Platform: " + about.getPlatform());
        selectedGameRelease.setText("Released: " + about.getReleased());
        selectedGameDeveloper.setText("Developer: " + about.getDeveloper());
        selectedGamePlayers.setText("Players: " + about.getPlayers());
        selectedGameRating.setText("Rating: " + about.getRating());
        descriptionArea.setText(about.getDescription());
    }

    /**
     * Handler for clicks on {@link ListView} in scene.
     * Update info about selected item.
     * Prevent unnecessary actions if was clicked selected item or blank space.
     */
    @FXML
    private void handleMouseClickOnGamesListView() throws IOException {
        int index = getListViewSelectedIndex(gamesListView);
        if (index != prevIndex) {
            prevIndex = index;
            loadInfoAboutSelectedGameListViewElement();
        }
    }

    /**
     * Handle click on "ROMs manager" button
     * Opens "ROMs manager", by the way.
     */
    @FXML
    private void pressManageRomsButton() {
        openRomsManager();
    }

    /**
     * Handle click on "Play" button
     * Opens game launch scene.
     * Also opens "ROMs manager", by the way (if there's no downloaded ROMs for selected game)
     */
    @FXML
    private void handlePlayButton() {
        if (!RomsUtils.checkIsAnyRomExist(gamesListView.getSelectionModel().getSelectedItem().getId())) {
            // TODO: maybe some sort of builder for alerts?
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Can't play right now");
            alert.setHeaderText("Looks like you don't have downloaded ROMs for this game");
            alert.setContentText("Are you want to open ROMs manager?");
            alert.showAndWait().ifPresent(rs -> {
                if (rs == ButtonType.OK) {
                    System.out.println("Pressed OK.");
                    openRomsManager();
                }
                if (rs == ButtonType.CANCEL) {
                    System.out.println("Pressed CANCEL.");
                }
            });
        } else {
            openLaunchMenu();
        }
    }

    /**
     * Load and show launch game scene.
     * Init scene with selected game data.
     */
    // TODO: Add logging
    @SneakyThrows
    private void openLaunchMenu() {
        String selectedId = getListViewSelectedItem(gamesListView).getId();
        if (this.titleFullInfo == null) {
            this.titleFullInfo = RequestsApi.getTitleFullInfo(selectedId);
        }
        StageManager.show(StageManager.add(
                new LaunchSceneWrapper(
                        "Launch " + titleFullInfo.getAbout().getTitle(),
                        selectedId
                )
        ));
    }

    /**
     * Load and show launch ROMs manager scene.
     * Init scene with selected game data.
     */
    // TODO: Add logging
    @SneakyThrows
    private void openRomsManager() {
        String selectedId = getListViewSelectedItem(gamesListView).getId();
        if (this.titleFullInfo == null) {
            this.titleFullInfo = RequestsApi.getTitleFullInfo(selectedId);
        }
        StageManager.show(StageManager.add(
                new ManageRomsSceneWrapper(
                        "ROMs manager for " + titleFullInfo.getAbout().getTitle(),
                        titleFullInfo.getRoms(),
                        selectedId
                )
        ));
    }
}
