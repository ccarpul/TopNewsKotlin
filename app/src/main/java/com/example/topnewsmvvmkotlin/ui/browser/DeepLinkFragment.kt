package com.example.topnewsmvvmkotlin.ui.browser

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.topnewsmvvmkotlin.R
import com.example.topnewsmvvmkotlin.util.mWebViewClient
import kotlinx.android.synthetic.main.fragment_deeplink.*

class DeepLinkFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_deeplink, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args = arguments?.let { DeepLinkFragmentArgs.fromBundle(it) }
        webView.webViewClient = mWebViewClient()
        webView.loadUrl(args?.url)
    }
}