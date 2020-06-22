package com.example.topnewsmvvmkotlin.util

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import okhttp3.Response
import retrofit2.HttpException
import java.io.IOException

/**
 * Created by Mariangela Salcedo (msalcedo047@gmail.com) on 6/22/20.
 * Copyright (c) 2020 m-salcedo. All rights reserved.
 */

suspend fun <T> safeApiCall(
    dispatcher: CoroutineDispatcher,
    apiCall: suspend () -> T
): ResultWrapper<T> {
    return withContext(dispatcher) {
        try {
            val response = apiCall.invoke()
            if (response is Response && !response.isSuccessful) {
                ResultWrapper.GenericError(null, response.message())
            } else ResultWrapper.Success(response)
        } catch (throwable: Throwable) {
            when (throwable) {
                is IOException -> ResultWrapper.GenericError(null, "Error")
                is HttpException -> ResultWrapper.NetworkError(throwable)
                else -> ResultWrapper.GenericError(null, "Error")
            }
        }
    }
}

sealed class ResultWrapper<out T> {
    data class Success<out T>(val value: T) : ResultWrapper<T>()
    data class GenericError(val code: Int? = null, val error: String? = null) :
        ResultWrapper<Nothing>()

    data class NetworkError(val throwable: HttpException) : ResultWrapper<Nothing>()
}