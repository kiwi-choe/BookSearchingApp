package com.example.booksearchingapp.data

import com.example.booksearchingapp.api.BaseResponse
import com.example.booksearchingapp.api.BookService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

class BookRepository(private val service: BookService) {

    companion object {
        private const val FIRST_PAGE_INDEX = 1
        private const val SIZE_PER_PAGE = 50
        private const val SEARCH_BY_TARGET = "title"
    }

    private val bookResults = MutableSharedFlow<BaseResponse<List<Book>>>(replay = 1)

    private var isEndPage = false

    // keep the last requested page. When the request is successful, increment the page number.
    private var lastPage = FIRST_PAGE_INDEX

    private val inMemoryCache = mutableListOf<Book>()

    // avoid triggering multiple requests in the same time
    private var isRequestInProgress = false

    suspend fun getSearchedBookResultsLiveData(searchQuery: String): Flow<BaseResponse<List<Book>>> {
        lastPage = FIRST_PAGE_INDEX
        inMemoryCache.clear()
        requestSearchBooks(searchQuery)
        return bookResults
    }

    private suspend fun requestSearchBooks(searchQuery: String): Boolean {
        isRequestInProgress = true
        var success = false

        val result = service.searchBooks(SEARCH_BY_TARGET, lastPage, SIZE_PER_PAGE, searchQuery)
        when (result) {
            is BaseResponse.Success -> {
                isEndPage = result.data.meta.isEndPage
                val books = result.data.list ?: emptyList()
                inMemoryCache.addAll(books)
                // search something from in cache
                bookResults.emit(BaseResponse.Success(books))
                success = true
            }
            is BaseResponse.ApiError -> {
                bookResults.emit(BaseResponse.ApiError(result.errorMessage))
            }
            is BaseResponse.NetworkError -> {
                bookResults.emit(BaseResponse.NetworkError(result.error))
            }
            is BaseResponse.UnknownError -> {
                bookResults.emit(BaseResponse.UnknownError(result.error))
            }
            is BaseResponse.Loading -> {
                bookResults.emit(BaseResponse.Loading)
            }
        }
        isRequestInProgress = false
        return success
    }

//    private fun booksByStringFromInMemory(): List<Book> {
//        // from the in memory cache select only the repos whose name or description matches
//        // the query. Then order the results.
//        return inMemoryCache.filter {
//            Log.d("Repo", it.toString())
//            it.toString().contains(query, true)}
//    }

    suspend fun loadBooksMore(searchQuery: String) {
        if (isRequestInProgress) {
            return
        }

        if (isEndPage) {
            return
        }

        val success = requestSearchBooks(searchQuery)
        if (success) {
            lastPage++
        }
    }

    fun getBookDetail(isbn: String): ResultData<Book> {
        val book = inMemoryCache.find { it.isbn == isbn }
        return book?.let {
            ResultData.Success(book)
        } ?: ResultData.Error("")
    }

    fun updateBookLiked(bookId: String): Int {
        return updateBookLikedInMemory(bookId)
    }

    private fun updateBookLikedInMemory(isbn: String): Int {
        var updatedBookPosition = 0
        inMemoryCache.find { it.isbn == isbn }?.also { book ->
            book.liked = true
            updatedBookPosition = inMemoryCache.indexOf(book)
        }
        return updatedBookPosition
    }
}

sealed class ResultData<out T : Any> {
    data class Success<T : Any>(val data: T) : ResultData<T>()
    data class Error(val errorMessage: String) : ResultData<Nothing>()
}