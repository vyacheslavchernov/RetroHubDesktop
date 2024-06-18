package com.vych;

import com.vych.utils.FilesUtils;

/**
 * Launcher for app.
 * <p>
 * Check file structure of program, create it, if needed.
 * Run app {@link RetroHubApp}
 */
public class AppLauncher {
    public static void main(String[] args) {
        // TODO: check return of method on existence of errors. Don't start if there is any error.
        FilesUtils.validateDirectoriesStructure();
        RetroHubApp.main(args);
    }
}
