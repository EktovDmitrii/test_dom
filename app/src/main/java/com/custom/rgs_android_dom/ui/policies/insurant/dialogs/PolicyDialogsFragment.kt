package com.custom.rgs_android_dom.ui.policies.insurant.dialogs

import android.os.Bundle
import android.view.View
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.databinding.FragmentPolicyDialogsBinding
import com.custom.rgs_android_dom.ui.base.BaseBottomSheetFragment
import com.custom.rgs_android_dom.utils.setOnDebouncedClickListener

class PolicyDialogsFragment :
    BaseBottomSheetFragment<PolicyDialogsViewModel, FragmentPolicyDialogsBinding>() {

    override val TAG: String = "POLICY_DIALOGS_FRAGMENT"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.loadingLayout.closeImageView.setOnDebouncedClickListener {
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
