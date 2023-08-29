package com.walletmix.superapp

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.walletmix.superapp.databinding.ActivityWebBinding

class WebActivity : AppCompatActivity() {


    private lateinit var binding: ActivityWebBinding

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWebBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val url = intent.getStringExtra("URL_KEY")

        val webView = binding.webView

        //webView setups

        webView.isClickable = true
        webView.settings.apply {
            javaScriptEnabled = true
            domStorageEnabled = true
            cacheMode = WebSettings.LOAD_NO_CACHE
            loadWithOverviewMode = true
            useWideViewPort = true
        }
//        webView.settings.javaScriptEnabled = true
//        webView.settings.supportZoom()
//        webView.webViewClient = CustomWebViewClient()
        webView.webViewClient = WebViewClient()
        if (!url.isNullOrEmpty()) {
            webView.loadUrl(url)
        } else {
            Toast.makeText(this, "Invalid Url", Toast.LENGTH_LONG).show()
        }


//        if (url != null) {
//            binding.webView.loadUrl(url)
//        }
//        if (!url.isNullOrBlank()) {
//            binding.webView.loadUrl(url)
//        } else {
//
//        }


    }

    inner class WebViewClient : android.webkit.WebViewClient() {

        // Load the URL
        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            view.loadUrl(url)
            return false
        }

        // ProgressBar will disappear once page is loaded
        override fun onPageFinished(view: WebView, url: String) {
            super.onPageFinished(view, url)
            binding.progressBar.visibility = View.GONE
        }
    }

//    inner class CustomWebViewClient : WebViewClient() {
//        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
//            super.onPageStarted(view, url, favicon)
//            // Show the progress bar when a page starts loading
////            binding.progressBar.visibility = View.VISIBLE
////            web_view.visibility = View.GONE
//
//            binding.apply {
//                progressBar.visibility = View.VISIBLE
//                webView.visibility = View.GONE
//            }
//        }
//
//        override fun onPageFinished(view: WebView?, url: String?) {
//            super.onPageFinished(view, url)
//            // Hide the progress bar when the page finishes loading
////            progress_bar.visibility = View.GONE
////            web_view.visibility = View.VISIBLE
//            binding.apply {
//                progressBar.visibility = View.GONE
//                webView.visibility = View.VISIBLE
//            }
//
//        }
//    }
}