package com.example.booksearchingapp.books

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

abstract class EndlessRecyclerViewScrollListener(private val layoutManager: RecyclerView.LayoutManager) :
    RecyclerView.OnScrollListener() {

    // The minimum amount of items to have below your current scroll position
    // before loading more.
    private val visibleThreshold = 5

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

    // Defines the process for actually loading more data based on page
    abstract fun onLoadMore()
}