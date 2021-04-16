package com.example.booksearchingapp.books

import androidx.lifecycle.*
import com.example.booksearchingapp.Event
import com.example.booksearchingapp.api.BaseResponse
import com.example.booksearchingapp.data.Book
import com.example.booksearchingapp.data.Book.Companion.splitDateTime
import com.example.booksearchingapp.data.BookRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
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
    val bookPreviewResults: LiveData<List<BookPreviewData>> =
        searchingQueryEvent.switchMap { query ->
            liveData {
                _emptySearchingQueryState.value = validateSearchingQuery(query).not()

                if (validateSearchingQuery(query).and(isNewQuery(query))) {
                    val res = repository.getSearchedBookResultsLiveData(query)
                        .map { result ->
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
                        .asLiveData(Dispatchers.Main)
                    emitSource(res)
                }

                initPreQuery(query)
            }
        }

    private val _emptySearchingQueryState = MutableLiveData(false)
    val emptySearchingQueryState: LiveData<Boolean> = _emptySearchingQueryState

    val empty: LiveData<Boolean> = Transformations.map(bookPreviewResults) {
        it.isEmpty()
    }

    private var preQuery = ""
    private fun initPreQuery(query: String?) {
        preQuery = query ?: ""
    }

    val updatedPositionOfBookLiked: LiveData<Event<Int>> =
        repository.bookLikedPosition.map { Event(it) }.asLiveData(Dispatchers.Main)

    fun onTextChanged() {
        searchingQueryEvent.value = searchingQuery.value
    }

    fun onSearchButtonClicked() {
        searchingQueryEvent.value = searchingQuery.value
    }

    private fun validateSearchingQuery(query: String?): Boolean {
        return query?.isNotBlank() == true
    }

    private fun isNewQuery(query: String): Boolean {
        return query != preQuery
    }

    fun listScrolled() {
        val immutableQuery = searchingQuery.value
        if (immutableQuery != null) {
            viewModelScope.launch {
                repository.loadBooksMore(immutableQuery)
            }
        }
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

    fun onItemClicked(bookId: String) {
        _navigateToBookDetail.value = Event(bookId)
    }

}