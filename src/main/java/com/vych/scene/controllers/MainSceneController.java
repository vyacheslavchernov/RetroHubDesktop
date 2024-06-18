package com.vych.scene.controllers;

import com.vych.api.RequestsApi;
import com.vych.api.entities.Title;
import com.vych.api.entities.TitleAbout;
import com.vych.api.entities.TitleFullInfo;
import com.vych.utils.RomsUtils;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.IOException;

import static com.vych.utils.SceneComponentsUtils.getListViewSelectedIndex;
import static com.vych.utils.SceneComponentsUtils.getListViewSelectedItem;

/**
 * Controller for MainScene fxml scene.
 * Handle, load and display information about available in repo titles.
 */
public class MainSceneController {

    private int prevIndex = -1;
    private TitleFullInfo titleFullInfo = null;

    // region: Scene components linking
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
    // endregion: Scene components linking

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
    @SuppressWarnings("CallToPrintStackTrace")
    private void openLaunchMenu() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/LaunchRomScene.fxml"));
            String selectedId = getListViewSelectedItem(gamesListView).getId();

            if (this.titleFullInfo == null) {
                this.titleFullInfo = RequestsApi.getTitleFullInfo(selectedId);
            }

            loader.setController(
                    new LaunchSceneController(selectedId)
            );

            Parent root1 = loader.load();

            LaunchSceneController controller = loader.getController();
            controller.init();

            Stage stage = new Stage();
            stage.setTitle("Launch " + titleFullInfo.getAbout().getTitle());
            stage.setScene(new Scene(root1));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Load and show launch ROMs manager scene.
     * Init scene with selected game data.
     */
    // TODO: Add logging
    @SuppressWarnings("CallToPrintStackTrace")
    private void openRomsManager() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ManageRomsScene.fxml"));
            String selectedId = getListViewSelectedItem(gamesListView).getId();

            if (this.titleFullInfo == null) {
                this.titleFullInfo = RequestsApi.getTitleFullInfo(selectedId);
            }

            loader.setController(
                    new ManageRomsSceneController(this.titleFullInfo.getRoms(), selectedId)
            );

            Parent root1 = loader.load();

            ManageRomsSceneController controller = loader.getController();
            controller.init();

            Stage stage = new Stage();
            stage.setTitle("ROMs manger for " + titleFullInfo.getAbout().getTitle());
            stage.setScene(new Scene(root1));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
