package com.example.topnewsmvvmkotlin.ui.browser

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import androidx.fragment.app.Fragment
import com.example.topnewsmvvmkotlin.R
import com.example.topnewsmvvmkotlin.util.WebViewClient
import com.example.topnewsmvvmkotlin.util.hide
import kotlinx.android.synthetic.main.fragment_deeplink.*

class DeepLinkFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_deeplink, container, false)
    }

    @SuppressLint("NewApi")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args = arguments?.let { DeepLinkFragmentArgs.fromBundle(it) }

        webView.apply {
            webViewClient = WebViewClient()
            loadUrl(args?.url)
            webViewClient = object : android.webkit.WebViewClient() {
                override fun onPageFinished(view: WebView, url: String) {
                    if(progressBarWebView != null) progressBarWebView.hide() }
            }
        }
    }
}