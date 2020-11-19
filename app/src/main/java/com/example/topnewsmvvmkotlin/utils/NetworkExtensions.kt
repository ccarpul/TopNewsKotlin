package com.example.topnewsmvvmkotlin.utils

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
): ResultWrapper<T> = //{

    //withContext(dispatcher) {
        try {
            val response = apiCall.invoke() // TAMBIEN -> val response = apiCall()
            if (response is Response && !response.isSuccessful) {
                ResultWrapper.GenericError(null, response.message())
            } else ResultWrapper.Success(response)

        } catch (throwable: Throwable) {
            when (throwable) {
                is IOException -> ResultWrapper.GenericError(null, "I/O Error: ${throwable.message}")
                is HttpException -> ResultWrapper.NetworkError(throwable)
                else -> ResultWrapper.GenericError(null, "Error: ${throwable.message}")
            }
        }
    //}
//}

sealed class ResultWrapper<out T> {
    data class Success<out T>(val value: T) : ResultWrapper<T>()
    data class GenericError(val code: Int? = null, val error: String? = null) :
        ResultWrapper<Nothing>()

    data class NetworkError(val throwable: HttpException) : ResultWrapper<Nothing>()
}

//Login with Firebase

sealed class Result<out T> {

    data class Success<out T>(val value: T) : Result<T>()
    data class GenericError(val error: String?) : Result<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success -> "Success: $value"
            is GenericError -> "ErrorGeneric"

        }
    }
}

data class LoginFormState(
    val usernameError: Int? = null,
    val passwordError: Int? = null,
    val isDataValid: Boolean = false
)