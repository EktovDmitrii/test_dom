package com.custom.rgs_android_dom.ui.purchase.payments.error

import android.os.Bundle
import android.view.View
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.databinding.FragmentPaymentErrorBinding
import com.custom.rgs_android_dom.ui.base.BaseFragment
import com.custom.rgs_android_dom.ui.navigation.PAYMENT
import com.custom.rgs_android_dom.ui.navigation.ScreenManager
import com.custom.rgs_android_dom.utils.setOnDebouncedClickListener
import com.custom.rgs_android_dom.utils.setStatusBarColor

class PaymentErrorFragment :
    BaseFragment<PaymentErrorViewModel, FragmentPaymentErrorBinding>(R.layout.fragment_payment_error) {

    override fun setStatusBarColor() {
        setStatusBarColor(R.color.primary500_black_alpha40)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.closeImageView.setOnDebouncedClickListener {
            viewModel.onCloseScope()
        }
        binding.writeChatMaster.setOnDebouncedClickListener {
            viewModel.navigateChat()
        }
        binding.cancelPaymentTextView.setOnDebouncedClickListener {
            viewModel.navigatePurchase()
        }
    }

    override fun onClose() {
        ScreenManager.closeScope(PAYMENT)
    }

}