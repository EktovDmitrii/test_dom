package com.custom.rgs_android_dom.ui.policies

import android.os.Bundle
import android.view.View
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.databinding.FragmentPoliciesBinding
import com.custom.rgs_android_dom.ui.base.BaseFragment
import com.custom.rgs_android_dom.utils.*
import com.custom.rgs_android_dom.utils.gone

class PoliciesFragment :
    BaseFragment<PoliciesViewModel, FragmentPoliciesBinding>(R.layout.fragment_policies) {

    private val policiesAdapter: PoliciesAdapter
        get() = binding.dataAvailableStateLayout.recyclerView.adapter as PoliciesAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.dataAvailableStateLayout.recyclerView.adapter = PoliciesAdapter {
            viewModel.onPolicyClick(it)
        }

        binding.backImageView.setOnDebouncedClickListener {
            viewModel.onBackClick()
        }

        binding.addImageView.setOnDebouncedClickListener {
            viewModel.onAddClick()
        }

        binding.emptyStateLayout.bindTextView.setOnDebouncedClickListener {
            viewModel.onBindClick()
        }

        subscribe(viewModel.policiesObserver) {
            binding.emptyStateLayout.root.visibleIf(it.isEmpty())
            binding.dataAvailableStateLayout.root.visibleIf(it.isNotEmpty())
            policiesAdapter.setItems(it)
        }
    }
}