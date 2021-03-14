package com.example.booksearchingapp.books

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import com.example.booksearchingapp.EventObserver
import com.example.booksearchingapp.R
import com.example.booksearchingapp.bookdetail.BookDetailFragment
import com.example.booksearchingapp.databinding.FragmentBooksBinding
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class BooksFragment : Fragment() {

    private val viewModel: BooksViewModel by sharedViewModel()

    private lateinit var binding: FragmentBooksBinding

    companion object {
        fun newInstance(): BooksFragment {
            Log.d("BooksFragment", "is created")
            return BooksFragment()
        }
    }

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
        viewModel.navigateToBookDetail.observe(viewLifecycleOwner, EventObserver { bookId ->
            Log.d("BooksFragment", "navigateToBookDetail livedata is invoked")
            openBookDetail(bookId)
        })
    }

    private fun openBookDetail(bookId: String) {
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, BookDetailFragment.newInstance(bookId))
            .addToBackStack(null)
            .commit()
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

            viewModel.bookPreviewResults.observe(viewLifecycleOwner) { list ->
                (adapter as? BooksAdapter)?.submitList(list)
            }

            viewModel.updateLikedBook.observe(viewLifecycleOwner, EventObserver { updatedPosition ->
                Log.d("BooksFragment", "updatedPosition $updatedPosition")
                (adapter as? BooksAdapter)?.updateLiked(updatedPosition)
            })
        }
    }
}