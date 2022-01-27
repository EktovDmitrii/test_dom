package com.custom.rgs_android_dom.ui.policies.insurant.dialogs

import android.os.Bundle
import android.view.View
import by.kirich1409.viewbindingdelegate.internal.getRootView
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.databinding.FragmentPolicyDialogsBinding
import com.custom.rgs_android_dom.domain.catalog.models.CatalogSubCategoryModel
import com.custom.rgs_android_dom.ui.base.BaseBottomSheetFragment
import com.custom.rgs_android_dom.ui.catalog.subcategory.CatalogSubcategoryFragment
import com.custom.rgs_android_dom.ui.policies.add.PolicyDialogModel
import com.custom.rgs_android_dom.ui.policies.insurant.InsurantFragment
import com.custom.rgs_android_dom.utils.*
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.parameter.parametersOf

class PolicyDialogsFragment :
    BaseBottomSheetFragment<PolicyDialogsViewModel, FragmentPolicyDialogsBinding>() {

    override val TAG: String = "POLICY_DIALOGS_FRAGMENT"

    companion object {

        const val KEY_POLICY_DIALOG = "KEY_POLICY_DIALOG_MESSAGE"

        fun newInstance(model: PolicyDialogModel): PolicyDialogsFragment {
            return PolicyDialogsFragment().args {
                putSerializable(KEY_POLICY_DIALOG, model)
            }
        }
    }

    override fun getParameters(): ParametersDefinition = {
        parametersOf(requireArguments().getString(KEY_POLICY_DIALOG))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val layouts = listOf<Any>(
            binding.bindingPolicyFailureLayout,
            binding.bindingPolicySuccessLayout,
            binding.savePolicyLayout,
            binding.findPolicyFailureLayout,
            binding.loadingLayout
        )

        binding.loadingLayout.closeImageView.setOnDebouncedClickListener {
            viewModel.onCloseClick()
        }

        binding.savePolicyLayout.saveTextView.setOnDebouncedClickListener {
            viewModel.onSavePolicyClick()
        }

        binding.savePolicyLayout.cancelTextView.setOnDebouncedClickListener {
            viewModel.onCancelClick()
        }

        binding.bindingPolicySuccessLayout.understandTextView.setOnDebouncedClickListener {
            viewModel.onUnderstandClick()
        }

        binding.bindingPolicyFailureLayout.chatTextView.setOnDebouncedClickListener {
            viewModel.onChatClick()
        }

        binding.bindingPolicyFailureLayout.changeDataTextView.setOnDebouncedClickListener {
            viewModel.onChangeDataClick()
        }

        binding.findPolicyFailureLayout.chatTextView.setOnDebouncedClickListener {
            viewModel.onChatClick()
        }

        binding.findPolicyFailureLayout.changeDataTextView.setOnDebouncedClickListener {
            viewModel.onChangeDataClick()
        }

        subscribe(viewModel.dialogModelObserver) {
            when (it) {
                is PolicyDialogModel.FindPolicySuccess -> InsurantFragment()
                is PolicyDialogModel.Loading -> {
                    binding.loadingLayout.root.visible()
                    layouts.filter { it != binding.loadingLayout }.forEach{ (it as FragmentPolicyDialogsBinding).root.gone() }
                }
                is  PolicyDialogModel.FindPolicyFailure -> {
                    binding.findPolicyFailureLayout.root.visible()
                    layouts.filter { it != binding.findPolicyFailureLayout }.forEach{ (it as FragmentPolicyDialogsBinding).root.gone() }
                }
            }
        }

    }


    override fun onClose() {
        dismissAllowingStateLoss()
    }

    override fun getThemeResource(): Int {
        return R.style.BottomSheet
    }

}
