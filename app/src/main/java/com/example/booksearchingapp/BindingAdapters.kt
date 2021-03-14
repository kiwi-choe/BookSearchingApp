package com.example.booksearchingapp

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.squareup.picasso.Picasso

@BindingAdapter("app:imgUrl")
fun setImgUrl(view: ImageView, url: String?) {
    if (url.isNullOrEmpty().not()) {
        Picasso.with(view.context).load(url).apply {
            into(view)
        }
    }
}