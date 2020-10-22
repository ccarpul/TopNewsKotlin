package com.example.topnewsmvvmkotlin.ui.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.topnewsmvvmkotlin.R
import com.example.topnewsmvvmkotlin.ui.MainActivity
import com.example.topnewsmvvmkotlin.ui.adapter.ArticlesAdapterRecyclerView
import com.example.topnewsmvvmkotlin.util.*
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_home.*
import org.koin.android.viewmodel.ext.android.viewModel

class HomeFragment : Fragment(), ArticlesAdapterRecyclerView.OnClickSelectedItem {

    private val homeViewModel: HomeViewModel by viewModel() //inyección de dependencia

    private lateinit var toolBar: MaterialToolbar
    private lateinit var navBottomNavigation: BottomNavigationView
    private lateinit var linearLayoutManager: LinearLayoutManager
    private var adapterRecycler: ArticlesAdapterRecyclerView =
        ArticlesAdapterRecyclerView(mutableListOf(), this)

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        setQuery(arguments)
        toolBar = (activity as MainActivity).toolBar
        toolBar.show()

        if (auth.currentUser == null) {
            findNavController().apply {
                popBackStack()
                navigate(R.id.loginFragment)
            }

        } else {
            val username = auth.currentUser?.email ?: auth.currentUser?.displayName
            toolBar.subtitle = username
        }
        homeViewModel.modelArticles.observe(this, Observer(::upDateUi))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navBottomNavigation = (activity as MainActivity).navBottomNavigation
        toolBar.title = getString(R.string.app_name)
        setupRecyclerView()
        onScrollTopNews()

        toolBar.setOnMenuItemClickListener {

            if(it.itemId == R.id.logout) {
                auth.signOut()
                findNavController().apply {
                    popBackStack()
                    navigate(R.id.loginFragment)
                }
            }
            false
        }
    }

    private fun upDateUi(state: HomeViewModel.StateLiveData) {
        when (state) {
            is HomeViewModel.StateLiveData.InitialStateUi -> {
                homeViewModel.getDataArticles()
            }
            is HomeViewModel.StateLiveData.PreCall -> {
                progressBar.show()
            }

            is HomeViewModel.StateLiveData.RefreshStateUi -> {

                if (homeViewModel.getTotalResults() == 0) {
                    findNavController().apply {
                        popBackStack()
                        navigate(R.id.filtersFragment)
                    }
                    makeToast(context, getString(R.string.noResults))
                }
                adapterRecycler.addData(state.response)
            }
            is HomeViewModel.StateLiveData.PostCall -> {
                progressBar.hide()
                if (!navBottomNavigation.isVisible) navBottomNavigation.show()
            }

            is HomeViewModel.StateLiveData.AdapterRecycler -> {
                for (data in state.dataRecyclerView)
                    adapterRecycler.addData(data)
                topNews_recyclerView.layoutManager?.scrollToPosition(homeViewModel.pos.minus(1))
            }
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
                homeViewModel.pos = adapterRecycler.getPosition()
                if (topNews_recyclerView.isLastArticleDisplayed(linearLayoutManager)) {
                    if (homeViewModel.page++ * Constants.PAGESIZE < homeViewModel.getTotalResults()) {
                        homeViewModel.getDataArticles()

                    } else {
                        makeToast(context, getString(R.string.allArticlesdisplayed))
                    }
                }
            }
        })
    }

    private fun setQuery(args: Bundle?) {
        if (args != null) homeViewModel.queryFilters = HomeFragmentArgs.fromBundle(args)
            .defaulValuesFilter.split(",")
    }

    override fun onClick(query: String) {

        if("save" in query) homeViewModel.setArticle()
        else {
            val passUrl = HomeFragmentDirections.actionHomeFragmentToDeepLinkFragment(query)
            findNavController().navigate(passUrl)
        }
    }
}