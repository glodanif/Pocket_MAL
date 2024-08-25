package com.g.pocketmal.ui.utils;

import android.widget.AbsListView;

public abstract class LazyLoadOnScrollListener implements AbsListView.OnScrollListener {

    private int bunchSize;
    private int startLoadOffset;

    private boolean isLoading, isLastCallFailed;
    private int loadedInLastBunch;

    private int loadedCount;

    public LazyLoadOnScrollListener(int bunchSize) {
        this.bunchSize = bunchSize;
    }

    public LazyLoadOnScrollListener(int bunchSize, int startLoadOffset) {
        this.bunchSize = bunchSize;
        this.startLoadOffset = startLoadOffset;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

        if (totalItemCount != 0 && loadedInLastBunch < bunchSize) {
            return;
        }

        boolean offsetReached = totalItemCount - (firstVisibleItem + visibleItemCount) <= startLoadOffset;

        if (totalItemCount > 0 && offsetReached && !isLoading && !isLastCallFailed) {
            loadedCount += bunchSize;
            isLoading = true;
            onLoad(loadedCount);
        }
    }

    /**
     * This method will be triggered when user reaches end of list / startLoadOffset
     * @param offset Number of last loaded item
     */
    public abstract void onLoad(int offset);

    /**
     * Call this method when getting new items was failed
     */
    public void notifyFail() {
        isLoading = false;
        isLastCallFailed = true;
    }

    /**
     * Call this method when getting new items was successful
     * @param lastBunchSize Size of last bunch to detect end of list
     */
    public void notifyFinish(int lastBunchSize) {
        isLoading = isLastCallFailed = false;
        loadedInLastBunch = lastBunchSize;
    }

    /**
     * Resets listener
     */
    public void reset() {
        loadedInLastBunch = loadedCount = 0;
        isLoading = isLastCallFailed = false;
    }

    /**
     * Returns count of loaded items
     * @return Count of loaded items
     */
    public int getLoadedCount() {
        return loadedCount;
    }
}