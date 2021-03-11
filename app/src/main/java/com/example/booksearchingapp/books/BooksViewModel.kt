package com.example.booksearchingapp.books

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.booksearchingapp.R
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

    fun onTextChanged() {
        searchBooks()
    }

    fun onSearchButtonClicked() {
        searchBooks()
    }

    private fun searchBooks() {
        if (validateSearchingQuery(searchingQuery.value)) {
            searchBooks(searchingQuery.value!!)
        } else {
            _snackbarMessage.value = R.string.please_input_text
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

}