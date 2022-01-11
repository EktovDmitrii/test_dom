package com.custom.rgs_android_dom.ui.property.document.detail_document

import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import android.webkit.WebViewClient
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.data.providers.auth.manager.AuthContentProviderManager
import com.custom.rgs_android_dom.databinding.FragmentDetailDocumentBinding
import com.custom.rgs_android_dom.databinding.FragmentDocumentBinding
import com.custom.rgs_android_dom.domain.property.models.PropertyDocumentsModel
import com.custom.rgs_android_dom.ui.base.BaseFragment
import com.custom.rgs_android_dom.ui.property.document.DocumentFragment
import com.custom.rgs_android_dom.ui.property.document.DocumentViewModel
import com.custom.rgs_android_dom.utils.args
import retrofit2.http.Url


class DetailDocumentFragment :
    BaseFragment<DocumentViewModel, FragmentDetailDocumentBinding>(R.layout.fragment_detail_document) {

    var url: String? = null


    companion object {
        private const val SONE = "SONE"

        fun newInstance(
            url: String
        ): DetailDocumentFragment {
            return DetailDocumentFragment().args {
                putString(SONE, url)
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        url =
            if (requireArguments().containsKey(SONE))
                requireArguments().getString(SONE) as String
            else
                null

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val authContentProviderManager = AuthContentProviderManager(requireContext())
        val token = "Bearer " +authContentProviderManager.getAccessToken()!!
        val headerMap = HashMap<String, String>()
        headerMap["Authorization:"] = token

        binding.webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                view.loadUrl(url, headerMap)
                return true
            }

            override fun onPageFinished(view: WebView?, url: String?) {

            }

            override fun shouldInterceptRequest(
                view: WebView?,
                request: WebResourceRequest?
            ): WebResourceResponse? {
                return null

            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {

            }


        }
        Log.d("TAG",token.toString())
        binding.webView.loadUrl("http://docs.google.com/gview?embedded=true&url=" + url, headerMap)
    }

}
