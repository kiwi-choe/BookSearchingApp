package com.example.booksearchingapp.data

import com.example.booksearchingapp.api.BookService
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class BookRepository @Inject constructor(private val service: BookService) {

    suspend fun searchBooks(searchQuery: String): List<Book> {
        val result = service.searchBooks(target = "title", page = 1, size = 50, searchQuery)
        return if (result.isSuccessful) {
            result.body()?.list ?: emptyList()
        } else {
            emptyList()
        }

//        return when (result) {
//            is Result.Success -> {
//                result.data.mapTo(filteredResult) { it }
//            }
//            else -> {
//                emptyList()
//            }
//        }
    }

}