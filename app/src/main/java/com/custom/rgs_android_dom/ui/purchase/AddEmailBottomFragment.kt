package com.custom.rgs_android_dom.ui.purchase

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.custom.rgs_android_dom.databinding.FragmentBottomAddEmailBinding
import com.custom.rgs_android_dom.ui.base.BaseBottomSheetModalFragment
import com.custom.rgs_android_dom.utils.isValidEmail
import com.custom.rgs_android_dom.utils.setOnDebouncedClickListener
import java.io.Serializable

class AddEmailBottomFragment :
    BaseBottomSheetModalFragment<AddEmailViewModel, FragmentBottomAddEmailBinding>() {

    override val TAG: String = "ADD_EMAIL_BOTTOM_FRAGMENT"
    private var purchaseEmailListener: PurchaseEmailListener? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (parentFragment is PurchaseEmailListener) {
            purchaseEmailListener = parentFragment as PurchaseEmailListener
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.emailTextInputLayout.addTextWatcher { email ->
            binding.saveButton.isEnabled = email.trim().isValidEmail()
        }
        binding.saveButton.setOnDebouncedClickListener {
            purchaseEmailListener?.onSaveEmailClick(binding.emailTextInputLayout.getText().trim())
            dismissAllowingStateLoss()
        }
    }

    interface PurchaseEmailListener : Serializable {
        fun onSaveEmailClick(email: String)
    }
}