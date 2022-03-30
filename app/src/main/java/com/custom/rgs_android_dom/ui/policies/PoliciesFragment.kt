package com.custom.rgs_android_dom.ui.policies

import android.os.Bundle
import android.view.View
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.databinding.FragmentPoliciesBinding
import com.custom.rgs_android_dom.domain.policies.models.PolicyDialogModel
import com.custom.rgs_android_dom.domain.policies.models.ShowPromptModel
import com.custom.rgs_android_dom.ui.base.BaseFragment
import com.custom.rgs_android_dom.ui.policies.insurant.dialogs.PolicyDialogsFragment
import com.custom.rgs_android_dom.utils.*

class PoliciesFragment :
    BaseFragment<PoliciesViewModel, FragmentPoliciesBinding>(R.layout.fragment_policies) {

    private val policiesAdapter: PoliciesAdapter
        get() = binding.dataStateLayout.recyclerView.adapter as PoliciesAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.dataStateLayout.recyclerView.adapter = PoliciesAdapter { id, isActive ->
            viewModel.onPolicyClick(id, isActive)
        }

        binding.backImageView.setOnDebouncedClickListener {
            viewModel.onBackClick()
        }

        binding.addImageView.setOnDebouncedClickListener {
            viewModel.onAddClick(getNavigateId())
        }

        binding.emptyStateLayout.bindTextView.setOnDebouncedClickListener {
            viewModel.onBindClick(getNavigateId())
        }

        subscribe(viewModel.policiesObserver) {
            binding.emptyStateLayout.root.visibleIf(it.isNullOrEmpty())
            binding.dataStateLayout.root.visibleIf(it.isNotEmpty())
            policiesAdapter.setItems(it)
        }

        subscribe(viewModel.promptSaveObserver) {
            if (it) {
                val dialog = PolicyDialogsFragment.newInstance(PolicyDialogModel(showPrompt = ShowPromptModel.Content), getNavigateId())
                dialog.show(childFragmentManager, dialog.TAG)
            }
        }

    }
}