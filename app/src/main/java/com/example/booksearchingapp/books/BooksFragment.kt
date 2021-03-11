package com.example.booksearchingapp.books

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.booksearchingapp.databinding.FragmentBooksBinding
import com.google.android.material.snackbar.Snackbar
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
        super.onViewCreated(view, savedInstanceState)

        setupEnterSearchingQueryView()
        setupBooksAdapter()
        setupSnackbar()
    }

    private fun setupSnackbar() {
        viewModel.snackbarMessage.observe(viewLifecycleOwner) { stringRes ->
            stringRes?.let { showSnackbar(it) }
        }
    }

    private fun showSnackbar(@StringRes stringRes: Int) {
        view?.let { Snackbar.make(it, stringRes, Snackbar.LENGTH_SHORT).show() }
    }

    private fun setupEnterSearchingQueryView() {
        binding.editBooksSearching.addTextChangedListener {
            viewModel.onTextChanged()
        }
    }

    private fun setupBooksAdapter() {
        binding.listBooks.adapter = BooksAdapter(viewModel)
    }
}