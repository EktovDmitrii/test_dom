package com.custom.rgs_android_dom.ui.registration.fill_profile

import android.graphics.Rect
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.databinding.FragmentRegistrationFillProfileBinding
import com.custom.rgs_android_dom.ui.base.BaseFragment
import com.custom.rgs_android_dom.ui.navigation.ScreenManager
import com.custom.rgs_android_dom.utils.*
import com.custom.rgs_android_dom.views.edit_text.MSDLabelIconEditText
import com.custom.rgs_android_dom.views.edit_text.MSDMaskedLabelEditText
import org.joda.time.LocalDate
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.parameter.parametersOf

class RegistrationFillProfileFragment : BaseFragment<RegistrationFillProfileViewModel, FragmentRegistrationFillProfileBinding>(
    R.layout.fragment_registration_fill_profile
) {

    companion object {
        private const val ARG_PHONE = "ARG_PHONE"

        fun newInstance(phone: String): RegistrationFillProfileFragment {
            return RegistrationFillProfileFragment().args {
                putString(ARG_PHONE, phone)
            }
        }
    }

    override fun getParameters(): ParametersDefinition = {
        parametersOf(requireArguments().getString(ARG_PHONE))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.knowAgentCodeTextView.setOnDebouncedClickListener {
            viewModel.onKnowAgentCodeClick()
        }

        binding.skipTextView.setOnDebouncedClickListener {
            viewModel.onSkipClick()
        }

        binding.closeImageView.setOnDebouncedClickListener {
            viewModel.onCloseClick()
        }

        binding.saveTextView.setOnDebouncedClickListener {
            hideSoftwareKeyboard()
            viewModel.onSaveClick()
        }

        binding.birthdayEditText.setOnIconClickListener {
            showDatePicker(
                maxDate = LocalDate.now().minusYears(16).plusDays(1).toDate(),
                minDate = LocalDate.parse("1900-01-01").toDate()
            ){
                val date = it.formatTo()
                binding.birthdayEditText.setState(MSDLabelIconEditText.State.NORMAL)
                binding.birthdayEditText.setTextFromUser(date)
            }
        }

        binding.surnameEditText.addTextWatcher {
            viewModel.onSurnameChanged(it)
        }

        binding.nameEditText.addTextWatcher {
            viewModel.onNameChanged(it)
        }

        binding.genderSelector.setGenderSelectedListener {
            viewModel.onGenderSelected(it)
        }

        binding.birthdayEditText.addOnTextChangedListener { birthday, isMaskFilled ->
            binding.birthdayEditText.setState(MSDLabelIconEditText.State.NORMAL)
            viewModel.onBirthdayChanged(birthday, isMaskFilled)
        }

        binding.agentCodeEditText.addTextWatcher {
            viewModel.onAgentCodeChanged(it)
        }

        binding.agentPhoneEditText.addOnTextChangedListener { agentPhone, isMaskFilled ->
            binding.agentPhoneEditText.setState(MSDMaskedLabelEditText.State.NORMAL)
            viewModel.onAgentPhoneChanged(agentPhone, isMaskFilled)
        }

        binding.contentNestedScrollView.viewTreeObserver.addOnScrollChangedListener {
            val scrollBounds = Rect()
            binding.contentNestedScrollView.getHitRect(scrollBounds)
            CrossfadeAnimator.crossfade(binding.titlePrimaryTextView, binding.titleSecondaryTextView, scrollBounds)
        }

        subscribe(viewModel.isAgentInfoLinearLayoutVisibleObserver){
            binding.agentInfoLinearLayout.isVisible = it
        }

        subscribe(viewModel.knowAgentCodeTextObserver){
            binding.knowAgentCodeTextView.text = it
        }

        subscribe(viewModel.isSaveTextViewEnabledObserver){
            binding.saveTextView.isEnabled = it
        }

        subscribe(viewModel.birthdayErrorObserver){
            //binding.birthdayEditText.setState(MSDLabelIconEditText.State.ERROR, it)
        }

        subscribe(viewModel.agentPhoneErrorObserver){
            //binding.agentPhoneEditText.setState(MSDMaskedLabelEditText.State.ERROR, it)
        }
    }

    override fun onClose() {
        hideSoftwareKeyboard()
        //ScreenManager.closeScope(REGISTRATION)
        ScreenManager.back(getNavigateId())
    }

    override fun onLoading() {
        super.onLoading()
        binding.saveTextView.setLoading(true)
    }

    override fun onError() {
        super.onError()
        binding.saveTextView.setLoading(false)
        toast("Произошла ошибка")
    }

    override fun onContent() {
        super.onContent()
        binding.saveTextView.setLoading(false)
    }

}