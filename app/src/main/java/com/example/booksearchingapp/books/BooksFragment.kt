package com.example.booksearchingapp.books

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.booksearchingapp.databinding.FragmentBooksBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BooksFragment : Fragment() {

    companion object {
        fun newInstance(): BooksFragment = BooksFragment()
    }

    private val viewModel by viewModels<BooksViewModel>()

    private lateinit var binding: FragmentBooksBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBooksBinding.inflate(inflater, container, false).apply {
            viewmodel = viewModel
            lifecycleOwner = viewLifecycleOwner
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.editBooksSearching.addTextChangedListener {
            Log.d("##", it?.toString() ?: "")
            viewModel.onTextChanged()
        }

        viewModel.books.observe(viewLifecycleOwner) {books ->
            // TODO: 11/03/2021 submitList

        }
    }
}