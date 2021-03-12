package com.example.booksearchingapp.data

import androidx.paging.PageKeyedDataSource
import com.example.booksearchingapp.api.BookService
import com.example.booksearchingapp.books.BookViewData

class BookPagedDataSource(private val api: BookService) : PageKeyedDataSource<Int, BookViewData>() {
    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, BookViewData>
    ) {

    }


    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, BookViewData>) {

    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, BookViewData>) {}
}