package com.example.topnewsmvvmkotlin.util

import com.example.topnewsmvvmkotlin.BuildConfig
import com.example.topnewsmvvmkotlin.R

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

    //FIREBASE

    const val DEFAUL_WEB_CLIENT_ID = "773632563142-m625b3k6utlfo7fdull73btrkn9vra2h.apps.googleusercontent.com"
    const val GOOGLE_LOGIN = 100
    const val TWITTER_LOGIN = 101
    const val FACEBOOK_LOGIN = 102

}