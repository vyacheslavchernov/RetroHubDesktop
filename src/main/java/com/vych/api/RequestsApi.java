package com.vych.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vych.api.entities.Title;
import com.vych.api.entities.TitleFullInfo;
import com.vych.cache.AppCache;
import com.vych.database.AppDatabase;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static com.vych.database.SettingsDefaults.REPOSITORY_IP;
import static com.vych.utils.FilesUtils.*;

/**
 * Provide access to dedicated repository with information about games, ROMs, emulators
 */
// TODO: Subject to full rework
public class RequestsApi {

    public final static long CACHED_REQUEST_LIFETIME_SEC = 300;

    /**
     * Get short information about ({@link Title}) all games existed in repository
     *
     * @return List of shot information about games
     * @throws IOException ..
     */
    public static List<Title> getAllTitles() throws IOException {
        String titlesJson = getJsonRequest(
                AppDatabase.getSettings().get(REPOSITORY_IP.getName()) + "/games/get_all_titles"
        );
        return new ObjectMapper().readValue(titlesJson, new TypeReference<List<Title>>() {
        });
    }

    /**
     * Get full information about ({@link TitleFullInfo}) target game.
     *
     * @param title target
     * @return Full information about target game
     * @throws IOException ..
     */
    public static TitleFullInfo getTitleFullInfo(String title) throws IOException {
        String titlesJson = titlesJson = getJsonRequest(
                AppDatabase.getSettings().get(REPOSITORY_IP.getName()) + "/games/info/" + title
        );
        return new ObjectMapper().readValue(titlesJson, TitleFullInfo.class);
    }

    /**
     * Get cover art path for game.
     *
     * @param title target
     * @return game cover art path
     * @throws IOException ..
     */
    public static String getTitleCover(String title) throws IOException {
        String saveFilePath = buildPathString(CACHE_COVERS_PATH, title + ".png");

        // TODO: Cache covers with AppCache
        File f = new File(saveFilePath);
        if (f.exists()) {
            return saveFilePath;
        }

        HttpURLConnection con = getRawRequest(
                AppDatabase.getSettings().get(REPOSITORY_IP.getName()) + "/games/cover/" + title
        );

        InputStream inStream = con.getInputStream();
        FileOutputStream outputStream = new FileOutputStream(saveFilePath);

        int bytesRead = -1;
        byte[] buffer = new byte[4096];
        while ((bytesRead = inStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }

        outputStream.close();
        inStream.close();
        con.disconnect();

        return saveFilePath;
    }

    /**
     * Get ROM from repository
     *
     * @param title   target game
     * @param romPath target ROM
     * @throws IOException ..
     */
    public static void getRom(String title, String romPath) throws IOException {
        if (!Files.exists(Paths.get(buildPathString(ROMS_PATH, title)))) {
            new File(buildPathString(ROMS_PATH, title)).mkdirs();
        }

        String saveFilePath = buildPathString(ROMS_PATH, title, romPath);
        File f = new File(saveFilePath);

        HttpURLConnection con = getRawRequest(
                AppDatabase.getSettings().get(REPOSITORY_IP.getName()) +
                        "/games/rom/" + title + "/" + romPath.replace(" ", "%20")
        );

        InputStream inStream = con.getInputStream();
        FileOutputStream outputStream = new FileOutputStream(saveFilePath);

        int bytesRead = -1;
        byte[] buffer = new byte[4096];
        while ((bytesRead = inStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }

        outputStream.close();
        inStream.close();
        con.disconnect();
    }

    /**
     * Send request and parse JSON response
     *
     * @param path request URL
     * @return Json content of response
     * @throws IOException ..
     */
    private static String getJsonRequest(String path) throws IOException {
        String cached = AppCache.getString(path);
        if (cached != null) {
            return cached;
        }

        URL url = new URL(path);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuilder content = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();
        con.disconnect();

        AppCache.cacheString(path, content.toString(), CACHED_REQUEST_LIFETIME_SEC);
        return content.toString();
    }

    /**
     * Build connection for request, return not sent
     *
     * @param path request URL
     * @return built connection
     * @throws IOException ..
     */
    private static HttpURLConnection getRawRequest(String path) throws IOException {
        URL url = new URL(path);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        return con;
    }
}
