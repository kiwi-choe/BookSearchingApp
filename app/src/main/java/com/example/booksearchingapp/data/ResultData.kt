package com.example.booksearchingapp.data

sealed class ResultData<out T : Any> {
    data class Success<T : Any>(val data: T) : ResultData<T>()
    data class Error(val errorMessage: String) : ResultData<Nothing>()
}