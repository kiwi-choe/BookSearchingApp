package com.example.booksearchingapp.di

import com.example.booksearchingapp.api.BookService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideBookService(): BookService {
        return BookService.create()
    }
}

