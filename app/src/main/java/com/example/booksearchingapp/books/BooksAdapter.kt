package com.example.booksearchingapp.books

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter

class BooksAdapter(private val viewModel: BooksViewModel) :
    ListAdapter<BookPreviewData, BookViewHolder>(BooksDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        return BookViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(viewModel, item)
    }

    fun updateLiked(updatedPosition: Int) {
        val updatingItem = getItem(updatedPosition)
        if (updatingItem != null) {
            updatingItem.liked = true
            notifyItemChanged(updatedPosition)
        }
    }
}

class BooksDiffCallback : DiffUtil.ItemCallback<BookPreviewData>() {
    override fun areItemsTheSame(oldItem: BookPreviewData, newItem: BookPreviewData): Boolean {
        return oldItem.isbn == newItem.isbn
    }

    override fun areContentsTheSame(oldItem: BookPreviewData, newItem: BookPreviewData): Boolean {
        return oldItem.isbn == newItem.isbn && oldItem.liked == newItem.liked
    }
}