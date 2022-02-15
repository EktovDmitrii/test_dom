package com.custom.rgs_android_dom.ui.policies.insurant.dialogs

import android.os.Bundle
import android.view.View
import androidx.appcompat.content.res.AppCompatResources
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.databinding.FragmentPolicyDialogsBinding
import com.custom.rgs_android_dom.domain.policies.models.Failure
import com.custom.rgs_android_dom.ui.base.BaseBottomSheetFragment
import com.custom.rgs_android_dom.domain.policies.models.PolicyDialogModel
import com.custom.rgs_android_dom.domain.policies.models.ShowPromptModel
import com.custom.rgs_android_dom.ui.base.BaseBottomSheetModalFragment
import com.custom.rgs_android_dom.utils.*
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.parameter.parametersOf

class PolicyDialogsFragment :
    BaseBottomSheetModalFragment<PolicyDialogsViewModel, FragmentPolicyDialogsBinding>() {

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

        const val KEY_POLICY_DIALOG_MODEL = "KEY_POLICY_DIALOG_MODEL"
        const val KEY_FRAGMENT_ID = "KEY_FRAGMENT_ID"

        fun newInstance(model: PolicyDialogModel, fragmentId: Int): PolicyDialogsFragment {
            return PolicyDialogsFragment().args {
                putSerializable(KEY_POLICY_DIALOG_MODEL, model)
                putInt(KEY_FRAGMENT_ID, fragmentId)
            }
        }
    }

    override fun getParameters(): ParametersDefinition = {
        parametersOf(requireArguments().getSerializable(KEY_POLICY_DIALOG_MODEL) as PolicyDialogModel)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fragmentId = requireArguments().getInt(KEY_FRAGMENT_ID)

        binding.loadingLayout.closeImageView.setOnDebouncedClickListener {
            viewModel.onCloseClick()
        }

        binding.savePolicyLayout.saveTextView.setOnDebouncedClickListener {
            viewModel.onSaveClick()
        }

        binding.savePolicyLayout.cancelTextView.setOnDebouncedClickListener {
            viewModel.onCancelClick()
        }

        binding.savePolicyLayout.successTextView.setOnDebouncedClickListener {
            viewModel.onSuccessClick()
        }

        binding.bindPolicySuccessLayout.understandTextView.setOnDebouncedClickListener {
            viewModel.onUnderstandClick(fragmentId)
        }

        binding.bindPolicyFailureLayout.chatTextView.setOnDebouncedClickListener {
            viewModel.onChatClick()
        }

        binding.bindPolicyFailureLayout.changeDataTextView.setOnDebouncedClickListener {
            viewModel.onChangeDataClick()
        }

        binding.findPolicyFailureLayout.chatTextView.setOnDebouncedClickListener {
            viewModel.onChatClick()
        }

        binding.findPolicyFailureLayout.changeDataTextView.setOnDebouncedClickListener {
            viewModel.onChangeDataClick()
        }

        subscribe(viewModel.dialogModelObserver) {
            binding.loadingLayout.root.visibleIf(it.showLoader)
            binding.bindPolicySuccessLayout.root.visibleIf(it.bound != null)
            binding.bindPolicyFailureLayout.root.visibleIf(it.failureMessage != null)
            binding.savePolicyLayout.root.visibleIf(it.showPrompt != null)

            when {
                it.failureMessage != null -> {
                    binding.bindPolicyFailureLayout.errorMessageTextView.text =
                        when (it.failureMessage) {
                            Failure.NOT_FOUND -> MESSAGE_NOT_FOUND
                            Failure.BOUND_TO_YOUR_PROFILE -> MESSAGE_BOUND_TO_YOUR_PROFILE
                            Failure.BOUND_TO_ANOTHER_PROFILE -> MESSAGE_BOUND_TO_ANOTHER_PROFILE
                            Failure.DATA_NOT_MATCH -> MESSAGE_DATA_NOT_MATCH
                            Failure.EXPIRED -> MESSAGE_EXPIRED
                            else -> "Unspecified error"
                        }
                }
                it.bound != null -> {
                    //todo replace with translation value
                    binding.bindPolicySuccessLayout.durationTextView.text =
                        binding.bindPolicySuccessLayout.durationTextView.text.toString().insertDate(
                            it.bound.startsAt, it.bound.endsAt
                        )
                    binding.bindPolicySuccessLayout.descriptionTextView.text =
                        binding.bindPolicySuccessLayout.descriptionTextView.text.toString().insertDate(
                            it.bound.startsAt, it.bound.endsAt
                        )
                }
                it.showPrompt != null -> {
                    when (it.showPrompt) {
                        ShowPromptModel.Loading -> {
                            binding.savePolicyLayout.saveTextView.visible()
                            binding.savePolicyLayout.saveTextView.setLoading(true)
                            binding.savePolicyLayout.cancelTextView.visible()

                        }
                        ShowPromptModel.Content -> {
                            binding.savePolicyLayout.saveTextView.visible()
                            binding.savePolicyLayout.cancelTextView.visible()
                            binding.savePolicyLayout.saveTextView.setLoading(false)

                        }
                        ShowPromptModel.Done -> {
                            binding.savePolicyLayout.saveTextView.gone()
                            binding.savePolicyLayout.cancelTextView.gone()
                            binding.savePolicyLayout.successTextView.visible()
                            binding.savePolicyLayout.iconImageView.setImageDrawable(
                                AppCompatResources.getDrawable(
                                    requireActivity(),
                                    R.drawable.ic_success_question
                                )
                            )
                            binding.savePolicyLayout.titleTextView.text = "Данные успешно сохранены!"
                            binding.savePolicyLayout.descriptionTextView.text =
                                "Данные из полиса страхования сохранены в вашем профиле"

                        }
                    }
                }
            }
        }

    }

    override fun onClose() {
        dismissAllowingStateLoss()
    }

}