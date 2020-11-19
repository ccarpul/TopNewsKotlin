package com.example.topnewsmvvmkotlin.ui.home

import androidx.lifecycle.LiveData
import com.example.topnewsmvvmkotlin.data.NewsApiClient
import com.example.topnewsmvvmkotlin.data.model.ModelResponse
import com.example.topnewsmvvmkotlin.utils.Constants
import com.example.topnewsmvvmkotlin.utils.ResultWrapper
import com.example.topnewsmvvmkotlin.utils.safeApiCall
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

class HomeRepository(private val newsApiClient: NewsApiClient) {

    val dispatcher = Dispatchers.IO

    /**queryFilters [source, language, country, category]*/
     suspend fun getArticles(page: Int, queryFilters: List<String>): ResultWrapper<ModelResponse> =

        //withContext(dispatcher) {
            safeApiCall(dispatcher) {
                newsApiClient.getArticles(
                    page, Constants.PAGESIZE,
                    queryFilters[0], //Country
                    queryFilters[1], //Category
                    queryFilters[2], //Source
                    queryFilters[3], //Language
                    queryFilters[4]
                ) //KeyWord
            }
        //}
}