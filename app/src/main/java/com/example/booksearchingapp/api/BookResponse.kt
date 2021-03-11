package com.example.booksearchingapp.api

import com.example.booksearchingapp.data.Book
import com.google.gson.annotations.SerializedName

data class BookResponse(
    @field:SerializedName("documents") val list: List<Book>
)
