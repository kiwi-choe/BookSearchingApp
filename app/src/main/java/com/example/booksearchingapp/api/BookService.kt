package com.example.booksearchingapp.api

import retrofit2.http.GET
import retrofit2.http.Query

interface BookService {
    @GET("/v3/search/book")
    suspend fun searchBooks(
        @Query("target") target: String = "title",
        @Query("page") page: Int,
        @Query("size") size: Int = 50,
        @Query("query") query: String
    ): BaseResponse<BookResponse>
}