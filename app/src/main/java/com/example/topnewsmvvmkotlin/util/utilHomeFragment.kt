package com.example.topnewsmvvmkotlin.util
import android.content.Context
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.topnewsmvvmkotlin.ui.adapter.ArticlesAdapterRecyclerView
import kotlinx.android.synthetic.main.fragment_home.*

fun isLastArticleDisplayed(recyclerView: RecyclerView, linearLayoutManager: LinearLayoutManager): Boolean {

    var totalItems: Int? = recyclerView.adapter?.itemCount
    val lastVisibleItemPosition = linearLayoutManager.findLastCompletelyVisibleItemPosition()
    if (totalItems != 0) {
        if (lastVisibleItemPosition != RecyclerView.NO_POSITION
            && lastVisibleItemPosition == (totalItems?.minus(1))
        ) return true
    }
    return false
}