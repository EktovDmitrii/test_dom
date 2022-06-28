package com.custom.rgs_android_dom.ui.promo_code.modal

import android.os.Bundle
import android.view.View
import com.custom.rgs_android_dom.databinding.FragmentModalPromoCodesBinding
import com.custom.rgs_android_dom.ui.base.BaseBottomSheetModalFragment
import com.custom.rgs_android_dom.ui.promo_code.PromoCodesAdapter
import com.custom.rgs_android_dom.utils.hideSoftwareKeyboard
import com.custom.rgs_android_dom.utils.setOnDebouncedClickListener
import com.custom.rgs_android_dom.utils.subscribe
import com.custom.rgs_android_dom.utils.visibleIf

class ModalPromoCodesFragment :
    BaseBottomSheetModalFragment<ModalPromoCodesViewModel, FragmentModalPromoCodesBinding>() {


    override val TAG: String = "SELECT_PROMO_CODE_FRAGMENT"

    private val promoCodesAdapter: PromoCodesAdapter
        get() = binding.dataStateLayout.recyclerView.adapter as PromoCodesAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        hideSoftwareKeyboard()

        binding.dataStateLayout.recyclerView.adapter = PromoCodesAdapter { id, isActive ->
            //TODO добавить клик в задаче RGSMSD-1848
        }

        binding.emptyStateLayout.bindTextView.setOnDebouncedClickListener {
//            viewModel.onAddClick(parentFragmentManager)
        }

        binding.addImageView.setOnDebouncedClickListener {
            viewModel.onAddClick(parentFragmentManager)
        }

        subscribe(viewModel.promoCodesObserver) {
            binding.emptyStateLayout.root.visibleIf(it.isEmpty())
            binding.dataStateLayout.root.visibleIf(it.isNotEmpty())
            promoCodesAdapter.setItems(it)
        }
    }
}
