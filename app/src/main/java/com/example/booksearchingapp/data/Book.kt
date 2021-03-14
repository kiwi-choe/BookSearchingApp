package com.example.booksearchingapp.data

import com.google.gson.annotations.SerializedName
import java.util.concurrent.atomic.AtomicBoolean

data class Book(
    @field:SerializedName("title") val title: String,
    @field:SerializedName("contents") val contents: String,
    @field:SerializedName("isbn") val isbn: String,
    @field:SerializedName("datetime") val datetime: String,
    @field:SerializedName("sale_price") val sale_price: Int,
    @field:SerializedName("publisher") val publisher: String,
    @field:SerializedName("thumbnail") val thumbnail: String,
    @field:SerializedName("status") val status: String,
    var liked: Boolean = false
)