package com.custom.rgs_android_dom.ui.catalog.product.single

import android.os.Bundle
import android.view.View
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.databinding.FragmentClientBinding
import com.custom.rgs_android_dom.databinding.FragmentMainStubBinding
import com.custom.rgs_android_dom.databinding.FragmentSingleProductBinding
import com.custom.rgs_android_dom.ui.base.BaseBottomSheetFragment
import com.custom.rgs_android_dom.utils.*
import com.custom.rgs_android_dom.utils.recycler_view.HorizontalItemDecoration

class SingleProductFragment : BaseBottomSheetFragment<SingleProductViewModel, FragmentSingleProductBinding>() {

    override val TAG: String = "SINGLE_PRODUCT_FRAGMENT"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun getThemeResource(): Int {
        return R.style.BottomSheetNoDim
    }
}