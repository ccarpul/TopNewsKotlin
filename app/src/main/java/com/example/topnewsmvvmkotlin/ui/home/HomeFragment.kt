package com.example.topnewsmvvmkotlin.ui.home

import android.content.ContentValues.TAG
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.topnewsmvvmkotlin.R
import com.example.topnewsmvvmkotlin.data.model.ModelResponse
import com.example.topnewsmvvmkotlin.ui.adapter.ArticlesAdapterRecyclerView
import com.example.topnewsmvvmkotlin.util.Constants
import com.example.topnewsmvvmkotlin.util.isLastArticleDisplayed
import com.example.topnewsmvvmkotlin.util.makeToast
import kotlinx.android.synthetic.main.fragment_home.*
import org.koin.android.viewmodel.ext.android.viewModel


class HomeFragment : Fragment(), ArticlesAdapterRecyclerView.OnClickSelectedItem {

    private val homeViewModel: HomeViewModel by viewModel() //inyección de dependencia
    private lateinit var linearLayoutManager: LinearLayoutManager
    private var adapterRecycler: ArticlesAdapterRecyclerView
           = ArticlesAdapterRecyclerView(mutableListOf(), this)
    private var totalResults: Int = Constants.TOTAL_RESULTS_INIT

    override fun onAttach(context: Context) {
        super.onAttach(context)
        homeViewModel.modelArticles.observe(this, Observer(::upDateUi))
        setQuery(arguments)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onScrollTopNews()
    }

    var i =1
    private fun upDateUi(state: HomeViewModel.StateLiveData) {

        when(state){
            is HomeViewModel.StateLiveData.InitialStateUi -> {
                setupRecyclerView()
                homeViewModel.getDataArticles()
            }
            is HomeViewModel.StateLiveData.PreCall -> {/*TODO progressBar.Show()*/}
            is HomeViewModel.StateLiveData.RefreshStateUi -> {
                totalResults = state.response.totalResults
                if (totalResults == 0) {
                    findNavController().navigate(R.id.filtersFragment)
                    makeToast(context, getString(R.string.noResults))
                }
                adapterRecycler.addData(state.response)
            }
            is HomeViewModel.StateLiveData.PostCall -> { /*TODO progressBar.Hide()*/}
        }
    }

    private fun setupRecyclerView() {
        linearLayoutManager = LinearLayoutManager(requireContext())
        topNews_recyclerView.apply {
            layoutManager = linearLayoutManager
            adapter = adapterRecycler
        }
    }

    private fun onScrollTopNews() {
        topNews_recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                if (topNews_recyclerView.isLastArticleDisplayed(linearLayoutManager)) {
                    if (homeViewModel.page++ * Constants.PAGESIZE < totalResults)
                        homeViewModel.getDataArticles()
                    else  makeToast(context, getString(R.string.allArticlesdisplayed))
                }
            }
        })
    }

    private fun setQuery(args: Bundle?) {
        if (args != null) homeViewModel.queryFilters = HomeFragmentArgs.fromBundle(args)
            .defaulValuesFilter.split(",")
    }

    override fun onClick(query: String) {
        val passUrl
                = HomeFragmentDirections.actionHomeFragmentToDeepLinkFragment(query)
        findNavController().navigate(passUrl)
    }
}
