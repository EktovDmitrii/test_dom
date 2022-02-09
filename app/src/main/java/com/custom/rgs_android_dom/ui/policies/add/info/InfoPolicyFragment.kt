package com.custom.rgs_android_dom.ui.policies.add.info

import android.os.Bundle
import android.view.View
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.databinding.FragmentInfoPolicyBinding
import com.custom.rgs_android_dom.ui.base.BaseBottomSheetFragment
import com.custom.rgs_android_dom.utils.setOnDebouncedClickListener

class InfoPolicyFragment : BaseBottomSheetFragment<InfoPolicyViewModel, FragmentInfoPolicyBinding>() {

    override val TAG: String = "INFO_POLICY_FRAGMENT"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.confirmTextView.setOnDebouncedClickListener {
            viewModel.onConfirmClick()
        }

        binding.closeImageView.setOnDebouncedClickListener {
            viewModel.onCloseClick()
        }

    }
    override fun onClose() {
        dismissAllowingStateLoss()
    }

    override fun getThemeResource(): Int {
        return R.style.BottomSheet
    }

}