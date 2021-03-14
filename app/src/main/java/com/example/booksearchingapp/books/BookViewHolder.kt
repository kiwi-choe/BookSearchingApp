package com.example.booksearchingapp.books

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.booksearchingapp.databinding.ItemBookBinding

class BookViewHolder private constructor(private val binding: ItemBookBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(viewModel: BooksViewModel, item: BookPreviewData) {
        binding.run {
            viewmodel = viewModel
            book = item
            executePendingBindings()
        }
    }

    companion object {
        fun from(parent: ViewGroup): BookViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ItemBookBinding.inflate(layoutInflater, parent, false)

            return BookViewHolder(binding)
        }
    }
}