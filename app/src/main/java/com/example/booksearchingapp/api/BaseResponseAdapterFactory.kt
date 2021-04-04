package com.example.booksearchingapp.api

import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

class BaseResponseAdapterFactory : CallAdapter.Factory() {

    override fun get(
        returnType: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ): CallAdapter<*, *>? {

        // suspend functions wrap the response type in `Call`
        if (Call::class.java != getRawType(returnType)) {
            return null
        }

        // check first that the return type is `ParameterizedType`
        check(returnType is ParameterizedType) {
            "return type must be parameterized as Call<BaseResponse<<Foo>> or Call<BaseResponse<out Foo>>"
        }

        // get the response type inside the `Call` type
        val responseType = getParameterUpperBound(0, returnType)
        // if the response type is not ApiResponse then we can't handle this type, so we return null
        if (getRawType(responseType) != BaseResponse::class.java) {
            return null
        }

        // the response type is ApiResponse and should be parameterized
        check(responseType is ParameterizedType) { "Response must be parameterized as BaseResponse<Foo> or BaseResponse<out Foo>" }

        val successBodyType = getParameterUpperBound(0, responseType)

        return BaseResponseAdapter<Any>(successBodyType)
    }
}


class BaseResponseAdapter<R : Any>(
    private val successType: Type
) : CallAdapter<R, Call<BaseResponse<R>>> {

    override fun responseType(): Type = successType

    override fun adapt(call: Call<R>): Call<BaseResponse<R>> {
        return BaseResponseCall(call)
    }
}

