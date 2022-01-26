package com.custom.rgs_android_dom.ui.purchase_service

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.databinding.FragmentPaymentErrorBinding
import com.custom.rgs_android_dom.ui.base.BaseFragment
import com.custom.rgs_android_dom.ui.navigation.PAYMENT
import com.custom.rgs_android_dom.ui.navigation.ScreenManager
import com.custom.rgs_android_dom.utils.args
import com.custom.rgs_android_dom.utils.setOnDebouncedClickListener
import com.custom.rgs_android_dom.utils.setStatusBarColor
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.parameter.parametersOf

class PaymentErrorFragment :
    BaseFragment<PaymentErrorViewModel, FragmentPaymentErrorBinding>(R.layout.fragment_payment_error) {

    companion object {
        private const val ARG_PRICE = "ARG_PRICE"
        private const val ARG_PURCHASE_PAGE_ID = "ARG_PURCHASE_PAGE_ID"

        fun newInstance( fragmentId: Int, price: String) = PaymentErrorFragment().args {
            putInt(ARG_PURCHASE_PAGE_ID, fragmentId)
            putString(ARG_PRICE, price)
        }
    }

    override fun getParameters(): ParametersDefinition = {
        parametersOf(
            requireArguments().getInt(ARG_PURCHASE_PAGE_ID),
            requireArguments().getString(ARG_PRICE)
        )
    }

    override fun setStatusBarColor() {
        setStatusBarColor(R.color.primary500_black_alpha40)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.orderBtn.btnTitle.text = "Заказать"
        binding.orderBtn.btnPrice.text = "${requireArguments().getString(ARG_PRICE)} ₽"

        binding.closeImageView.setOnDebouncedClickListener {
            viewModel.onCloseScope()
        }
        binding.orderBtn.root.setOnDebouncedClickListener {
            viewModel.navigatePurchase()
        }
        binding.cancelPaymentTextView.setOnDebouncedClickListener {
            viewModel.onCloseScope()
        }
    }

    override fun onClose() {
        ScreenManager.closeScope(PAYMENT)
    }

}