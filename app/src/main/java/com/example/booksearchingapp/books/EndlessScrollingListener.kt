package com.example.booksearchingapp.books

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

abstract class EndlessRecyclerViewScrollListener(private val layoutManager: RecyclerView.LayoutManager) :
    RecyclerView.OnScrollListener() {

    // The minimum amount of items to have below your current scroll position
    // before loading more.
    private val visibleThreshold = 5

    // The current offset index of data you have loaded
    private var currentPage = 0

    // The total number of items in the dataset after the last load
    private var previousTotalItemCount = 0

    // True if we are still waiting for the last set of data to load.
    private var loading = true

    // Sets the starting page index
    private val startingPageIndex = 0

    // This happens many times a second during a scroll, so be wary of the code you place here.
    // We are given a few useful parameters to help us work out if we need to load some more data,
    // but first we check if we are waiting for the previous load to finish.
    override fun onScrolled(view: RecyclerView, dx: Int, dy: Int) {
        val totalItemCount = layoutManager.itemCount
        val lastVisibleItemPosition =
            (layoutManager as LinearLayoutManager).findLastVisibleItemPosition()

        val visibleItemCount = layoutManager.childCount
        if (visibleItemCount + lastVisibleItemPosition + visibleThreshold >= totalItemCount) {
            onLoadMore()
        }
    }

    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        super.onScrollStateChanged(recyclerView, newState)
    }

    // Defines the process for actually loading more data based on page
    abstract fun onLoadMore()
}