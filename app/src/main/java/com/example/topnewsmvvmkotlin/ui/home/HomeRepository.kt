package com.example.topnewsmvvmkotlin.ui.home

import com.example.topnewsmvvmkotlin.data.NewsApiClient
import com.example.topnewsmvvmkotlin.data.model.ModelResponse
import com.example.topnewsmvvmkotlin.util.Constants

class HomeRepository(private val newsApiClient: NewsApiClient) {

    //queryFilters [source, langunage, country, category]
    suspend fun getArticles(page: Int, queryFilters: List<String>): ModelResponse {

        return newsApiClient.getArticles(
            page, Constants.PAGESIZE,
            Constants.KEYWORD_INIT,
            queryFilters[0], //Country
            queryFilters[1], //Category
            queryFilters[2], //Source
            queryFilters[3]) //Language
    }
}