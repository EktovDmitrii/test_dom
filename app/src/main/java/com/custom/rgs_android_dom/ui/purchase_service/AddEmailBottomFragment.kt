package com.custom.rgs_android_dom.ui.purchase_service

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import com.custom.rgs_android_dom.databinding.FragmentBottomAddEmailBinding
import com.custom.rgs_android_dom.ui.base.BaseBottomSheetModalFragment
import com.custom.rgs_android_dom.utils.isValidEmail
import com.custom.rgs_android_dom.utils.setOnDebouncedClickListener

class AddEmailBottomFragment :
    BaseBottomSheetModalFragment<AddEmailViewModel, FragmentBottomAddEmailBinding>() {

    override val TAG: String = "ADD_EMAIL_BOTTOM_FRAGMENT"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.emailTextInputLayout.addTextWatcher { email ->
            binding.saveButton.isEnabled = email.trim().isValidEmail()
        }
        binding.saveButton.setOnDebouncedClickListener {
            setFragmentResult(
                "email_request",
                bundleOf("email_key" to binding.emailTextInputLayout.getText().trim())
            )
            dismiss()
        }
    }

}