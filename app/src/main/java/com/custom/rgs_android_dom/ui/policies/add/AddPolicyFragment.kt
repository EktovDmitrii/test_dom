package com.custom.rgs_android_dom.ui.policies.add

import android.os.Bundle
import android.view.View
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.databinding.FragmentAddPolicyBinding
import com.custom.rgs_android_dom.ui.base.BaseFragment
import com.custom.rgs_android_dom.ui.policies.add.info.InfoPolicyFragment
import com.custom.rgs_android_dom.ui.policies.insurant.InsurantFragment
import com.custom.rgs_android_dom.utils.setOnDebouncedClickListener
import com.custom.rgs_android_dom.utils.subscribe

class AddPolicyFragment :
    BaseFragment<AddPolicyViewModel, FragmentAddPolicyBinding>(R.layout.fragment_add_policy) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.backImageView.setOnDebouncedClickListener {
            viewModel.onBackClick()
        }

        binding.nextTextView.setOnDebouncedClickListener {
            viewModel.onNextClick()
        }

        binding.policyInput.onInfoClick = {
            val dialog = InfoPolicyFragment()
            dialog.show(childFragmentManager, dialog.TAG)
        }

        binding.policyInput.addTextWatcher {
            viewModel.policyChanged(it)
        }

        subscribe(viewModel.policyChangedObserver) {
            binding.nextTextView.isEnabled = it.isNotEmpty()
        }

        /*subscribe(viewModel.policyObserver) {
            when (it) {
                is PolicyDialogModel.FindPolicySuccess -> {
                    InsurantFragment()
                }
                is PolicyDialogModel.FindPolicyFailure -> {

                }
            }
        }*/

    }

}