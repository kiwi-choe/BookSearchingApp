package com.example.booksearchingapp.books

data class BookViewData(
    val title: String,
    val contents: String,
    val isbn: String,
    val releaseDate: String,
    val price: String,
    val publisher: String,
    val thumbnail: String
)