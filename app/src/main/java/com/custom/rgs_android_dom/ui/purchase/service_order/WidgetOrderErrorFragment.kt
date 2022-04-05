package com.custom.rgs_android_dom.ui.purchase.service_order

import android.os.Bundle
import android.view.View
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.databinding.FragmentPaymentSuccessBinding
import com.custom.rgs_android_dom.databinding.FragmentWidgetOrderErrorBinding
import com.custom.rgs_android_dom.ui.base.BaseFragment
import com.custom.rgs_android_dom.ui.chats.chat.ChatFragment
import com.custom.rgs_android_dom.ui.navigation.PAYMENT
import com.custom.rgs_android_dom.ui.navigation.ScreenManager
import com.custom.rgs_android_dom.utils.args
import com.custom.rgs_android_dom.utils.setOnDebouncedClickListener
import com.custom.rgs_android_dom.utils.setStatusBarColor
import com.custom.rgs_android_dom.utils.subscribe
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.parameter.parametersOf

class WidgetOrderErrorFragment :
    BaseFragment<WidgetOrderErrorViewModel, FragmentWidgetOrderErrorBinding>(R.layout.fragment_widget_order_error) {

    override fun setStatusBarColor() {
        setStatusBarColor(R.color.primary500_black_alpha40)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.closeImageView.setOnDebouncedClickListener {
            viewModel.close()
        }
        binding.moreAboutTextView.setOnDebouncedClickListener {
            viewModel.close()
        }
        binding.contactOnlineMasterTextView.setOnDebouncedClickListener {
            viewModel.navigateChat()
        }
    }

}