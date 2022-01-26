package com.custom.rgs_android_dom.ui.purchase_service

import android.os.Bundle
import android.view.View
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.databinding.FragmentPaymentSuccessBinding
import com.custom.rgs_android_dom.ui.base.BaseFragment
import com.custom.rgs_android_dom.ui.navigation.PAYMENT
import com.custom.rgs_android_dom.ui.navigation.ScreenManager
import com.custom.rgs_android_dom.utils.setOnDebouncedClickListener
import com.custom.rgs_android_dom.utils.setStatusBarColor

class PaymentSuccessFragment :
    BaseFragment<PaymentSuccessViewModel, FragmentPaymentSuccessBinding>(R.layout.fragment_payment_success) {

    companion object {
        fun newInstance() = PaymentSuccessFragment()
    }

    override fun setStatusBarColor() {
        setStatusBarColor(R.color.primary500_black_alpha40)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.closeImageView.setOnDebouncedClickListener {
            ScreenManager.closeScope(PAYMENT)
        }
        binding.moreAboutTextView.setOnDebouncedClickListener {}
    }

}