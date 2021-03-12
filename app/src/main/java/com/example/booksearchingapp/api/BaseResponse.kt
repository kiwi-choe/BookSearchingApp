package com.example.booksearchingapp.api

import dagger.Provides
import java.io.IOException

/**
 * A generic class that holds a value with its loading status.
 * @param <T>
 */

sealed class BaseResponse<out T : Any> {

    data class Success<T : Any>(val data: T) : BaseResponse<T>()

    data class ApiError(val errorMessage: String) : BaseResponse<Nothing>()

    data class NetworkError(val error: IOException) : BaseResponse<Nothing>()

    /**
     * For example, json parsing error
     */
    data class UnknownError(val error: Throwable?) : BaseResponse<Nothing>()

    object Loading : BaseResponse<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[=$data]"
            is ApiError -> "ApiError[=${errorMessage}]"
            is NetworkError -> "NetworkError[=${error.message}]"
            is UnknownError -> "UnknownError[=${error?.message ?: "null"}]"
            Loading -> "Loading"
        }
    }
}

///**
// * `true` if [BaseResponse] is of type [Success] & holds non-null [Success.data].
// */
//val BaseResponse<*>.succeeded
//    get() = this is Success && data != null
