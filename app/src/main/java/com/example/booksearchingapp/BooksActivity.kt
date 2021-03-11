package com.example.booksearchingapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.booksearchingapp.books.BooksFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BooksActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setFragment()
    }

    private fun setFragment() {
        supportFragmentManager.beginTransaction()
            .add(R.id.fragment_container, BooksFragment.newInstance()).commit()
    }
}