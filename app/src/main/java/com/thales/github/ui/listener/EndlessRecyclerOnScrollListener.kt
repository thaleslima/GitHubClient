package com.thales.github.ui.listener

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView

abstract class EndlessRecyclerOnScrollListener(private val linearLayoutManager: LinearLayoutManager) : RecyclerView.OnScrollListener() {

    // The total number of items in the dataset after the last load
    private var previousTotal = 0

    // True if we are still waiting for the last set of data to load.
    private var loading = true

    // The minimum amount of items to have below your current scroll position before loading more.
    private val visibleThreshold = 5
    private var firstVisibleItem: Int = 0
    private var visibleItemCount: Int = 0
    private var totalItemCount: Int = 0

    private var currentPage = 1

    fun setCurrentPage(currentPage: Int) {
        this.currentPage = currentPage
    }

    fun onRestoreInstanceState(savedInstanceState: Bundle) {
        if (savedInstanceState.containsKey(BUNDLE_CURRENT_PAGE)) {
            currentPage = savedInstanceState.getInt(BUNDLE_CURRENT_PAGE)
        }
    }

    fun onSaveInstanceState(outState: Bundle) {
        outState.putInt(BUNDLE_CURRENT_PAGE, currentPage)
    }

    override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        visibleItemCount = recyclerView!!.childCount
        totalItemCount = linearLayoutManager.itemCount
        firstVisibleItem = linearLayoutManager.findFirstVisibleItemPosition()

        if (loading && totalItemCount > previousTotal) {
            loading = false
            previousTotal = totalItemCount

        }
        if (!loading && totalItemCount - visibleItemCount <= firstVisibleItem + visibleThreshold) {
            // End has been reached

            // Do something
            currentPage++

            onLoadMore(currentPage)

            loading = true
        }
    }

    abstract fun onLoadMore(currentPage: Int)

    companion object {
        private val BUNDLE_CURRENT_PAGE = "current-page"
    }
}
