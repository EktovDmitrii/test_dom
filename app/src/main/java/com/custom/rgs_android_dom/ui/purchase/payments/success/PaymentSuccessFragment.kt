package com.custom.rgs_android_dom.ui.purchase.payments.success

import android.os.Bundle
import android.view.View
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.databinding.FragmentPaymentSuccessBinding
import com.custom.rgs_android_dom.ui.base.BaseFragment
import com.custom.rgs_android_dom.ui.navigation.PAYMENT
import com.custom.rgs_android_dom.ui.navigation.ScreenManager
import com.custom.rgs_android_dom.utils.args
import com.custom.rgs_android_dom.utils.setOnDebouncedClickListener
import com.custom.rgs_android_dom.utils.setStatusBarColor
import com.custom.rgs_android_dom.utils.subscribe
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.parameter.parametersOf

class PaymentSuccessFragment :
    BaseFragment<PaymentSuccessViewModel, FragmentPaymentSuccessBinding>(R.layout.fragment_payment_success) {

    companion object {

        private const val ARG_PRODUCT_ID = "ARG_PRODUCT_ID"
        private const val ARG_PRODUCT_VERSION_ID = "ARG_PRODUCT_VERSION_D"
        private const val ARG_EMAIL = "ARG_EMAIL"
        private const val ARG_ORDER_ID = "ARG_ORDER_ID"

        fun newInstance(productId: String, productVersionId: String?, email: String, orderId: String) =
            PaymentSuccessFragment().args {
                putString(ARG_PRODUCT_ID, productId)
                putString(ARG_PRODUCT_VERSION_ID, productVersionId)
                putString(ARG_EMAIL, email)
                putString(ARG_ORDER_ID, orderId)
            }
    }

    override fun getParameters(): ParametersDefinition = {
        parametersOf(
            requireArguments().getString(ARG_PRODUCT_ID),
            requireArguments().getString(ARG_PRODUCT_VERSION_ID),
            requireArguments().getString(ARG_EMAIL),
            requireArguments().getString(ARG_ORDER_ID)
        )
    }

    override fun setStatusBarColor() {
        setStatusBarColor(R.color.primary500_black_alpha40)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.closeImageView.setOnDebouncedClickListener {
            viewModel.onCloseScope()
        }
        binding.moreAboutTextView.setOnDebouncedClickListener {
            viewModel.onMoreClick()
        }

        subscribe(viewModel.emailObserver) {
            binding.descriptionTextView.text = it
        }
    }

    override fun onClose() {
        ScreenManager.closeScope(PAYMENT)
    }
}