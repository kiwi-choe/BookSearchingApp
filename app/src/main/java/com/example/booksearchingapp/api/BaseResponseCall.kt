package com.example.booksearchingapp.api

import okhttp3.Request
import okio.Timeout
import retrofit2.*
import java.io.IOException

internal class BaseResponseCall<R : Any>(
    private val delegate: Call<R>,
) : Call<BaseResponse<R>> {

    override fun enqueue(callback: Callback<BaseResponse<R>>) {
        return delegate.enqueue(object : Callback<R> {
            override fun onResponse(call: Call<R>, response: Response<R>) {
                val body = response.body()
                val code = response.code()
                val error = response.errorBody()

                if (response.isSuccessful) {
                    if (body != null) {
                        callback.onResponse(
                            this@BaseResponseCall,
                            Response.success(BaseResponse.Success(body))
                        )
                    } else {
                        // Response is successful but the body is null
                        callback.onResponse(
                            this@BaseResponseCall,
                            Response.success(BaseResponse.UnknownError(null))
                        )
                    }
                } else {
                    val errorBody = when {
                        error == null -> null
                        error.contentLength() == 0L -> null
                        else -> error
                    }
                    if (errorBody != null) {
                        callback.onResponse(
                            this@BaseResponseCall,
                            Response.success(BaseResponse.ApiError("$code: $errorBody"))
                        )
                    } else {
                        callback.onResponse(
                            this@BaseResponseCall,
                            Response.success(BaseResponse.UnknownError(null))
                        )
                    }
                }
            }

            override fun onFailure(call: Call<R>, throwable: Throwable) {
                val baseResponse = when (throwable) {
                    is IOException -> BaseResponse.NetworkError(throwable)
                    else -> BaseResponse.UnknownError(throwable)
                }
                callback.onResponse(this@BaseResponseCall, Response.success(baseResponse))
            }
        })
    }

    override fun isExecuted() = delegate.isExecuted

    override fun clone() = BaseResponseCall(delegate.clone())

    override fun isCanceled() = delegate.isCanceled

    override fun cancel() = delegate.cancel()

    override fun execute(): Response<BaseResponse<R>> {
        throw UnsupportedOperationException("BaseResponseCall doesn't support execute")
    }

    override fun request(): Request = delegate.request()

    override fun timeout(): Timeout = delegate.timeout()
}