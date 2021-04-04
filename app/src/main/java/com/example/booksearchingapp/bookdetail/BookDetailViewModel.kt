package com.example.booksearchingapp.bookdetail

import androidx.lifecycle.*
import com.example.booksearchingapp.data.Book
import com.example.booksearchingapp.data.BookRepository
import com.example.booksearchingapp.data.ResultData
import kotlinx.coroutines.launch

class BookDetailViewModel(
    private val repository: BookRepository
) : ViewModel() {

    private val loadBookDetailEvent = MutableLiveData<String>()

    private val _snackbarMessage = MutableLiveData<String>()
    val snackbarMessage: LiveData<String> = _snackbarMessage

    val bookDetail: LiveData<Book> = loadBookDetailEvent.switchMap { bookId ->
        liveData {
            when (val res = repository.getBookDetail(bookId)) {
                is ResultData.Success -> {
                    emit(res.data)
                }
                is ResultData.Error -> {
                    _snackbarMessage.value = res.errorMessage
                }
            }
        }
    }

    fun getBookDetail(bookId: String) {
        loadBookDetailEvent.value = bookId
    }

    fun likeBook(bookId: String) {
        viewModelScope.launch {
            repository.updateBookLiked(bookId)
        }
    }
}