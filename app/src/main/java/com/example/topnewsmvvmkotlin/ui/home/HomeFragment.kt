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
import kotlinx.android.synthetic.main.fragment_home.*
import org.koin.android.viewmodel.ext.android.viewModel
import kotlin.math.log

class HomeFragment : Fragment(), ArticlesAdapterRecyclerView.OnClickSelectedItem {

    private val homeViewModel: HomeViewModel by viewModel()  //inyecciòn de dependencia

    private lateinit var AdapterRecycler: ArticlesAdapterRecyclerView
    private lateinit var linearLayoutManager: LinearLayoutManager
    private var totalResults: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        homeViewModel.page = 1
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        homeViewModel.getDataArticles.observe(viewLifecycleOwner, Observer(::upDateUi))
        setupRecyclerView()
        onScrollTopNews()
        setQuery(arguments)  //Arguments from SafeArgs
    }

    private fun upDateUi(data: ModelResponse) {
        AdapterRecycler.addData(data)
        totalResults = data.totalResults

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
        val passUrl = HomeFragmentDirections.actionHomeFragmentToDeepLinkFragment(query)
        findNavController().navigate(passUrl)
    }

    private fun onScrollTopNews() {
        topNews_recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (isLastArticleDisplayed(recyclerView))
                    if (homeViewModel.page * Constants.PAGESIZE <= totalResults) homeViewModel.getDataArticles
                    else Toast.makeText(context, "Es todo!!!", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun isLastArticleDisplayed(recyclerView: RecyclerView): Boolean {

        var totalItems: Int? = recyclerView.adapter?.itemCount
        val lastVisibleItemPosition = linearLayoutManager.findLastCompletelyVisibleItemPosition()
        if (totalItems != 0) {
            if (lastVisibleItemPosition != RecyclerView.NO_POSITION
                && lastVisibleItemPosition == (totalItems?.minus(1))
            ) return true
        }
        return false
    }

    private fun setQuery(args: Bundle?) {
        if (args != null)
            homeViewModel.queryFilters = HomeFragmentArgs.fromBundle(args)
                .defaulValuesFilter.split(",")
    }
}