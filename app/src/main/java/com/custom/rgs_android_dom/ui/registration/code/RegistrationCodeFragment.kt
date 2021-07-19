package com.custom.rgs_android_dom.ui.registration.code

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.custom.rgs_android_dom.databinding.FragmentRegistrationCodeBinding
import com.custom.rgs_android_dom.ui.base.BaseFragment
import com.custom.rgs_android_dom.utils.args
import com.custom.rgs_android_dom.utils.setOnDebouncedClickListener
import com.custom.rgs_android_dom.utils.subscribe
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.parameter.parametersOf

class RegistrationCodeFragment : BaseFragment<RegistrationCodeViewModel, FragmentRegistrationCodeBinding>(RegistrationCodeViewModel::class.java) {

    companion object {
        private const val ARG_PHONE = "ARG_PHONE"

        fun newInstance(phone: String): RegistrationCodeFragment {
            return RegistrationCodeFragment().args {
                putString(ARG_PHONE, phone)
            }
        }
    }

    override fun getParameters(): ParametersDefinition = {
        parametersOf(requireArguments().getString(ARG_PHONE))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.closeImageView.setOnDebouncedClickListener {
            viewModel.onCloseClick()
        }
        binding.codeInput.setOnCodeCompleteListener {
            viewModel.onCodeComplete(it)
        }

        subscribe(viewModel.isErrorVisibleObserver){
            if (it){
                binding.codeInput.setErrorState()
            } else {
                binding.codeInput.removeErrorState()
            }
        }
    }

    override fun getViewBinding(inflater: LayoutInflater,container: ViewGroup?): FragmentRegistrationCodeBinding {
        return FragmentRegistrationCodeBinding.inflate(inflater, container, false)
    }
}