package com.thales.github.ui.listener;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public abstract class EndlessRecyclerOnScrollListener extends RecyclerView.OnScrollListener {
    private static final String BUNDLE_CURRENT_PAGE = "current-page";

    // The total number of items in the dataset after the last load
    private int previousTotal = 0;

    // True if we are still waiting for the last set of data to load.
    private boolean loading = true;

    // The minimum amount of items to have below your current scroll position before loading more.
    private final int visibleThreshold = 5;
    private int firstVisibleItem;
    private int visibleItemCount;
    private int totalItemCount;

    private int currentPage = 1;

    private final LinearLayoutManager linearLayoutManager;

    public EndlessRecyclerOnScrollListener(LinearLayoutManager linearLayoutManager) {
        this.linearLayoutManager = linearLayoutManager;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState.containsKey(BUNDLE_CURRENT_PAGE)) {
            currentPage = savedInstanceState.getInt(BUNDLE_CURRENT_PAGE);
        }
    }

    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(BUNDLE_CURRENT_PAGE, currentPage);
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        visibleItemCount = recyclerView.getChildCount();
        totalItemCount = linearLayoutManager.getItemCount();
        firstVisibleItem = linearLayoutManager.findFirstVisibleItemPosition();

        if (loading && totalItemCount > previousTotal) {
            loading = false;
            previousTotal = totalItemCount;

        }
        if (!loading && (totalItemCount - visibleItemCount)
                <= (firstVisibleItem + visibleThreshold)) {
            // End has been reached

            // Do something
            currentPage++;

            onLoadMore(currentPage);

            loading = true;
        }
    }

    public abstract void onLoadMore(int currentPage);
}
