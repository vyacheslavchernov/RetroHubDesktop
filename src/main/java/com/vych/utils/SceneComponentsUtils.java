package com.vych.utils;

import javafx.scene.control.ListView;
import lombok.experimental.UtilityClass;

/**
 * Utility methods for work with JavaFX scene components
 */
@UtilityClass
public class SceneComponentsUtils {
    /**
     * Get selected index of provided {@link ListView}
     *
     * @param listView target
     * @return index of selected item in target listView
     */
    public static int getListViewSelectedIndex(ListView<?> listView) {
        return listView.getSelectionModel().getSelectedIndex();
    }

    /**
     * Get selected item of provided {@link ListView}
     *
     * @param listView target
     * @return selected item in target listView
     */
    public static <T> T getListViewSelectedItem(ListView<T> listView) {
        return listView.getSelectionModel().getSelectedItem();
    }
}
