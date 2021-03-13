package com.example.booksearchingapp.books

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.example.booksearchingapp.api.BaseResponse
import com.example.booksearchingapp.data.Book
import com.example.booksearchingapp.data.BookRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BooksViewModel @ViewModelInject constructor(
    private val repository: BookRepository
) : ViewModel() {

    val searchingQuery = MutableLiveData<String>()

    private val _snackbarMessage = MutableLiveData<String>()
    val snackbarMessage: LiveData<String> = _snackbarMessage

    private val _navigateToBookDetail = MutableLiveData<Unit>()
    val navigateToBookDetail: LiveData<Unit> = _navigateToBookDetail

    private val searchingQueryEvent = MutableLiveData<String>()
    private val test: LiveData<BaseResponse<List<Book>>> = searchingQueryEvent.switchMap { query ->
        liveData {
            if (validateSearchingQuery(query).not()) {
                _emptySearchingQueryState.value = true
                return@liveData
            } else {
                _emptySearchingQueryState.value = false
            }
            if (isNewQuery(query)) {
                val res =
                    repository.getSearchedBookResultsLiveData(query).asLiveData(Dispatchers.Main)
                emitSource(res)
            }
        }
    }

    val bookResults: LiveData<List<BookViewData>> = Transformations.map(test) { result ->
        when (result) {
            is BaseResponse.Success -> {
                Log.d("BooksViewModel", "result ${result.toString()}")
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

    val empty: LiveData<Boolean> = Transformations.map(bookResults) {
        it.isEmpty()
    }

    private var preQuery = ""

    fun onTextChanged() {
        searchingQueryEvent.value = searchingQuery.value
    }

    fun onSearchButtonClicked() {
        searchingQueryEvent.value = searchingQuery.value
    }

    fun onItemClicked() {
        _navigateToBookDetail.value = Unit
    }

    private fun convertToViewData(it: Book): BookViewData {
        return BookViewData(
            it.title,
            it.contents,
            it.isbn,
            it.datetime,
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
}