package com.custom.rgs_android_dom.ui.policies.policy

import android.os.Bundle
import android.view.View
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.databinding.FragmentPolicyBinding
import com.custom.rgs_android_dom.ui.base.BaseFragment
import com.custom.rgs_android_dom.utils.args
import com.custom.rgs_android_dom.utils.setOnDebouncedClickListener
import com.custom.rgs_android_dom.utils.subscribe
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.parameter.parametersOf

class PolicyFragment : BaseFragment<PolicyViewModel, FragmentPolicyBinding>(R.layout.fragment_policy) {

    companion object {

        const val KEY_CONTRACT_ID = "KEY_CONTRACT_ID"
/*
        const val KEY_POLICIES_FRAGMENT_ID = "KEY_POLICIES_FRAGMENT_ID"
*/

        fun newInstance(contractId: String) = PolicyFragment().args {
            putString(KEY_CONTRACT_ID, contractId)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding.backImageView.setOnDebouncedClickListener {
            viewModel.onBackClick()
        }

        subscribe(viewModel.productObserver) {
            binding.productImageView
            binding.titleTextView.text = it.productTitle
            //binding.addressTextView.text = it.
            binding.descriptionTextView.text = it.productDescription
            //binding.clientNameTextView.text = it.
        }

    }

    override fun getParameters(): ParametersDefinition = {
        parametersOf(
            requireArguments().getString(KEY_CONTRACT_ID, null)
        )
    }

}