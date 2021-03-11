package com.example.booksearchingapp

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.booksearchingapp.books.BooksAdapter
import com.example.booksearchingapp.data.Book

@BindingAdapter("app:items")
fun setItems(view: RecyclerView, items: List<Book>?) {
    items?.let {
        (view.adapter as BooksAdapter).submitList(items)
    }
}