package com.vych.api.games;

import com.fasterxml.jackson.core.type.TypeReference;
import com.vych.api.RequestApi;
import com.vych.api.entities.RequestTemplate;
import com.vych.api.entities.Response;
import com.vych.api.games.entities.Game;
import com.vych.api.games.entities.GameTitle;
import com.vych.database.AppDatabase;

import java.util.HashMap;
import java.util.List;

import static com.vych.api.entities.Headers.ACCEPT;
import static com.vych.api.entities.MediaTypes.JSON;
import static com.vych.database.SettingsDefaults.REPOSITORY_IP;

/**
 * Provide access to games specific data from repository
 */
public class GamesApi {
    private static final HashMap<String, Game> loadedGames = new HashMap<>();

    /**
     * Get game from repo by ID.
     *
     * @param id game id
     * @return instance of requested game
     */
    public static Game getGame(String id) {
        if (loadedGames.containsKey(id)) {
            return loadedGames.get(id).updateInfo();
        }

        loadedGames.put(id, new Game(id));
        return loadedGames.get(id);
    }

    /**
     * Get list of all available in repo titles
     *
     * @return list of titles
     */
    public static List<GameTitle> getAllTitles() {
        RequestTemplate rqTemplate = RequestApi.getInstance().getRequestBuilder()
                .setPath(AppDatabase.getSettings().get(REPOSITORY_IP.getName()) + "/games/get_all_titles")
                .addHeader(ACCEPT, JSON)
                .build();
        Response result = RequestApi.getInstance().execute(rqTemplate);

        return result.getParsed(new TypeReference<List<GameTitle>>() {
        });
    }

    /**
     * Get cover art for specific game in repo
     *
     * @param id game id
     * @return path to cover art file
     */
    public static String getCoverArt(String id) {
        return getGame(id).getCover();
    }
}
