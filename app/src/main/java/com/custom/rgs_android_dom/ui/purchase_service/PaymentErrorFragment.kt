package com.custom.rgs_android_dom.ui.purchase_service

import android.os.Bundle
import android.view.View
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.databinding.FragmentPaymentErrorBinding
import com.custom.rgs_android_dom.ui.base.BaseFragment
import com.custom.rgs_android_dom.ui.navigation.PAYMENT
import com.custom.rgs_android_dom.ui.navigation.ScreenManager
import com.custom.rgs_android_dom.utils.args
import com.custom.rgs_android_dom.utils.setOnDebouncedClickListener
import com.custom.rgs_android_dom.utils.setStatusBarColor

class PaymentErrorFragment :
    BaseFragment<PaymentErrorViewModel, FragmentPaymentErrorBinding>(R.layout.fragment_payment_error) {

    companion object {
        fun newInstance() = PaymentErrorFragment()
    }

    override fun setStatusBarColor() {
        setStatusBarColor(R.color.primary500_black_alpha40)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.orderBtn.btnTitle.text = "Заказать"
        binding.orderBtn.btnPrice.text = "1500 ₽"

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