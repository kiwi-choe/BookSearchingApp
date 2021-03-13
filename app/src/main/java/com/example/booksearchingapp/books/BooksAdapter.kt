package com.example.booksearchingapp.books

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.booksearchingapp.databinding.ItemBookBinding

class BooksAdapter(private val viewModel: BooksViewModel) :
    ListAdapter<BookViewData, BooksAdapter.BookViewHolder>(BooksDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        return BookViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(viewModel, item)
    }

    class BookViewHolder private constructor(private val binding: ItemBookBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(viewModel: BooksViewModel, item: BookViewData) {
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
}

class BooksDiffCallback : DiffUtil.ItemCallback<BookViewData>() {
    override fun areItemsTheSame(oldItem: BookViewData, newItem: BookViewData): Boolean {
        return oldItem.isbn == newItem.isbn
    }

    override fun areContentsTheSame(oldItem: BookViewData, newItem: BookViewData): Boolean {
        return oldItem == newItem
    }
}