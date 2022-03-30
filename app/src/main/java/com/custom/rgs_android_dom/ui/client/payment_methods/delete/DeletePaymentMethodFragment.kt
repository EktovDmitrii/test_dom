package com.custom.rgs_android_dom.ui.client.payment_methods.delete

import android.os.Bundle
import android.util.Log
import android.view.View
import com.custom.rgs_android_dom.databinding.FragmentDeletePaymentMethodBinding
import com.custom.rgs_android_dom.ui.base.BaseBottomSheetModalFragment
import com.custom.rgs_android_dom.ui.client.payment_methods.error.ErrorDeletePaymentMethodFragment
import com.custom.rgs_android_dom.utils.args
import com.custom.rgs_android_dom.utils.setOnDebouncedClickListener
import com.custom.rgs_android_dom.utils.subscribe
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.parameter.parametersOf

class DeletePaymentMethodFragment() : BaseBottomSheetModalFragment<DeletePaymentMethodViewModel, FragmentDeletePaymentMethodBinding>() {

    override val TAG: String = "DELETE_PAYMENT_METHOD_BINDING"

    companion object {
        private const val ARG_BINDING_ID = "ARG_BINDING_ID"

        fun newInstance(bindingId: String): DeletePaymentMethodFragment {
            return DeletePaymentMethodFragment().args {
                putString(ARG_BINDING_ID, bindingId)
            }
        }

    }

    override fun getParameters(): ParametersDefinition = {
        parametersOf(requireArguments().getString(ARG_BINDING_ID))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.deleteTextView.setOnDebouncedClickListener {
            viewModel.onDeleteClick()
        }

        subscribe(viewModel.showErrorDialogObserver){
            Log.d("MyLog", "SHOW ERROR DIALOG " + it)
            dismissAllowingStateLoss()
            parentFragment?.let { parentFragment->
                val errorDialog = ErrorDeletePaymentMethodFragment.newInstance(it)
                errorDialog.show(parentFragment.childFragmentManager, errorDialog.TAG)
            }
        }
    }

}