package com.example.topnewsmvvmkotlin.ui.browser

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.topnewsmvvmkotlin.R
import com.example.topnewsmvvmkotlin.util.Constants
import com.example.topnewsmvvmkotlin.util.mWebViewClient
import kotlinx.android.synthetic.main.activity_web.*

class WebActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web)

        val url = intent.getStringExtra(Constants.KEYURL_INTENT)
        webView.webViewClient =
            mWebViewClient()
        webView.loadUrl(url)

    }
}
