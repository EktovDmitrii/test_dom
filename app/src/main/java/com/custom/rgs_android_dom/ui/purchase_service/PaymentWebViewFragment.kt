package com.custom.rgs_android_dom.ui.purchase_service

import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.webkit.*
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.databinding.FragmentPaymentWebviewBinding
import com.custom.rgs_android_dom.ui.base.BaseFragment
import com.custom.rgs_android_dom.ui.navigation.PAYMENT
import com.custom.rgs_android_dom.ui.navigation.ScreenManager
import com.custom.rgs_android_dom.utils.args
import com.custom.rgs_android_dom.utils.setStatusBarColor
import com.custom.rgs_android_dom.utils.subscribe
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.parameter.parametersOf

class PaymentWebViewFragment :
    BaseFragment<PaymentWebViewViewModel, FragmentPaymentWebviewBinding>(R.layout.fragment_payment_webview) {

    companion object {
        private const val ARG_PAYMENT_URL = "ARG_PRODUCT_ID"

        fun newInstance(url: String): PaymentWebViewFragment {
            return PaymentWebViewFragment().args {
                putString(ARG_PAYMENT_URL, url)
            }
        }
    }

    override fun getParameters(): ParametersDefinition = {
        parametersOf(requireArguments().getString(ARG_PAYMENT_URL))
    }

    override fun setStatusBarColor() {
        setStatusBarColor(R.color.primary400)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.paymentWebView.webChromeClient = WebChromeClient()

        val webClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                return if (request?.url.toString().startsWith("https://dom.moi-service.ru/api/billing/success")) {
                    ScreenManager.showScreenScope(PaymentErrorFragment.newInstance(), PAYMENT)
                    true
                } else {
                    super.shouldOverrideUrlLoading(view, request)
                }
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
            }

            override fun onReceivedError(
                view: WebView?,
                request: WebResourceRequest?,
                error: WebResourceError?
            ) {
                super.onReceivedError(view, request, error)
            }
        }

        binding.paymentWebView.settings.javaScriptEnabled = true
        binding.paymentWebView.webViewClient = webClient
        binding.paymentWebView.settings.loadWithOverviewMode = true
        binding.paymentWebView.settings.useWideViewPort = true
        binding.paymentWebView.settings.domStorageEnabled = true

        subscribe(viewModel.paymentUrlObserver) {
            binding.paymentWebView.loadUrl(it)
        }
    }

    override fun onClose() {
        if (binding.paymentWebView.canGoBack()){
            binding.paymentWebView.goBack()
        } else {
            super.onClose()
        }
    }
}