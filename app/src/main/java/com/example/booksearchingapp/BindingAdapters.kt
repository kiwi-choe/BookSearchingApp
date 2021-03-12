package com.example.booksearchingapp

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.booksearchingapp.books.BooksAdapter
import com.example.booksearchingapp.books.BookViewData
import com.squareup.picasso.Picasso

@BindingAdapter("app:items")
fun setItems(view: RecyclerView, items: List<BookViewData>?) {
    items?.let {
        (view.adapter as BooksAdapter).submitList(items)
    }
}

@BindingAdapter("app:imgUrl")
fun setImgUrl(view: ImageView, url: String?) {
    if (url.isNullOrEmpty().not()) {
        Picasso.with(view.context).load(url).apply {
            into(view)
        }
    }
}