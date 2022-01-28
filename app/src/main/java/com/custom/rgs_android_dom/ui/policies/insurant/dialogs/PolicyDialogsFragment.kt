package com.custom.rgs_android_dom.ui.policies.insurant.dialogs

import android.os.Bundle
import android.util.Log
import android.view.View
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.databinding.FragmentPolicyDialogsBinding
import com.custom.rgs_android_dom.domain.policies.models.Failure
import com.custom.rgs_android_dom.ui.base.BaseBottomSheetFragment
import com.custom.rgs_android_dom.domain.policies.models.PolicyDialogModel
import com.custom.rgs_android_dom.utils.*
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.parameter.parametersOf

class PolicyDialogsFragment :
    BaseBottomSheetFragment<PolicyDialogsViewModel, FragmentPolicyDialogsBinding>() {

    override val TAG: String = "POLICY_DIALOGS_FRAGMENT"

    companion object {

        const val MESSAGE_NOT_FOUND =
            "К сожалению, мы не смогли найти указанный полис. Пожалуйста, обратитесь к Мастеру онлайн"
        const val MESSAGE_BOUND_TO_YOUR_PROFILE =
            "Данный полис уже привязан к вашему профилю. Пожалуйста, обратитесь к Мастеру онлайн"
        const val MESSAGE_BOUND_TO_ANOTHER_PROFILE =
            "Данный полис уже привязан к другому профилю. Пожалуйста, обратитесь к Мастеру онлайн"
        const val MESSAGE_DATA_NOT_MATCH =
            "Введенные данные страхователя не соответствуют полису. Пожалуйста, обратитесь к Мастеру онлайн"
        const val MESSAGE_EXPIRED = "У данного полиса закончился срок действия. Пожалуйста, обратитесь к Мастеру онлайн"
        const val MESSAGE_YET_NOT_DUE =
            "У данного полиса еще не наступил срок действия. Пожалуйста, обратитесь к Мастеру онлайн"

        const val KEY_POLICY_DIALOG_MODEL = "KEY_POLICY_DIALOG_MODEL"

        fun newInstance(model: PolicyDialogModel): PolicyDialogsFragment {
            return PolicyDialogsFragment().args {
                putSerializable(KEY_POLICY_DIALOG_MODEL, model)
            }
        }
    }

    override fun getParameters(): ParametersDefinition = {
        parametersOf(requireArguments().getSerializable(KEY_POLICY_DIALOG_MODEL) as PolicyDialogModel)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
            Log.d("Syrgashev", "PolicyDialogModel: $it")
            when {
                it.showLoader -> {
                    binding.loadingLayout.root.visible()
                }
                it.failureMessage != null -> {
                    binding.bindingPolicyFailureLayout.root.visible()
                    binding.bindingPolicyFailureLayout.errorMessageTextView.text =
                        when (it.failureMessage) {
                            Failure.NOT_FOUND -> MESSAGE_NOT_FOUND
                            Failure.BOUND_TO_YOUR_PROFILE -> MESSAGE_BOUND_TO_YOUR_PROFILE
                            Failure.BOUND_TO_ANOTHER_PROFILE -> MESSAGE_BOUND_TO_ANOTHER_PROFILE
                            Failure.DATA_NOT_MATCH -> MESSAGE_DATA_NOT_MATCH
                            Failure.EXPIRED -> MESSAGE_EXPIRED
                            Failure.YET_NOT_DUE -> MESSAGE_YET_NOT_DUE
                        }
                }
                it.bound == true -> {
                    binding.bindingPolicySuccessLayout.root.visible()
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
