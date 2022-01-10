package com.custom.rgs_android_dom.ui.catalog.product

import android.os.Bundle
import android.view.View
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.databinding.FragmentMyProductBinding
import com.custom.rgs_android_dom.ui.base.BaseBottomSheetFragment
import com.custom.rgs_android_dom.ui.catalog.MyProductAdvantagesAdapter
import com.custom.rgs_android_dom.ui.catalog.MyProductInclusionAdapter
import com.custom.rgs_android_dom.utils.*
import com.custom.rgs_android_dom.utils.recycler_view.GridSpaceItemDecoration

class MyProductFragment :
    BaseBottomSheetFragment<MyProductViewModel, FragmentMyProductBinding>() {

    companion object {
        fun newInstance(): MyProductFragment {
            return MyProductFragment()
        }
    }

    override val TAG: String = "PRODUCT_DETAIL_FRAGMENT"

    override fun getThemeResource(): Int = R.style.BottomSheetNoDim

    private val inclusionAdapter: MyProductInclusionAdapter
        get() = binding.includes.includesRecycler.adapter as MyProductInclusionAdapter

    private val advantagesAdapter: MyProductAdvantagesAdapter
        get() = binding.advantagesLayout.advantagesRecycler.adapter as MyProductAdvantagesAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.backImageView.setOnDebouncedClickListener {
            viewModel.onBackClick()
        }

        binding.includes.includesRecycler.apply {
            adapter = MyProductInclusionAdapter {
                viewModel.onServiceClick()
            }
            val spacingInPixels = resources.getDimensionPixelSize(R.dimen.material_margin_normal)
            addItemDecoration(GridSpaceItemDecoration(spacingInPixels))
        }
        binding.advantagesLayout.advantagesRecycler.adapter = MyProductAdvantagesAdapter()
    }
}