package com.vych.ui.scene;

/**
 * Background task that can be run by {@link BaseController#runBackground(BackgroundTask)} in non FX thread.
 * Results that was produced by task than can be used in FX thread.
 *
 * @param <T> Type that produced by task
 */
public interface BackgroundTask<T> {
    /**
     * Code that will be executed in non FX thread
     *
     * @return result of operations
     */
    T doInBackground();

    /**
     * Code that will be executed in FX thread
     *
     * @param bgResult result of operations in non FX thread
     */
    void fxAfter(T bgResult);
}
