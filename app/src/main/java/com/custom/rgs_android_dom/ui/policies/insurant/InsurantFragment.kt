package com.custom.rgs_android_dom.ui.policies.insurant

import android.os.Bundle
import android.view.View
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.databinding.FragmentInsurantBinding
import com.custom.rgs_android_dom.domain.client.exceptions.ClientField
import com.custom.rgs_android_dom.domain.policies.models.PolicyDialogModel
import com.custom.rgs_android_dom.ui.base.BaseFragment
import com.custom.rgs_android_dom.ui.policies.insurant.dialogs.PolicyDialogsFragment
import com.custom.rgs_android_dom.utils.*
import com.custom.rgs_android_dom.views.edit_text.MSDLabelIconEditText
import org.joda.time.LocalDateTime

class InsurantFragment : BaseFragment<InsurantViewModel, FragmentInsurantBinding>(R.layout.fragment_insurant) {

    companion object {

        private const val KEY_FRAGMENT_ID = "KEY_FRAGMENT_ID"

        fun newInstance(fragmentId: Int) = InsurantFragment().args {
            putInt(KEY_FRAGMENT_ID, fragmentId)
        }
    }

    private var shouldShowDialog = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.backImageView.setOnDebouncedClickListener {
            viewModel.onBackClick()
        }

        binding.nextTextView.setOnDebouncedClickListener {
            shouldShowDialog = true
            viewModel.onNextClick()
        }

        binding.firstNameEditText.addTextWatcher {
            viewModel.firstNameChanged(it , binding.birthdayEditText.isMaskFiled())
        }

        binding.lastNameEditText.addTextWatcher {
            viewModel.lastNameChanged(it, binding.birthdayEditText.isMaskFiled())
        }

        binding.middleNameEditText.addTextWatcher {
            viewModel.middleNameChanged(it, binding.birthdayEditText.isMaskFiled())
        }

        binding.birthdayEditText.addTextWatcher {
            binding.birthdayEditText.isMaskFiled()
            viewModel.birthdayChanged(it, binding.birthdayEditText.isMaskFiled())
            binding.birthdayEditText.setState(MSDLabelIconEditText.State.NORMAL)
        }

        binding.birthdayEditText.setOnIconClickListener {
            hideSoftwareKeyboard()
            showDatePicker(
                maxDate = LocalDateTime.now().minusYears(16).plusDays(-1).toDate(),
                minDate = LocalDateTime.parse("1900-01-01").toDate()
            ) {
                val date = it.formatTo()
                binding.birthdayEditText.setState(MSDLabelIconEditText.State.NORMAL)
                binding.birthdayEditText.setTextFromUser(date)
            }
        }

        subscribe(viewModel.insurantViewStateObserver) {
            binding.firstNameEditText.setText(it.firstName)
            binding.lastNameEditText.setText(it.lastName)
            binding.middleNameEditText.setText(it.middleName)
            binding.nextTextView.isEnabled = it.isNextEnabled
            if (it.isValidationPassed && shouldShowDialog) {
                shouldShowDialog = false
                val dialog = PolicyDialogsFragment.newInstance(
                    PolicyDialogModel(showLoader = true),
                    requireArguments().getInt(KEY_FRAGMENT_ID))
                dialog.show(childFragmentManager, dialog.TAG)
                hideSoftwareKeyboard()
            }
        }

        subscribe(viewModel.validateExceptionObserver) { specError ->
            specError.fields.forEach {
                if (it.fieldName == ClientField.BIRTHDATE) {
                    binding.birthdayEditText.setState( MSDLabelIconEditText.State.ERROR, it.errorMessage )
                }
            }
        }

    }


}