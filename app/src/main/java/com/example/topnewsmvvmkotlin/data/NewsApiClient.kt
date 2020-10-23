package com.example.topnewsmvvmkotlin.data

import com.example.topnewsmvvmkotlin.data.model.ModelResponse
import com.example.topnewsmvvmkotlin.utils.Constants
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApiClient {

    @GET("${Constants.ENDPOINT}${Constants.APIKEY}")
    suspend fun getArticles(
        @Query("page")     page: Int,
        @Query("pagesize") page_size: Int,
        @Query("country")  country: String?,
        @Query("category") category: String?,
        @Query("sources")  source: String?,
        @Query("language") language: String?,
        @Query("q")        key_word: String?) : ModelResponse
}
