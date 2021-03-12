package com.example.booksearchingapp.data

import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.booksearchingapp.api.BaseResponse
import com.example.booksearchingapp.api.BookService
import com.example.booksearchingapp.books.BookViewData
import javax.inject.Inject
import javax.inject.Singleton


typealias ResultData<T> = BaseResponse<T>

@Singleton
class BookRepository @Inject constructor(private val service: BookService) {

    companion object {
        const val BOOKS_PAGE_SIZE = 5
        const val BOOKS_INIT_LOAD_SIZE = 50
        const val PREFETCH_PAGE_SIZE = 3

    }
    suspend fun searchBooks(searchQuery: String): ResultData<List<Book>> {

//        val config = PagedList.Config.Builder()
//            .setPageSize(BOOKS_PAGE_SIZE)
//            .setInitialLoadSizeHint(BOOKS_INIT_LOAD_SIZE)
//            .setPrefetchDistance(PREFETCH_PAGE_SIZE)
//            .setEnablePlaceholders(true)
//            .build()
//
//        val pagedLiveData = LivePagedListBuilder(object: DataSource.Factory<Int, BookViewData>() {
//            override fun create(): DataSource<Int, BookViewData> {
//                return BookPagedDataSource(service)
//            }
//        }, config).build()

        val result = service.searchBooks(target = "title", page = 1, size = 50, searchQuery)
        return when (result) {
            is BaseResponse.Success -> {
                BaseResponse.Success(result.data.list)
            }
            is BaseResponse.ApiError -> {
                BaseResponse.ApiError(result.errorMessage)
            }
            is BaseResponse.NetworkError -> {
                BaseResponse.NetworkError(result.error)
            }
            is BaseResponse.UnknownError -> {
                BaseResponse.UnknownError(result.error)
            }
            is BaseResponse.Loading -> {
                BaseResponse.Loading
            }
        }
    }
}