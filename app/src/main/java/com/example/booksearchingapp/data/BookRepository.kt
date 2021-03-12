package com.example.booksearchingapp.data

import com.example.booksearchingapp.api.BaseResponse
import com.example.booksearchingapp.api.BookService
import javax.inject.Inject
import javax.inject.Singleton


typealias ResultData<T> = BaseResponse<T>

@Singleton
class BookRepository @Inject constructor(private val service: BookService) {

    suspend fun searchBooks(searchQuery: String, page: Int): ResultData<List<Book>> {
        val result = service.searchBooks(target = "title", page, size = 50, searchQuery)
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