package com.custom.rgs_android_dom.ui.purchase.payments

import android.os.Bundle
import android.view.View
import android.webkit.*
import androidx.core.content.ContextCompat
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.databinding.FragmentPaymentWebviewBinding
import com.custom.rgs_android_dom.ui.base.BaseFragment
import com.custom.rgs_android_dom.ui.navigation.PAYMENT
import com.custom.rgs_android_dom.ui.navigation.ScreenManager
import com.custom.rgs_android_dom.ui.purchase.payments.error.PaymentErrorFragment
import com.custom.rgs_android_dom.ui.purchase.payments.success.PaymentSuccessFragment
import com.custom.rgs_android_dom.utils.args
import com.custom.rgs_android_dom.utils.setOnDebouncedClickListener
import com.custom.rgs_android_dom.utils.setStatusBarColor
import com.custom.rgs_android_dom.utils.subscribe
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.parameter.parametersOf

class PaymentWebViewFragment :
    BaseFragment<PaymentWebViewViewModel, FragmentPaymentWebviewBinding>(R.layout.fragment_payment_webview) {

    companion object {
        private const val ARG_PAYMENT_URL = "ARG_PAYMENT_URL"
        private const val ARG_PRODUCT_ID = "ARG_PRODUCT_ID"
        private const val ARG_EMAIL = "ARG_EMAIL"
        private const val ARG_PRICE = "ARG_PRICE"
        private const val ARG_PURCHASE_PAGE_ID = "ARG_PURCHASE_PAGE_ID"

        fun newInstance(
            url: String,
            productId: String,
            email: String,
            price: String,
            fragmentId: Int = 0
        ): PaymentWebViewFragment {
            return PaymentWebViewFragment().args {
                putString(ARG_PAYMENT_URL, url)
                putString(ARG_PRODUCT_ID, productId)
                putString(ARG_EMAIL, email)
                putString(ARG_PRICE, price)
                putInt(ARG_PURCHASE_PAGE_ID, fragmentId)
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
        binding.closeImageView.setOnDebouncedClickListener {
            viewModel.onBack()
        }
        binding.paymentWebView.webChromeClient = WebChromeClient()

        val email = requireArguments().getString(ARG_EMAIL) ?: ""
        val productId = requireArguments().getString(ARG_PRODUCT_ID) ?: ""
        val price = requireArguments().getString(ARG_PRICE) ?: ""
        val fragmentId = requireArguments().getInt(ARG_PURCHASE_PAGE_ID)

        val webClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                return when {
                    request?.url.toString().contains("/billing/fail") -> {
                        ScreenManager.showScreenScope(
                            PaymentErrorFragment.newInstance(fragmentId, price),
                            PAYMENT
                        )
                        true
                    }
                    request?.url.toString().contains("/billing/success") -> {
                        ScreenManager.showScreenScope(
                            PaymentSuccessFragment.newInstance(
                                productId,
                                email
                            ), PAYMENT
                        )
                        true
                    }
                    else -> {
                        super.shouldOverrideUrlLoading(view, request)
                    }
                }
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                binding.layout.background = if (url?.contains("3dsec.sberbank.ru") == true) {
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.rectangle_filled_grey_top_radius_24dp
                    )
                } else {
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.rectangle_filled_white_top_radius_24dp
                    )
                }
                super.onPageFinished(view, url)
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
        if (binding.paymentWebView.canGoBack()) {
            binding.paymentWebView.goBack()
        } else {
            super.onClose()
        }
    }
}