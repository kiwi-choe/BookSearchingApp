package com.example.booksearchingapp.books

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.example.booksearchingapp.databinding.FragmentBooksBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.WithFragmentBindings

@AndroidEntryPoint
@WithFragmentBindings
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
        setupNavigate()
    }

    private fun setupNavigate() {
        viewModel.navigateToBookDetail.observe(viewLifecycleOwner) {
            // TODO: 13/03/2021 navigate to Book detail page
        }
    }

    private fun setupSnackbar() {
        viewModel.snackbarMessage.observe(viewLifecycleOwner) { message ->
            message?.let { showSnackbar(it) }
        }
    }

    private fun showSnackbar(message: String) {
        view?.let { Snackbar.make(it, message, Snackbar.LENGTH_SHORT).show() }
    }

    private fun setupEnterSearchingQueryView() {
        binding.editBooksSearching.addTextChangedListener {
            viewModel.onTextChanged()
        }
    }

    private fun setupBooksAdapter() {
        binding.listBooks.run {
            adapter = BooksAdapter(viewModel)
            layoutManager?.let { layoutManager ->
                addOnScrollListener(object : EndlessRecyclerViewScrollListener(layoutManager) {
                    override fun onLoadMore() {
                        viewModel.listScrolled()
                    }
                })
            }

            viewModel.bookResults.observe(viewLifecycleOwner) { list ->
                Log.d("BooksFragment", "list $list")
                (adapter as? BooksAdapter)?.submitList(list)
            }
        }
    }
}