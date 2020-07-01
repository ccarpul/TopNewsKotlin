package com.example.topnewsmvvmkotlin.util

import com.example.topnewsmvvmkotlin.BuildConfig

object Constants {

    //NETWORK
    const val BASEURL   = "https://newsapi.org/v2/"
    const val ENDPOINT  = "top-headlines?"
    const val APIKEY    = BuildConfig.NEWS_APIKEY
    const val PAGESIZE  = 5
    const val PAGE_INIT = 1
    const val TOTAL_RESULTS_INIT = 0

    //FILTERS
    const val NO_FILTERS_SELECTED = ",,,, "
    const val FIELD_EMPTY = ""

}