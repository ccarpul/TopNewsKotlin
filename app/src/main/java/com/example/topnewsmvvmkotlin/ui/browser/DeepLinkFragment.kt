package com.example.topnewsmvvmkotlin.ui.browser

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.topnewsmvvmkotlin.R
import com.example.topnewsmvvmkotlin.ui.MainActivity
import com.example.topnewsmvvmkotlin.util.WebViewClient
import com.example.topnewsmvvmkotlin.util.hide
import com.example.topnewsmvvmkotlin.util.show
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.fragment_deeplink.*

class DeepLinkFragment : Fragment() {

    private lateinit var bottomNavView: BottomNavigationView
    //private lateinit var titleActionBar: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_deeplink, container, false)
    }

    @SuppressLint("NewApi")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bottomNavView = activity?.findViewById(R.id.navBottomNavigation)!!
        //titleActionBar = activity?.findViewById(R.id.titleActionBar)!!
        bottomNavView.hide()

        val args = arguments?.let { DeepLinkFragmentArgs.fromBundle(it) }

        webView.apply {

            webViewClient = WebViewClient()
            loadUrl(args?.url)
            webViewClient = object : android.webkit.WebViewClient() {
                override fun onPageFinished(view: WebView, url: String) {
                    if(progressBarWebView != null) progressBarWebView.hide()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        bottomNavView.show()
        //titleActionBar.show()
    }
}