package com.custom.rgs_android_dom.ui.client.personal_data.edit

import android.os.Bundle
import android.util.Log
import android.view.View
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.databinding.FragmentEditPersonalDataBinding
import com.custom.rgs_android_dom.domain.client.exceptions.ClientField
import com.custom.rgs_android_dom.ui.base.BaseFragment
import com.custom.rgs_android_dom.ui.client.personal_data.request_edit.RequestEditPersonalDataFragment
import com.custom.rgs_android_dom.utils.*
import com.custom.rgs_android_dom.views.edit_text.MSDLabelEditText
import com.custom.rgs_android_dom.views.edit_text.MSDLabelIconEditText
import com.custom.rgs_android_dom.views.edit_text.MSDMaskedLabelEditText
import org.joda.time.LocalDateTime

class EditPersonalDataFragment :
    BaseFragment<EditPersonalDataViewModel, FragmentEditPersonalDataBinding>(R.layout.fragment_edit_personal_data) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.lastNameEditText.addTextWatcher {
            viewModel.onLastNameChanged(it)
            binding.lastNameEditText.setState(MSDLabelEditText.State.NORMAL)
        }

        binding.firstNameEditText.addTextWatcher {
            viewModel.onFirstNameChanged(it)
            binding.firstNameEditText.setState(MSDLabelEditText.State.NORMAL)
        }

        binding.middleNameEditText.addTextWatcher {
            viewModel.onMiddleNameChanged(it)
            binding.middleNameEditText.setState(MSDLabelEditText.State.NORMAL)
        }

        binding.birthdayEditText.addOnTextChangedListener { birthday, _ ->
            viewModel.onBirthdayChanged(birthday)
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

        binding.genderSelector.setGenderSelectedListener {
            viewModel.onGenderChanged(it)
        }

        binding.passportSeriesEditText.addTextWatcher {
            viewModel.onDocSerialChanged(it)
            binding.passportSeriesEditText.setState(MSDLabelEditText.State.NORMAL)
        }

        binding.passportNumberEditText.addTextWatcher {
            viewModel.onDocNumberChanged(it)
            binding.passportNumberEditText.setState(MSDLabelEditText.State.NORMAL)
        }

        binding.phoneEditText.addTextWatcher {
            viewModel.onPhoneChanged(it)
        }

        binding.additionalPhoneEditText.addOnTextChangedListener { phone, isMaskFilled ->
            viewModel.onSecondPhoneChanged(phone, isMaskFilled)
            binding.additionalPhoneEditText.setState(MSDMaskedLabelEditText.State.NORMAL)
        }

        binding.emailEditText.addTextWatcher {
            viewModel.onEmailChanged(it)
            binding.emailEditText.setState(MSDMaskedLabelEditText.State.NORMAL)
        }

        binding.saveTextView.setOnDebouncedClickListener {
            hideSoftwareKeyboard()
            viewModel.onSaveClick()
        }

        binding.backImageView.setOnDebouncedClickListener {
            viewModel.onBackClick()
        }

        binding.editRequestTextView.setOnDebouncedClickListener {
            val requestEditPersonalDataFragment = RequestEditPersonalDataFragment()
            requestEditPersonalDataFragment.show(
                childFragmentManager,
                requestEditPersonalDataFragment.TAG
            )
        }

        subscribe(viewModel.editPersonalDataObserver) { state ->
            if (state.hasProducts){
                binding.lastNameEditText.isEnabled = !state.isLastNameSaved
                binding.firstNameEditText.isEnabled = !state.isFirstNameSaved
                binding.middleNameEditText.isEnabled = !state.isMiddleNameSaved
                binding.birthdayEditText.isEnabled = !state.isBirthdaySaved
                binding.genderSelector.isEnabled = !state.isGenderSaved
                binding.passportSeriesEditText.isEnabled = !state.isDocSerialSaved
                binding.passportNumberEditText.isEnabled = !state.isDocNumberSaved
            }

            binding.lastNameEditText.setText(state.lastName)
            binding.firstNameEditText.setText(state.firstName)
            binding.middleNameEditText.setText(state.middleName)
            binding.birthdayEditText.setText(state.birthday)
            state.gender?.let { gender ->
                binding.genderSelector.setSelectedGender(gender)
            }
            binding.passportSeriesEditText.setText(state.docSerial)
            binding.passportNumberEditText.setText(state.docNumber)
            binding.phoneEditText.isEnabled = !state.isPhoneSaved
            binding.phoneEditText.setText(state.phone)

            if (state.secondPhone.isNotEmpty()){
                binding.additionalPhoneEditText.setText(state.secondPhone)
            }

            binding.emailEditText.setText(state.email)
        }

        subscribe(viewModel.isSaveTextViewEnabledObserver) {
            binding.saveTextView.isEnabled = it
        }

        subscribe(viewModel.validateExceptionObserver) { specError ->
            specError.fields.forEach {
                when (it.fieldName) {
                    ClientField.LASTNAME -> {
                        binding.lastNameEditText.setState(
                            MSDLabelEditText.State.ERROR,
                            it.errorMessage
                        )
                    }
                    ClientField.FIRSTNAME -> {
                        binding.firstNameEditText.setState(
                            MSDLabelEditText.State.ERROR,
                            it.errorMessage
                        )
                    }
                    ClientField.MIDDLENAME -> {
                        binding.middleNameEditText.setState(
                            MSDLabelEditText.State.ERROR,
                            it.errorMessage
                        )
                    }
                    ClientField.BIRTHDATE -> {
                        binding.birthdayEditText.setState(
                            MSDLabelIconEditText.State.ERROR,
                            it.errorMessage
                        )
                    }
                    ClientField.DOC_SERIAL -> {
                        binding.passportSeriesEditText.setState(
                            MSDLabelEditText.State.ERROR,
                            it.errorMessage
                        )
                    }
                    ClientField.DOC_NUMBER -> {
                        binding.passportNumberEditText.setState(
                            MSDLabelEditText.State.ERROR,
                            it.errorMessage
                        )
                    }
                    ClientField.SECOND_PHONE -> {
                        binding.additionalPhoneEditText.setState(
                            MSDMaskedLabelEditText.State.ERROR,
                            it.errorMessage
                        )
                    }
                    ClientField.EMAIL -> {
                        binding.emailEditText.setState(
                            MSDMaskedLabelEditText.State.ERROR,
                            it.errorMessage
                        )
                    }
                }
            }

        }

        subscribe(viewModel.networkErrorObserver) {
            toast(it)
        }

        subscribe(viewModel.editPersonalDataRequestedObserver) { wasRequested ->
            binding.editRequestTextView.goneIf(wasRequested)
            binding.descriptionTextView.text = if (wasRequested) {
                "Заявка на редактирование данных будет рассмотрена"
            } else {
                "Чтобы редактировать личные данные,"
            }
        }
    }

    override fun onLoading() {
        super.onLoading()
        binding.saveTextView.setLoading(true)
    }

    override fun onContent() {
        super.onContent()
        binding.saveTextView.setLoading(false)
    }

    override fun onError() {
        super.onError()
        binding.saveTextView.setLoading(false)
    }
}