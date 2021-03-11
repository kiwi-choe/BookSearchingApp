package com.example.booksearchingapp.books

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.example.booksearchingapp.data.Book
import com.example.booksearchingapp.data.BookRepository
import kotlinx.coroutines.launch

class BooksViewModel @ViewModelInject constructor(
    private val repository: BookRepository
) : ViewModel() {

    private val _books = MutableLiveData<List<Book>>()
    val books: LiveData<List<Book>> = _books

    val searchingQuery = MutableLiveData<String>()

    private val _snackbarMessage = MutableLiveData<Int>()
    val snackbarMessage: LiveData<Int> = _snackbarMessage

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
            _books.value = repository.searchBooks(searchQuery)
        }
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