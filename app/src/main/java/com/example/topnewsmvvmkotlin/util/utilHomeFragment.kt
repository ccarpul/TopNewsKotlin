package com.example.topnewsmvvmkotlin.util

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

fun RecyclerView.isLastArticleDisplayed(linearLayoutManager: LinearLayoutManager) :Boolean {

    val totalItems = this.adapter?.itemCount
    if (totalItems != 0) {
        if (RecyclerView.NO_POSITION != linearLayoutManager.findLastCompletelyVisibleItemPosition() &&
            linearLayoutManager.findLastCompletelyVisibleItemPosition() == totalItems?.minus(1))
            return true
    }
    return false
}