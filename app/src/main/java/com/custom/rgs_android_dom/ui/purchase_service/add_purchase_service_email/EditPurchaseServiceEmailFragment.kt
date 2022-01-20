package com.custom.rgs_android_dom.ui.purchase_service.add_purchase_service_email

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.databinding.FragmentEditPurchaseServiceEmailBinding
import com.custom.rgs_android_dom.utils.setOnDebouncedClickListener
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.io.Serializable

class EditPurchaseServiceEmailFragment : BottomSheetDialogFragment() {

    private val binding: FragmentEditPurchaseServiceEmailBinding by viewBinding(createMethod = CreateMethod.INFLATE)

    private var currentEmail: String? = null

    private var editPurchaseServiceEmailListener: EditPurchaseServiceEmailListener? = null


    companion object {
        const val TAG: String = "EDIT_PURCHASE_SERVICE_FIELD_FRAGMENT"

        fun newInstance(): EditPurchaseServiceEmailFragment =
            EditPurchaseServiceEmailFragment()
    }

    override fun getTheme(): Int {
        return R.style.BottomSheet
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (parentFragment is EditPurchaseServiceEmailListener) {
            editPurchaseServiceEmailListener = parentFragment as EditPurchaseServiceEmailListener
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.mailTextInputLayout.addTextWatcher { email ->
            binding.saveTextView.isEnabled =
                email != currentEmail && email.isNotEmpty()
            if (binding.saveTextView.isEnabled)
                currentEmail = email
        }

        binding.saveTextView.setOnDebouncedClickListener {
            currentEmail?.let { editPurchaseServiceEmailListener?.onSaveMailClick(it) }
            dismissAllowingStateLoss()
        }
    }

    interface EditPurchaseServiceEmailListener : Serializable {
        fun onSaveMailClick(email: String)
    }
}
