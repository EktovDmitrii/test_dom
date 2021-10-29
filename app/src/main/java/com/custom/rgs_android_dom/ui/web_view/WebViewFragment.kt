package com.custom.rgs_android_dom.ui.web_view

import android.os.Bundle
import android.view.View
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.databinding.FragmentWebViewBinding
import com.custom.rgs_android_dom.ui.base.BaseFragment
import com.custom.rgs_android_dom.utils.args

class WebViewFragment : BaseFragment<WebViewViewModel, FragmentWebViewBinding>(R.layout.fragment_web_view) {

    companion object {
        private const val ARG_URL = "ARG_URL"

        fun newInstance(url: String): WebViewFragment {
            return WebViewFragment().args {
                putString(ARG_URL, url)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.webView.settings.javaScriptEnabled = true
        binding.webView.settings.loadWithOverviewMode = true
        binding.webView.settings.useWideViewPort = true
        binding.webView.settings.allowFileAccess = true
        binding.webView.settings.allowContentAccess = true
        binding.webView.settings.allowFileAccessFromFileURLs = true
        binding.webView.settings.allowUniversalAccessFromFileURLs = true
        binding.webView.settings.domStorageEnabled = true
        binding.webView.settings.setSupportZoom(true)
        binding.webView.settings.builtInZoomControls = true
        binding.webView.settings.displayZoomControls = false

        binding.webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                return super.shouldOverrideUrlLoading(view, request);
            }
        }

        val url = requireArguments().getString(ARG_URL, "")
        binding.webView.loadUrl(url)

    }

    override fun onClose() {
        if (binding.webView.canGoBack()){
            binding.webView.goBack()
        } else {
            super.onClose()
        }
    }

}