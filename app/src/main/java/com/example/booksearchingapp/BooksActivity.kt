package com.example.booksearchingapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.booksearchingapp.books.BooksFragment

class BooksActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_books)

        if (savedInstanceState == null) {
            setupBooksFragment()
        }
    }

    private fun setupBooksFragment() {
        supportFragmentManager.beginTransaction()
            .add(
                R.id.fragment_container,
                BooksFragment.newInstance(),
                BooksFragment::class.java.simpleName
            )
            .commit()
    }
}