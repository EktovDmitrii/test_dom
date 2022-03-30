package com.custom.rgs_android_dom.ui.client.payment_methods.error

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.custom.rgs_android_dom.databinding.FragmentErrorDeletePaymentMethodBinding
import com.custom.rgs_android_dom.ui.base.BaseBottomSheetModalFragment
import com.custom.rgs_android_dom.utils.*
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.parameter.parametersOf

class ErrorDeletePaymentMethodFragment : BaseBottomSheetModalFragment<ErrorDeletePaymentMethodViewModel, FragmentErrorDeletePaymentMethodBinding>() {

    companion object {
        private const val ARG_ERROR_CODE = "ARG_ERROR_CODE"

        fun newInstance(errorCode: String): ErrorDeletePaymentMethodFragment {
            return ErrorDeletePaymentMethodFragment().args {
                putString(ARG_ERROR_CODE, errorCode)
            }
        }
    }

    override val TAG: String = "ERROR_DELETE_PAYMENT_METHOD_FRAGMENT"

    private var listener: ErrorDeletePaymentMethodListener? = null

    override fun getParameters(): ParametersDefinition = {
        parametersOf(requireArguments().getString(ARG_ERROR_CODE))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (parentFragment is ErrorDeletePaymentMethodListener){
            listener = parentFragment as ErrorDeletePaymentMethodListener
        }
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tryAgainTextView.setOnDebouncedClickListener {
            viewModel.onTryAgainClick()
        }

        binding.contactOnlineMasterTextView.setOnDebouncedClickListener {
            viewModel.onContactOnlineMasterClick()
            listener?.contactMasterOnline()
        }

        subscribe(viewModel.errorCodeObserver){
            binding.errorCodeTextView.text = it
        }

    }

    interface ErrorDeletePaymentMethodListener {
        fun contactMasterOnline()
    }

}