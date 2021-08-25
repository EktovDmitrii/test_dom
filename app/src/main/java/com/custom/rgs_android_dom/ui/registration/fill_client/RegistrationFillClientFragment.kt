package com.custom.rgs_android_dom.ui.registration.fill_client

import android.graphics.Rect
import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import androidx.core.view.isVisible
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.databinding.FragmentRegistrationFillClientBinding
import com.custom.rgs_android_dom.domain.client.ProfileField
import com.custom.rgs_android_dom.ui.base.BaseFragment
import com.custom.rgs_android_dom.ui.main.MainFragment
import com.custom.rgs_android_dom.ui.navigation.REGISTRATION
import com.custom.rgs_android_dom.ui.navigation.ScreenManager
import com.custom.rgs_android_dom.utils.*
import com.custom.rgs_android_dom.views.edit_text.MSDLabelEditText
import com.custom.rgs_android_dom.views.edit_text.MSDLabelIconEditText
import com.custom.rgs_android_dom.views.edit_text.MSDMaskedLabelEditText
import org.joda.time.LocalDateTime
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.parameter.parametersOf

class RegistrationFillClientFragment : BaseFragment<RegistrationFillClientViewModel, FragmentRegistrationFillClientBinding>(
    R.layout.fragment_registration_fill_client
) {

    companion object {
        private const val ARG_PHONE = "ARG_PHONE"

        fun newInstance(phone: String): RegistrationFillClientFragment {
            return RegistrationFillClientFragment().args {
                putString(ARG_PHONE, phone)
            }
        }
    }

    private var processedUpdateProfile = false

    private val scrollChangedListener = ViewTreeObserver.OnScrollChangedListener {
        val scrollBounds = Rect()
        binding.contentNestedScrollView.getHitRect(scrollBounds)
        CrossfadeAnimator.crossfade(binding.titlePrimaryTextView, binding.titleSecondaryTextView, scrollBounds)
    }

    override fun getParameters(): ParametersDefinition = {
        parametersOf(requireArguments().getString(ARG_PHONE))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.init()

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
                maxDate = LocalDateTime.now().minusYears(16).plusDays(-1).toDate(),
                minDate = LocalDateTime.parse("1900-01-01").toDate()
            ){
                val date = it.formatTo()
                binding.birthdayEditText.setState(MSDLabelIconEditText.State.NORMAL)
                binding.birthdayEditText.setTextFromUser(date)
            }
        }

        binding.surnameEditText.addTextWatcher {
            viewModel.onSurnameChanged(it)
            binding.surnameEditText.setState(MSDLabelEditText.State.NORMAL)
        }

        binding.nameEditText.addTextWatcher {
            viewModel.onNameChanged(it)
            binding.nameEditText.setState(MSDLabelEditText.State.NORMAL)
        }

        binding.genderSelector.setGenderSelectedListener {
            viewModel.onGenderSelected(it)
        }

        binding.birthdayEditText.addOnTextChangedListener { birthday, isMaskFilled ->
            viewModel.onBirthdayChanged(birthday, isMaskFilled)
            binding.birthdayEditText.setState(MSDLabelIconEditText.State.NORMAL)
        }

        binding.agentCodeEditText.addTextWatcher {agentCode->
            viewModel.onAgentCodeChanged(agentCode)
            binding.agentCodeEditText.setState(MSDLabelEditText.State.NORMAL)
        }

        binding.agentPhoneEditText.addOnTextChangedListener { agentPhone, isMaskFilled ->
            viewModel.onAgentPhoneChanged(agentPhone, isMaskFilled)
            binding.agentPhoneEditText.setState(MSDMaskedLabelEditText.State.NORMAL)
        }

        subscribe(viewModel.fillClientViewStateObserver){
            binding.agentInfoLinearLayout.isVisible = it.isOpenCodeAgendFields

            val knowAgentCodeText = if (it.isOpenCodeAgendFields) "Свернуть информацию об агенте"
            else "Знаю код агента"
            binding.knowAgentCodeTextView.text = knowAgentCodeText

            binding.agentCodeEditText.setText(it.agentCode ?: "")
            if (it.agentCode.isNullOrEmpty()){
                binding.agentCodeEditText.setState(MSDLabelEditText.State.NORMAL)
            }

            binding.nameEditText.setText(it.name ?: "")
            if (it.name.isNullOrEmpty()){
                binding.nameEditText.setState(MSDLabelEditText.State.NORMAL)
            }

            binding.surnameEditText.setText(it.surname ?: "")
            if (it.surname.isNullOrEmpty()){
                binding.surnameEditText.setState(MSDLabelEditText.State.NORMAL)
            }

            binding.birthdayEditText.setText(it.birthday?.formatTo() ?: "")
            if (it.birthday == null){
                binding.birthdayEditText.setState(MSDLabelIconEditText.State.NORMAL)
            }
        }

        subscribe(viewModel.isSaveTextViewEnabledObserver){
            binding.saveTextView.isEnabled = it
        }

        subscribe(viewModel.validateExceptionObserver){
            when(it.field){
                ProfileField.BIRTHDATE -> binding.birthdayEditText.setState(MSDLabelIconEditText.State.ERROR)
                ProfileField.NAME -> binding.nameEditText.setState(MSDLabelEditText.State.ERROR)
                ProfileField.SURNAME -> binding.surnameEditText.setState(MSDLabelEditText.State.ERROR)
                ProfileField.AGENTCODE -> binding.agentCodeEditText.setState(MSDLabelEditText.State.ERROR)
                ProfileField.AGENTPHONE -> binding.agentPhoneEditText.setState(MSDMaskedLabelEditText.State.ERROR)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        binding.contentNestedScrollView.viewTreeObserver.addOnScrollChangedListener(scrollChangedListener)
    }

    override fun onStop() {
        super.onStop()
        binding.contentNestedScrollView.viewTreeObserver.removeOnScrollChangedListener(scrollChangedListener)
    }

    override fun onClose() {
        hideSoftwareKeyboard()
        ScreenManager.closeScope(REGISTRATION)
        ScreenManager.showScreen(MainFragment())
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