package com.example.topnewsmvvmkotlin.ui.home

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.topnewsmvvmkotlin.R
import com.example.topnewsmvvmkotlin.data.model.ModelResponse
import com.example.topnewsmvvmkotlin.ui.adapter.ArticlesAdapterRecyclerView
import com.example.topnewsmvvmkotlin.ui.browser.WebActivity
import com.example.topnewsmvvmkotlin.util.Constants
import kotlinx.android.synthetic.main.fragment_home.*
import org.koin.android.viewmodel.ext.android.viewModel

class HomeFragment : Fragment(), ArticlesAdapterRecyclerView.OnClickSelectedItem  {

    private val homeViewModel: HomeViewModel by viewModel()  //inyecciòn de dependencia
    private lateinit var listAdapter: ArticlesAdapterRecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        homeViewModel.getDataArticles.observe(this, Observer(::upDateUi)) //.Observer {upDate(it}
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    private fun upDateUi(data: ModelResponse) {

        listAdapter = ArticlesAdapterRecyclerView(mutableListOf(), this)
        listAdapter.addData(data)
        setupRecyclerView()
    }
    private fun setupRecyclerView() {
        topNews_recyclerView.apply {
            layoutManager = LinearLayoutManager(context)

            adapter = listAdapter
        }
    }

    override fun onClick(query: String) {
        var intent = Intent(context, WebActivity::class.java)
        intent.putExtra(Constants.KEYURL_INTENT, query)
        startActivity(intent)
    }
}