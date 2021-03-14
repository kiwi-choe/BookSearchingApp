package com.example.booksearchingapp.books

import android.util.Log
import androidx.lifecycle.*
import com.example.booksearchingapp.Event
import com.example.booksearchingapp.api.BaseResponse
import com.example.booksearchingapp.data.Book
import com.example.booksearchingapp.data.Book.Companion.splitDateTime
import com.example.booksearchingapp.data.BookRepository
import com.example.booksearchingapp.data.ResultData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BooksViewModel(
    private val repository: BookRepository
) : ViewModel() {

    val searchingQuery = MutableLiveData<String>()

    private val _snackbarMessage = MutableLiveData<String>()
    val snackbarMessage: LiveData<String> = _snackbarMessage

    private val _navigateToBookDetail = MutableLiveData<Event<String>>()
    val navigateToBookDetail: LiveData<Event<String>> = _navigateToBookDetail

    private val searchingQueryEvent = MutableLiveData<String>()
    private val validateAndSearchQuery: LiveData<BaseResponse<List<Book>>> =
        searchingQueryEvent.switchMap { query ->
            liveData {
                if (validateSearchingQuery(query).not()) {
                    clearPreQuery()
                    _emptySearchingQueryState.value = true
                    return@liveData
                } else {
                    _emptySearchingQueryState.value = false
                }
                if (isNewQuery(query)) {
                    val res =
                        repository.getSearchedBookResultsLiveData(query)
                            .asLiveData(Dispatchers.Main)
                    emitSource(res)
                }
            }
        }


    val bookPreviewResults: LiveData<List<BookPreviewData>> =
        Transformations.map(validateAndSearchQuery) { result ->
            when (result) {
                is BaseResponse.Success -> {
                    result.data.map { convertToViewData(it) }
                }
                else -> {
                    _snackbarMessage.value = result.toString()
                    emptyList()
                }
            }
        }

    private val _emptySearchingQueryState = MutableLiveData(false)
    val emptySearchingQueryState: LiveData<Boolean> = _emptySearchingQueryState

    val empty: LiveData<Boolean> = Transformations.map(bookPreviewResults) {
        it.isEmpty()
    }

    private var preQuery = ""

    private fun clearPreQuery() {
        preQuery = ""
    }

    private val loadBookDetailEvent = MutableLiveData<String>()
    val bookDetail: LiveData<Book> = loadBookDetailEvent.switchMap { bookId ->
        liveData {
            val res = repository.getBookDetail(bookId)
            when (res) {
                is ResultData.Success -> {
                    emit(res.data)
                }
                is ResultData.Error -> {
                    _snackbarMessage.value = res.errorMessage
                }
            }
        }
    }

    private val _updateLikedBook = MutableLiveData<Event<Int>>()
    val updateLikedBook: LiveData<Event<Int>> = _updateLikedBook
    fun likeBook(bookId: String) {
        _updateLikedBook.value = Event(repository.updateBookLiked(bookId))
    }

    fun onTextChanged() {
        searchingQueryEvent.value = searchingQuery.value
    }

    fun onSearchButtonClicked() {
        searchingQueryEvent.value = searchingQuery.value
    }

    fun onItemClicked(bookId: String) {
        _navigateToBookDetail.value = Event(bookId)
    }

    private fun convertToViewData(it: Book): BookPreviewData {
        return BookPreviewData(
            it.title,
            it.contents,
            it.isbn,
            splitDateTime(it.datetime),
            it.sale_price.toString(),
            it.publisher,
            it.thumbnail
        )
    }

    private fun validateSearchingQuery(query: String?): Boolean {
        return query?.isNotEmpty() == true
    }

    private fun isNewQuery(query: String): Boolean {
        if (query != preQuery) {
            preQuery = query
            return true
        }
        return false
    }

    fun listScrolled() {
        val immutableQuery = searchingQuery.value
        if (immutableQuery != null) {
            viewModelScope.launch {
                repository.loadBooksMore(immutableQuery)
            }
        }
    }

    fun getBookDetail(bookId: String) {
        loadBookDetailEvent.value = bookId
    }
}