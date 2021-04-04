package com.example.booksearchingapp.bookdetail

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.example.booksearchingapp.R
import com.example.booksearchingapp.databinding.FragmentBookDetailBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class BookDetailFragment : Fragment() {

    companion object {
        private const val PARAM_KEY_ISBN = "param_key_isbn"
        fun newInstance(bookId: String) = BookDetailFragment().apply {
            arguments = bundleOf(PARAM_KEY_ISBN to bookId)
        }
    }

    private val viewModel: BookDetailViewModel by viewModel()

    private lateinit var binding: FragmentBookDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBookDetailBinding.inflate(inflater, container, false).apply {
            viewmodel = viewModel
            lifecycleOwner = viewLifecycleOwner
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as? AppCompatActivity)?.setSupportActionBar(binding.toolbar)

        setHasOptionsMenu(true)


        getArgsBookId()?.let { bookId ->
            viewModel.getBookDetail(bookId)
        } ?: return

        binding.toolbar.setNavigationOnClickListener {
            parentFragmentManager.popBackStackImmediate()
        }
    }

    private fun getArgsBookId() = arguments?.getString(PARAM_KEY_ISBN)

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.book_detail_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_like -> {
                getArgsBookId()?.let {
                    viewModel.likeBook(it)
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}