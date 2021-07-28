package com.custom.rgs_android_dom.ui.registration.fill_profile

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.databinding.FragmentRegistrationAgreementBinding
import com.custom.rgs_android_dom.databinding.FragmentRegistrationFillProfileBinding
import com.custom.rgs_android_dom.ui.base.BaseFragment
import com.custom.rgs_android_dom.ui.navigation.REGISTRATION
import com.custom.rgs_android_dom.ui.navigation.ScreenManager
import com.custom.rgs_android_dom.utils.*

class RegistrationFillProfileFragment : BaseFragment<RegistrationFillProfileViewModel, FragmentRegistrationFillProfileBinding>(
    R.layout.fragment_registration_fill_profile
) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.knowAgentCodeTextView.setOnDebouncedClickListener {
            viewModel.onKnowAgentCodeClick()
        }

        binding.skipTextView.setOnDebouncedClickListener {
            viewModel.onSkipClick()
        }

        binding.closeImageView.setOnDebouncedClickListener {
            viewModel.onCloseClick()
        }

        subscribe(viewModel.isAgentInfoLinearLayoutVisibleObserver){
            binding.agentInfoLinearLayout.isVisible = it
        }

        subscribe(viewModel.knowAgentCodeTextObserver){
            binding.knowAgentCodeTextView.text = it
        }
    }

    override fun onClose() {
        hideSoftwareKeyboard()
        //ScreenManager.closeScope(REGISTRATION)
        ScreenManager.back(getNavigateId())
    }

    override fun onLoading() {
        super.onLoading()
    }

    override fun onError() {
        super.onError()
    }

    override fun onContent() {
        super.onContent()
    }

}