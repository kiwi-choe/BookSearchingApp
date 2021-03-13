package com.example.booksearchingapp.api

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface BookService {
    @GET("/v3/search/book")
    suspend fun searchBooks(
        @Query("target") target: String = "title",
        @Query("page") page: Int,
        @Query("size") size: Int = 50,
        @Query("query") query: String
    ): BaseResponse<BookResponse>

    companion object {
        private const val BASE_URL = "https://dapi.kakao.com"
        private const val REST_API_KEY = "KakaoAK 5c006a144af5d6ca233c083a882b81ac"

        fun create(): BookService {
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

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addCallAdapterFactory(BaseResponseAdapterFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(BookService::class.java)
        }
    }
}