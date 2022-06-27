package com.custom.rgs_android_dom.ui.purchase.payments

import android.os.Bundle
import android.view.View
import android.webkit.*
import androidx.core.content.ContextCompat
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.databinding.FragmentPaymentWebviewBinding
import com.custom.rgs_android_dom.ui.base.BaseBottomSheetFragment
import com.custom.rgs_android_dom.ui.navigation.PAYMENT
import com.custom.rgs_android_dom.ui.navigation.ScreenManager
import com.custom.rgs_android_dom.ui.purchase.payments.error.PaymentErrorFragment
import com.custom.rgs_android_dom.ui.purchase.payments.success.PaymentSuccessFragment
import com.custom.rgs_android_dom.utils.*
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.parameter.parametersOf

class PaymentWebViewFragment : BaseBottomSheetFragment<PaymentWebViewViewModel, FragmentPaymentWebviewBinding>() {

    companion object {
        private const val ARG_PAYMENT_URL = "ARG_PAYMENT_URL"
        private const val ARG_PRODUCT_ID = "ARG_PRODUCT_ID"
        private const val ARG_PRODUCT_VERSION_ID = "ARG_PRODUCT_VERSION_ID"
        private const val ARG_EMAIL = "ARG_EMAIL"
        private const val ARG_ORDER_ID = "ARG_ORDER_ID"

        fun newInstance(
            url: String,
            productId: String,
            productVersionId: String?,
            email: String,
            orderId: String
        ): PaymentWebViewFragment {
            return PaymentWebViewFragment().args {
                putString(ARG_PAYMENT_URL, url)
                putString(ARG_PRODUCT_ID, productId)
                putString(ARG_PRODUCT_VERSION_ID, productVersionId)
                putString(ARG_EMAIL, email)
                putString(ARG_ORDER_ID, orderId)
            }
        }
    }

    override fun getParameters(): ParametersDefinition = {
        parametersOf(requireArguments().getString(ARG_PAYMENT_URL))
    }

    override val TAG = "PAYMENT_WEB_VIEW_FRAGMENT"

    override fun getThemeResource(): Int {
        return R.style.BottomSheetNoDim
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.closeImageView.setOnDebouncedClickListener {
            viewModel.onBack()
        }
        binding.paymentWebView.webChromeClient = WebChromeClient()

        val email = requireArguments().getString(ARG_EMAIL) ?: ""
        val productId = requireArguments().getString(ARG_PRODUCT_ID) ?: ""
        val productVersionId = requireArguments().getString(ARG_PRODUCT_VERSION_ID) ?: ""
        val orderId = requireArguments().getString(ARG_ORDER_ID, "")

        val webClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                return when {
                    request?.url.toString().contains("fail") -> {
                        viewModel.onBack()
                        ScreenManager.showScreenScope(
                            PaymentErrorFragment(),
                            PAYMENT
                        )
                        true
                    }
                    request?.url.toString().contains("success") -> {
                        viewModel.onBack()
                        ScreenManager.showScreenScope(
                            PaymentSuccessFragment.newInstance(
                                productId,
                                productVersionId,
                                email,
                                orderId
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
                try {
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
                } catch (e: Exception) {
                    logException(this, e)
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

    override fun isNavigationViewVisible(): Boolean {
        return false
    }
}