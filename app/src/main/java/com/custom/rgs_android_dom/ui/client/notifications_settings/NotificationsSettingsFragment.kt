package com.custom.rgs_android_dom.ui.client.notifications_settings

import android.os.Bundle
import android.view.View
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.databinding.FragmentNotificationsSettingsBinding
import com.custom.rgs_android_dom.domain.client.models.NotificationChannelType
import com.custom.rgs_android_dom.ui.base.BaseFragment
import com.custom.rgs_android_dom.utils.gone
import com.custom.rgs_android_dom.utils.setOnDebouncedClickListener
import com.custom.rgs_android_dom.utils.subscribe
import com.custom.rgs_android_dom.utils.visible

class NotificationsSettingsFragment : BaseFragment<NotificationsSettingsViewModel, FragmentNotificationsSettingsBinding>(R.layout.fragment_notifications_settings) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.enableSMSSwitch.setOnCheckedChangeListener { view, isChecked ->
            if (view.isPressed){
                viewModel.onSMSNotificationChannelChecked(isChecked)
            }
        }

        binding.enablePushSwitch.setOnCheckedChangeListener { view, isChecked ->
            if (view.isPressed) {
                viewModel.onPushNotificationChannelChecked(isChecked)
            }
        }

        binding.backImageView.setOnDebouncedClickListener {
            viewModel.close()
        }

        subscribe(viewModel.notificationChannelsObserver) { channels->
            channels.find { it.type == NotificationChannelType.PUSH }?.let {
                binding.enablePushSwitch.isChecked = it.enabled
            }

            channels.find { it.type == NotificationChannelType.SMS }?.let {
                binding.enableSMSSwitch.isChecked = it.enabled
            }
        }
    }

    override fun onLoading() {
        super.onLoading()
        binding.toolbarAppBar.gone()
        binding.contentLinearLayout.gone()
        binding.loadingProgressBar.visible()
    }

    override fun onContent() {
        super.onContent()
        binding.toolbarAppBar.visible()
        binding.contentLinearLayout.visible()
        binding.loadingProgressBar.gone()
    }

}