package com.vych.ui.scene;

import javafx.application.Platform;
import lombok.SneakyThrows;

/**
 * Base controller for FXML scenes.
 * Implement background tasking
 */
public abstract class BaseController {
    /**
     * Execute {@link BackgroundTask}
     *
     * @param task task to execute
     * @param <T>  Type that produced by task
     */
    @SneakyThrows
    protected <T> void runBackground(BackgroundTask<T> task) {
        new Thread(() -> {
            T result = task.doInBackground();
            Platform.runLater(() -> task.fxAfter(result));
        }).start();
    }
}
