package com.example.topnewsmvvmkotlin.ui.home

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.topnewsmvvmkotlin.R
import com.example.topnewsmvvmkotlin.data.model.ModelResponse
import com.example.topnewsmvvmkotlin.ui.adapter.ArticlesAdapterRecyclerView
import com.example.topnewsmvvmkotlin.ui.browser.DeepLinkFragmentArgs
import com.example.topnewsmvvmkotlin.ui.filters.FiltersFragmentDirections
import com.example.topnewsmvvmkotlin.util.Constants
import com.example.topnewsmvvmkotlin.util.isLastArticleDisplayed
import kotlinx.android.synthetic.main.fragment_home.*
import org.koin.android.viewmodel.ext.android.viewModel
import kotlin.math.log

class HomeFragment : Fragment(), ArticlesAdapterRecyclerView.OnClickSelectedItem {

    private val homeViewModel: HomeViewModel by viewModel()  //inyecciòn de dependencia
    private lateinit var linearLayoutManager: LinearLayoutManager
    private val adapterRecycler: ArticlesAdapterRecyclerView = ArticlesAdapterRecyclerView(mutableListOf(), this)
    private var totalResults: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setQuery(arguments)//Arguments from SafeArgs
        homeViewModel.getDataArticles.observe(this, Observer(::upDateUi))
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?
                              , savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        onScrollTopNews()
    }

    private fun upDateUi(data: ModelResponse) {
        adapterRecycler.addData(data)
        totalResults = data.totalResults

    }
    private fun setupRecyclerView() {
        linearLayoutManager = LinearLayoutManager(context)
        topNews_recyclerView.apply {
            layoutManager = linearLayoutManager
            adapter = adapterRecycler
        }
    }
    private fun onScrollTopNews() {
        topNews_recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (isLastArticleDisplayed(recyclerView, linearLayoutManager))
                    if (homeViewModel.page++ * Constants.PAGESIZE <= totalResults) homeViewModel.getDataArticles
                    else Toast.makeText(context, resources.getString(R.string.allArticlesdisplayed), Toast.LENGTH_SHORT).show()
            }
        })
    }
    private fun setQuery(args: Bundle?) {
        if (args != null) homeViewModel.queryFilters = HomeFragmentArgs.fromBundle(args)
                .defaulValuesFilter.split(",") }

    override fun onClick(query: String) {
        val passUrl = HomeFragmentDirections.actionHomeFragmentToDeepLinkFragment(query)
        findNavController().navigate(passUrl)

    }
}