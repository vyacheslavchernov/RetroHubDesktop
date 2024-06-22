package com.vych.ui.scene.main;

import com.vych.api.games.GamesApi;
import com.vych.api.games.entities.GameTitle;
import com.vych.ui.scene.BackgroundTask;
import com.vych.ui.scene.BaseController;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import lombok.SneakyThrows;

import java.util.List;

import static com.vych.utils.SceneComponentsUtils.getListViewSelectedItem;

/**
 * Controller for MainScene fxml scene.
 * Handle, load and display information about available in repo titles.
 * <p>
 * {@link MainSceneController#init()} need to be called when scene was loaded by {@link javafx.fxml.FXMLLoader}
 */
public class MainSceneController extends BaseController {
    private String selectedGameId = null;

    @FXML
    private ListView<TitlesListItem> titlesList;

    /**
     * Scene initialisation.
     * Call this method after scene was loaded by {@link javafx.fxml.FXMLLoader}
     */
    public void init() {
        runBackground(new LoadAvailableTitles());

        titlesList.setOnMouseClicked(event -> {
            if (selectedGameId == null) {
                return;
            }

            TitlesListItem selectedItem = getListViewSelectedItem(titlesList);
            if (selectedGameId.equals(selectedItem.getTitleId())) {
                return;
            }

            loadGameAsSelected(selectedItem.getTitleId());
            selectedGameId = selectedItem.getTitleId();
        });
    }

    @SneakyThrows
    private void loadGameAsSelected(String gameId) {
        // TODO
    }

    /**
     * Background task that load information about available titles from repo,
     * then fill titles list view ({@link MainSceneController#titlesList}) with that information
     */
    private class LoadAvailableTitles implements BackgroundTask<List<GameTitle>> {
        @Override
        public List<GameTitle> doInBackground() {
            List<GameTitle> loadedTitles = GamesApi.getAllTitles();
            for (GameTitle title : loadedTitles) {
                title.setCover(GamesApi.getCoverArt(title.getId()));
            }
            return loadedTitles;
        }

        @Override
        public void fxAfter(List<GameTitle> bgResult) {
            ObservableList<TitlesListItem> items = titlesList.getItems();
            for (GameTitle title : bgResult) {
                items.add(new TitlesListItem(title.getTitle(), title.getId(), title.getCover()));
            }
            titlesList.setItems(items);
            titlesList.getSelectionModel().select(0);
            selectedGameId = getListViewSelectedItem(titlesList).getTitleId();

            loadGameAsSelected(getListViewSelectedItem(titlesList).getTitleId());
        }
    }
}
