package com.custom.rgs_android_dom.ui.client.agent.edit

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.databinding.FragmentEditAgentBinding
import com.custom.rgs_android_dom.domain.client.exceptions.ClientField
import com.custom.rgs_android_dom.ui.base.BaseFragment
import com.custom.rgs_android_dom.utils.*
import com.custom.rgs_android_dom.views.edit_text.MSDLabelEditText
import com.custom.rgs_android_dom.views.edit_text.MSDMaskedLabelEditText

class EditAgentFragment : BaseFragment<EditAgentViewModel, FragmentEditAgentBinding>(R.layout.fragment_edit_agent) {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.backImageView.setOnDebouncedClickListener {
            viewModel.onBackClick()
        }

        binding.saveTextView.setOnDebouncedClickListener {
            hideSoftwareKeyboard()
            viewModel.onSaveClick()
        }

        binding.agentCodeEditText.addTextWatcher {
            viewModel.onAgentCodeChanged(it)
            binding.agentCodeEditText.setState(MSDLabelEditText.State.NORMAL)
        }

        binding.agentPhoneEditText.addOnTextChangedListener { phone, isMaskFilled ->
            viewModel.onAgentPhoneChanged(phone, isMaskFilled)
            binding.agentPhoneEditText.setState(MSDMaskedLabelEditText.State.NORMAL)
        }

        subscribe(viewModel.isSaveTextViewEnabledObserver){
            binding.saveTextView.isEnabled = it
        }

        subscribe(viewModel.validateExceptionObserver){ specError ->
            specError.fields.forEach {
                when (it.fieldName){
                    ClientField.AGENTCODE -> {
                        binding.agentCodeEditText.setState(MSDLabelEditText.State.ERROR, it.errorMessage)
                    }
                    ClientField.AGENTPHONE -> {
                        binding.agentPhoneEditText.setState(MSDMaskedLabelEditText.State.ERROR, it.errorMessage)
                    }
                }
            }
        }

        subscribe(viewModel.networkErrorObserver){
            toast(it)
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