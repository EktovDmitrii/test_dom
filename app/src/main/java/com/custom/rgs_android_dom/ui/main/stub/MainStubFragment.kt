package com.custom.rgs_android_dom.ui.main.stub

import android.os.Bundle
import android.view.View
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.databinding.FragmentClientBinding
import com.custom.rgs_android_dom.databinding.FragmentMainStubBinding
import com.custom.rgs_android_dom.ui.base.BaseBottomSheetFragment
import com.custom.rgs_android_dom.utils.*
import com.custom.rgs_android_dom.utils.recycler_view.HorizontalItemDecoration

class MainStubFragment : BaseBottomSheetFragment<MainStubViewModel, FragmentMainStubBinding>() {

    override val TAG: String = "MAIN_STUB_FRAGMENT"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.stubLinearLayout.setOnDebouncedClickListener {
            viewModel.onStubClick()
        }
    }

    override fun onClose() {
        hideSoftwareKeyboard()
    }

    override fun getThemeResource(): Int {
        return R.style.BottomSheetNoDim
    }
}