package com.custom.rgs_android_dom.ui.policies.add

import android.os.Bundle
import android.view.View
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.databinding.FragmentAddPolicyBinding
import com.custom.rgs_android_dom.ui.base.BaseFragment
import com.custom.rgs_android_dom.ui.policies.add.info.InfoPolicyFragment
import com.custom.rgs_android_dom.utils.args
import com.custom.rgs_android_dom.utils.hideSoftwareKeyboard
import com.custom.rgs_android_dom.utils.setOnDebouncedClickListener
import com.custom.rgs_android_dom.utils.subscribe

class AddPolicyFragment :
    BaseFragment<AddPolicyViewModel, FragmentAddPolicyBinding>(R.layout.fragment_add_policy) {

    companion object {

        private const val KEY_POLICIES_FRAGMENT_ID = "KEY_POLICIES_FRAGMENT_ID"

        fun newInstance(fragmentId: Int) = AddPolicyFragment().args {
            putInt(KEY_POLICIES_FRAGMENT_ID, fragmentId)
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fragmentId = requireArguments().getInt(KEY_POLICIES_FRAGMENT_ID)

        binding.backImageView.setOnDebouncedClickListener {
            viewModel.onBackClick()
        }

        binding.nextTextView.setOnDebouncedClickListener {
            viewModel.onNextClick(fragmentId)
            hideSoftwareKeyboard()
        }

        binding.policyInput.onInfoClick = {
            hideSoftwareKeyboard()
            val dialog = InfoPolicyFragment()
            dialog.show(childFragmentManager, dialog.TAG)
        }

        binding.policyInput.addTextWatcher {
            viewModel.policyChanged(it)
        }

        subscribe(viewModel.policyChangedObserver) {
            binding.nextTextView.isEnabled = it.isNotEmpty()
        }

    }

}