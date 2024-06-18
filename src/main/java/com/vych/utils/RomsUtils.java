package com.vych.utils;

import com.vych.api.entities.RomInfo;
import lombok.experimental.UtilityClass;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static com.vych.utils.FilesUtils.ROMS_PATH;
import static com.vych.utils.FilesUtils.buildPathString;

/**
 * Utility methods for work with ROMs
 */
@UtilityClass
public class RomsUtils {
    /**
     * Check existence of ROM local copy for game
     *
     * @param gameTitle game directory path
     * @param romPath   ROM file name
     * @return true if ROM exist, false otherwise
     */
    public static boolean checkIsRomExist(String gameTitle, String romPath) {
        Path path = Paths.get(buildPathString(ROMS_PATH, gameTitle));
        if (!Files.exists(path)) {
            return false;
        }

        return Files.exists(
                Paths.get(buildPathString(ROMS_PATH, gameTitle, romPath))
        );

    }

    /**
     * Check existence of at least one ROM local copy for game.
     *
     * @param gameTitle game directory path
     * @return true if existed, false otherwise
     */
    public static boolean checkIsAnyRomExist(String gameTitle) {
        Path path = Paths.get(buildPathString(ROMS_PATH, gameTitle));
        return Files.exists(path);
    }

    /**
     * Search rom information in list of objects with information.
     *
     * @param path        rom file name
     * @param romInfoList list of objects with information about ROMs to search in
     * @return information about ROM, if it was founded, null otherwise
     */
    public static RomInfo getRomInfoByPath(String path, List<RomInfo> romInfoList) {
        for (RomInfo rom : romInfoList) {
            if (rom.getPath().equals(path)) {
                return rom;
            }
        }
        return null;
    }
}
