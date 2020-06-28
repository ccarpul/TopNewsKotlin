package com.example.topnewsmvvmkotlin.ui.browser

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.topnewsmvvmkotlin.R
import com.example.topnewsmvvmkotlin.util.WebViewClient
import kotlinx.android.synthetic.main.fragment_deeplink.*

class DeepLinkFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_deeplink, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args = arguments?.let { DeepLinkFragmentArgs.fromBundle(it) }
        webView.webViewClient = WebViewClient()
        webView.loadUrl(args?.url)
    }
}