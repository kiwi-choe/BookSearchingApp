package com.example.booksearchingapp.di

import com.example.booksearchingapp.api.BaseResponseAdapterFactory
import com.example.booksearchingapp.api.BookService
import com.example.booksearchingapp.books.BooksViewModel
import com.example.booksearchingapp.data.BookRepository
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val koinModuleApp = module {
    single { BookRepository(get()) }
    viewModel { BooksViewModel(get()) }
}

private const val BASE_URL = "https://dapi.kakao.com"
private const val REST_API_KEY = "KakaoAK 5c006a144af5d6ca233c083a882b81ac"

val networkModule = module {
    single {
        val logger =
            HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }

        val client = OkHttpClient.Builder()
            .addInterceptor(logger)
            .addInterceptor(Interceptor() { chain ->
                val newRequest = chain.request().newBuilder()
                    .addHeader("Authorization", REST_API_KEY)
                    .build()
                chain.proceed(newRequest)
            })
            .build()

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addCallAdapterFactory(BaseResponseAdapterFactory())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(BookService::class.java)
    }
}
