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

        fun newInstance(price: String) = PaymentErrorFragment().args {
            bundleOf(ARG_PRICE to price)
        }
    }

    override fun getParameters(): ParametersDefinition = {
        parametersOf(requireArguments().getString(ARG_PRICE))
    }

    override fun setStatusBarColor() {
        setStatusBarColor(R.color.primary500_black_alpha40)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.orderBtn.btnTitle.text = "Заказать"
        binding.orderBtn.btnPrice.text = requireArguments().getString(ARG_PRICE)

        binding.closeImageView.setOnDebouncedClickListener {
            ScreenManager.closeScope(PAYMENT)
        }
        binding.orderBtn.root.setOnDebouncedClickListener {

        }
        binding.cancelPaymentTextView.setOnDebouncedClickListener {
            ScreenManager.closeScope(PAYMENT)
        }
    }

    override fun onClose() { }

}