package com.vych.ui.stage;

import com.vych.ui.scene.wrappers.SceneWrapper;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;

/**
 * Manager for scenes.
 * Handle showing and closing them
 */
public class StageManager {
    private static final Map<String, StageWrapper> registeredStages = new HashMap<>();

    /**
     * Create {@link javafx.stage.Stage} for scene and add it to SceneManager list
     * <p>
     * If scene have {@link SceneWrapper#isCanBeMany()} true,
     * then just focus on already existed one
     *
     * @param sceneWrapper scene to add
     * @return stage key
     */
    public static String add(SceneWrapper sceneWrapper) {
        if (registeredStages.containsKey(sceneWrapper.getTitle()) && !sceneWrapper.isCanBeMany()) {
            registeredStages.get(sceneWrapper.getTitle()).focus();
            return sceneWrapper.getTitle();
        }

        registeredStages.put(sceneWrapper.getTitle(), new StageWrapper(sceneWrapper));
        return sceneWrapper.getTitle();
    }

    /**
     * Call {@link Stage#show()} for stage from SceneManager list
     *
     * @param stageTitle stage key
     */
    public static void show(String stageTitle) {
        registeredStages.get(stageTitle).show();
    }

    /**
     * Call {@link Stage#close()} for stage from SceneManager list
     *
     * @param stageTitle stage key
     */
    public static void close(String stageTitle) {
        registeredStages.get(stageTitle).close();
    }

    /**
     * Delete stage from SceneManager list by it key
     *
     * @param stageTitle stage key
     */
    public static void delete(String stageTitle) {
        registeredStages.remove(stageTitle);
    }

    /**
     * Close all existed stages. Same as close app
     */
    public static void closeAll() {
        for (StageWrapper stageWrapper : registeredStages.values()) {
            stageWrapper.close();
        }
    }

}
