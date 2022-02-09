package com.custom.rgs_android_dom.ui.purchase.add.email

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.custom.rgs_android_dom.databinding.FragmentAddEmailBinding
import com.custom.rgs_android_dom.ui.base.BaseBottomSheetModalFragment
import com.custom.rgs_android_dom.utils.*
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.parameter.parametersOf
import java.io.Serializable

class AddEmailFragment : BaseBottomSheetModalFragment<AddEmailViewModel, FragmentAddEmailBinding>() {

    companion object {
        private const val ARG_EMAIL = "ARG_EMAIL"

        fun newInstance(email: String?): AddEmailFragment {
            return AddEmailFragment().args {
                putString(ARG_EMAIL, email)
            }
        }
    }

    override val TAG: String = "ADD_EMAIL_FRAGMENT"
    private var purchaseEmailListener: PurchaseEmailListener? = null

    override fun getParameters(): ParametersDefinition = {
        parametersOf(requireArguments().getString(ARG_EMAIL, ""))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        if (parentFragment is PurchaseEmailListener) {
            purchaseEmailListener = parentFragment as PurchaseEmailListener
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.saveButton.setOnDebouncedClickListener {
            purchaseEmailListener?.onSaveEmailClick(binding.emailTextInputLayout.getText().trim())
            onClose()
        }

        subscribe(viewModel.emailObserver){
            binding.emailTextInputLayout.setText(it)

            binding.emailTextInputLayout.setSelection(it.length)

            binding.emailTextInputLayout.addTextWatcher { email ->
                binding.saveButton.isEnabled = email.trim().isValidEmail()
            }
        }
    }
}

interface PurchaseEmailListener : Serializable {
    fun onSaveEmailClick(email: String)
}