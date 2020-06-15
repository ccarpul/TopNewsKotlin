package com.example.topnewsmvvmkotlin.ui.home

import com.example.topnewsmvvmkotlin.data.NewsApiClient
import com.example.topnewsmvvmkotlin.data.model.ModelResponse
import com.example.topnewsmvvmkotlin.util.Constants

class HomeRepository(private val newsApiClient: NewsApiClient) {

    suspend fun getArticles(page: Int): ModelResponse {

        val rPage =  page
        return newsApiClient.getArticles(rPage, Constants.PAGESIZE, Constants.KEYWORD_INIT, Constants.COUNTRY_INIT,
            Constants.CATEGORY_INIT, Constants.SOURCE_INIT, Constants.LANGUAGE_INIT
        )
    }
}