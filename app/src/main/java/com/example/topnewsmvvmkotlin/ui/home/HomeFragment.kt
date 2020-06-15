package com.example.topnewsmvvmkotlin.ui.home

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.LinearLayout
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.topnewsmvvmkotlin.R
import com.example.topnewsmvvmkotlin.data.model.ModelResponse
import com.example.topnewsmvvmkotlin.ui.adapter.ArticlesAdapterRecyclerView
import com.example.topnewsmvvmkotlin.ui.browser.WebActivity
import com.example.topnewsmvvmkotlin.util.Constants
import kotlinx.android.synthetic.main.fragment_home.*
import org.koin.android.viewmodel.ext.android.viewModel

class HomeFragment : Fragment(), ArticlesAdapterRecyclerView.OnClickSelectedItem {

    private val homeViewModel: HomeViewModel by viewModel()  //inyecciòn de dependencia
    private lateinit var AdapterRecycler: ArticlesAdapterRecyclerView
    private lateinit var linearLayoutManager: LinearLayoutManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    @SuppressLint("FragmentLiveDataObserve")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        OnScrollTopNews()
        homeViewModel.getDataArticles.observe(this, Observer(::upDateUi)) //.Observer {upDate(it}
    }

    private fun upDateUi(data: ModelResponse) {
        AdapterRecycler.addData(data)
    }

    private fun setupRecyclerView() {
        AdapterRecycler = ArticlesAdapterRecyclerView(mutableListOf(), this)
        linearLayoutManager = LinearLayoutManager(context)
        topNews_recyclerView.apply {
            layoutManager = linearLayoutManager
            adapter = AdapterRecycler

        }
    }

    override fun onClick(query: String) {
        var intent = Intent(context, WebActivity::class.java)
        intent.putExtra(Constants.KEYURL_INTENT, query)
        startActivity(intent)
    }

    private fun OnScrollTopNews() {
        topNews_recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                
                if(isLastArticleDisplayed(recyclerView))
                    homeViewModel.getDataArticles
                    Log.i(TAG, "onScrolled:")

            }
        })
    }

    private fun isLastArticleDisplayed (recyclerView: RecyclerView): Boolean {

        var totalItems: Int? = recyclerView.adapter?.itemCount
        val lastVisibleItemPosition = linearLayoutManager.findLastCompletelyVisibleItemPosition()
        if (totalItems!= 0) {
            if (lastVisibleItemPosition != RecyclerView.NO_POSITION
                && lastVisibleItemPosition == (totalItems?.minus(1))
            ) return true
        }
        return false
    }
}