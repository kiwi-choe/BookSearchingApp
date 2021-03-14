package com.example.booksearchingapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.booksearchingapp.books.BooksFragment
import com.google.android.material.navigation.NavigationView

class BooksActivity : AppCompatActivity() {

//    private val navController by lazy { findNavController(R.id.nav_fragment_container) }
//    private val appBarConfiguration by lazy { AppBarConfiguration(navController.graph) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_books)

//        setupActionBar()

        supportFragmentManager.beginTransaction()
            .add(R.id.fragment_container, BooksFragment.newInstance(), BooksFragment::class.java.simpleName)
            .commit()

    }

//    private fun setupActionBar() {
//        setSupportActionBar(findViewById(R.id.toolbar))

//        setupActionBarWithNavController(navController, appBarConfiguration)
//    }

//    override fun onSupportNavigateUp(): Boolean {
//        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
//    }
}