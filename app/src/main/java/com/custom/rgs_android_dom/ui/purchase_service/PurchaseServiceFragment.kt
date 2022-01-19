package com.custom.rgs_android_dom.ui.purchase_service

import android.os.Bundle
import android.view.View
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.databinding.FragmentPurchaseServiceBinding
import com.custom.rgs_android_dom.ui.base.BaseBottomSheetFragment

class PurchaseServiceFragment :
    BaseBottomSheetFragment<PurchaseServiceViewModel, FragmentPurchaseServiceBinding>() {
    override val TAG: String = "PURCHASE_SERVICE_FRAGMENT"

    companion object {
        fun newInstance(): PurchaseServiceFragment = PurchaseServiceFragment()
    }

    override fun getThemeResource(): Int = R.style.BottomSheetNoDim

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

}
