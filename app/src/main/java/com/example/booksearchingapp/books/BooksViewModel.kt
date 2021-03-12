package com.example.booksearchingapp.books

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.example.booksearchingapp.api.BaseResponse
import com.example.booksearchingapp.data.Book
import com.example.booksearchingapp.data.BookRepository
import kotlinx.coroutines.launch

class BooksViewModel @ViewModelInject constructor(
    private val repository: BookRepository
) : ViewModel() {

    private val _books = MutableLiveData<List<BookViewData>>()
    val books: LiveData<List<BookViewData>> = _books

    val searchingQuery = MutableLiveData<String>()

    private val _snackbarMessage = MutableLiveData<String>()
    val snackbarMessage: LiveData<String> = _snackbarMessage

    val empty: LiveData<Boolean> = Transformations.map(_books) {
        it.isEmpty()
    }

    private var preQuery = ""

    fun onTextChanged() {
        searchBooks()
    }

    fun onSearchButtonClicked() {
        searchBooks()
    }

    private fun searchBooks() {
        if (validateSearchingQuery(searchingQuery.value).not()) {
            _books.value = emptyList()
        } else {
            val query = searchingQuery.value!!
            if (isNewQuery(query)) {
                searchBooks(query)
            }
        }
    }

    private fun searchBooks(searchQuery: String) {
        viewModelScope.launch {
            repository.searchBooks(searchQuery).let { result ->
                _books.value = when (result) {
                    is BaseResponse.Success -> {
                        result.data.map { convertToViewData(it) }
                    }
                    else -> {
                        _snackbarMessage.value = result.toString()
                        emptyList()
                    }
                }
            }
        }
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

}