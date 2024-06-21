package com.vych.api.games.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vych.api.RequestApi;
import com.vych.api.entities.RequestTemplate;
import com.vych.api.entities.Response;
import com.vych.cache.AppCache;
import com.vych.database.AppDatabase;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;

import java.io.File;
import java.util.List;

import static com.vych.api.entities.Headers.ACCEPT;
import static com.vych.api.entities.MediaTypes.*;
import static com.vych.cache.AppCache.CACHED_IMAGE_LIFETIME_SEC;
import static com.vych.database.SettingsDefaults.REPOSITORY_IP;
import static com.vych.utils.FilesUtils.*;

/**
 * Represent game in repository.
 */
@Getter
@Setter
public class Game {
    private final RequestApi requestApi = RequestApi.getInstance();

    @Setter(AccessLevel.NONE)
    @JsonIgnore
    private String id;

    @Setter(AccessLevel.NONE)
    @JsonIgnore
    private String cover;

    private About about;
    private List<Rom> roms;

    public Game() {
    }

    public Game(String id) {
        this.id = id;
        updateInfo();
    }

    /**
     * Get {@link Rom} for this game by ROM path
     *
     * @param path ROM path
     * @return return ROM if presented, null otherwise
     */
    public Rom getRomByPath(String path) {
        for (Rom rom : roms) {
            if (rom.getPath().equals(path)) {
                return rom;
            }
        }
        return null;
    }

    /**
     * Check local existence of ROM for game
     *
     * @param rom rom to check
     * @return true if existed, false otherwise
     */
    public boolean isRomDownloaded(Rom rom) {
        if (!isAnyRomDownloaded()) {
            return false;
        }
        return isFileExist(buildPathString(ROMS_PATH, id, rom.getPath()));
    }

    /**
     * Check local existence of at least one ROM for game
     *
     * @return true if existed, false otherwise
     */
    public boolean isAnyRomDownloaded() {
        return isFileExist(buildPathString(ROMS_PATH, id));
    }

    /**
     * Download copy of ROM for game
     */
    public void downloadRom(Rom rom) {
        if (!isAnyRomDownloaded()) {
            new File(buildPathString(ROMS_PATH, id)).mkdirs();
        }

        RequestTemplate rqTemplate = requestApi.getRequestBuilder()
                .setPath(
                        AppDatabase.getSettings().get(REPOSITORY_IP.getName()) +
                                "/games/rom/" + id + "/" + rom.getPath().replace(" ", "%20")
                )
                .addHeader(ACCEPT, OCTET_STREAM)
                .build();
        Response rs = requestApi.execute(rqTemplate);

        rs.saveAsFile(buildPathString(ROMS_PATH, id, rom.getPath()));
    }

    /**
     * Request information about game from repository if needed.
     * Update instance fields if needed.
     *
     * @return updated instance
     */
    @SneakyThrows
    public Game updateInfo() {
        String repoIp = AppDatabase.getSettings().get(REPOSITORY_IP.getName());

        // request game information
        RequestTemplate rqTemplate = requestApi.getRequestBuilder()
                .setPath(repoIp + "/games/info/" + id)
                .addHeader(ACCEPT, JSON)
                .build();
        Response rs = requestApi.execute(rqTemplate);

        Game updatedGame = rs.getParsed(this.getClass());
        about = updatedGame.getAbout();
        roms = updatedGame.getRoms();

        // request game cover art
        String coverUrl = repoIp + "/games/cover/" + id;
        String cached = AppCache.getCachedImage(coverUrl);
        if (cached == null) {
            rqTemplate = requestApi.getRequestBuilder()
                    .setPath(repoIp + "/games/cover/" + id)
                    .addHeader(ACCEPT, IMAGE_PNG)
                    .build();
            rs = requestApi.execute(rqTemplate);

            String coverPath = buildPathString(CACHE_IMAGES_PATH, id + ".png");
            rs.saveAsFile(coverPath);
            AppCache.cacheImage(coverUrl, coverPath, CACHED_IMAGE_LIFETIME_SEC);
            cover = coverPath;
        } else {
            cover = cached;
        }

        return this;
    }
}
