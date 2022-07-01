package com.custom.rgs_android_dom.ui.promo_code

import android.os.Bundle
import android.view.View
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.databinding.FragmentPromoCodesBinding
import com.custom.rgs_android_dom.ui.base.BaseFragment
import com.custom.rgs_android_dom.ui.base.BaseViewModel
import com.custom.rgs_android_dom.utils.hideSoftwareKeyboard
import com.custom.rgs_android_dom.utils.setOnDebouncedClickListener
import com.custom.rgs_android_dom.utils.subscribe
import com.custom.rgs_android_dom.utils.visibleIf

class PromoCodesFragment :
    BaseFragment<PromoCodesViewModel, FragmentPromoCodesBinding>(R.layout.fragment_promo_codes) {

    private val promoCodesAdapter: PromoCodesAdapter
        get() = binding.dataStateLayout.recyclerView.adapter as PromoCodesAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        hideSoftwareKeyboard()

        binding.dataStateLayout.recyclerView.adapter = PromoCodesAdapter { _ -> }

        binding.backImageView.setOnDebouncedClickListener {
            viewModel.onBackClick()
        }

        binding.emptyStateLayout.bindTextView.setOnDebouncedClickListener {
            viewModel.onAddClick(childFragmentManager)
        }

        binding.addImageView.setOnDebouncedClickListener {
            viewModel.onAddClick(childFragmentManager)
        }

        subscribe(viewModel.loadingStateObserver) {
            binding.promoCodeShimmerLayout.root.visibleIf(it == BaseViewModel.LoadingState.LOADING)
        }

        subscribe(viewModel.promoCodesObserver) {
            binding.emptyStateLayout.root.visibleIf(it.isEmpty())
            binding.dataStateLayout.root.visibleIf(it.isNotEmpty())
            promoCodesAdapter.setItems(it, null)
        }
    }
}
