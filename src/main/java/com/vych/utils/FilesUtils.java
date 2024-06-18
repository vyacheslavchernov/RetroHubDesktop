package com.vych.utils;

import javafx.scene.control.Alert;
import lombok.experimental.UtilityClass;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility methods for work with files.
 */
@UtilityClass
public class FilesUtils {
    public static final String ROMS_PATH = "roms";
    public static final String EMULATORS_PATH = "emulators";
    public static final String CACHE_ROOT_PATH = "cache";
    public static final String CACHE_COVERS_PATH = buildPathString("cache", "covers");

    /**
     * Check file structure of app.
     * If any of not optional directories not exist, make it.
     *
     * @return List of errors if any was occurred
     */
    // TODO: check return of method on existence of errors. Don't start if there any error.
    @SuppressWarnings("UnusedReturnValue")
    public static List<String> validateDirectoriesStructure() {
        List<String> collectedFails = new ArrayList<>();
        List<String> pathsToValidate = List.of(ROMS_PATH, EMULATORS_PATH, CACHE_ROOT_PATH, CACHE_COVERS_PATH);

        pathsToValidate.forEach(path -> {
            if (!Files.exists(Paths.get(path))) {
                if (!new File(path).mkdirs()) {
                    collectedFails.add("Attempt to make direction for ROMs failed. Path " + path);
                }
            }
        });

        return collectedFails;
    }

    /**
     * Try to delete file. Show alert if it was failed.
     *
     * @param path Path to file
     */
    public static void deleteFile(String path) {
        File romFile = new File(path);

        if (!romFile.delete()) {
            // TODO: maybe some sort of builder for alerts?
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Can't delete file");
            alert.setContentText(String.format(
                    "Attempt to deleting file located in \"%s\" was failed",
                    path
            ));
            alert.show();
        }
    }

    /**
     * Build path with specific for OS separators
     *
     * @param args Directories or filenames to build path with
     * @return Built path
     */
    public static String buildPathString(String... args) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < args.length; i++) {
            sb.append(args[i]);
            if (i != args.length - 1) {
                sb.append(File.separator);
            }
        }
        return sb.toString();
    }
}
