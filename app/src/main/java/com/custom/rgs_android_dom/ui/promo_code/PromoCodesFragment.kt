package com.custom.rgs_android_dom.ui.promo_code

import android.os.Bundle
import android.view.View
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.databinding.FragmentPromoCodesBinding
import com.custom.rgs_android_dom.ui.base.BaseFragment
import com.custom.rgs_android_dom.utils.*

class PromoCodesFragment :
    BaseFragment<PromoCodesViewModel, FragmentPromoCodesBinding>(R.layout.fragment_promo_codes) {

    private val promoCodesAdapter: PromoCodesAdapter
        get() = binding.dataStateLayout.recyclerView.adapter as PromoCodesAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.dataStateLayout.recyclerView.adapter = PromoCodesAdapter { promoCodeModel ->
            viewModel.onItemClick(promoCodeModel, childFragmentManager)
        }

        binding.backImageView.setOnDebouncedClickListener {
            viewModel.onBackClick()
        }

        binding.emptyStateLayout.bindTextView.setOnDebouncedClickListener {
            viewModel.onAddClick(childFragmentManager)
        }

        binding.addImageView.setOnDebouncedClickListener {
            viewModel.onAddClick(childFragmentManager)
        }

        subscribe(viewModel.promoCodesObserver) {
            binding.emptyStateLayout.root.visibleIf(it.isEmpty())
            binding.dataStateLayout.root.visibleIf(it.isNotEmpty())
            promoCodesAdapter.setItems(it, false)
        }
    }

    override fun onContent() {
        super.onContent()
        binding.promoCodeShimmerLayout.root.gone()
    }

    override fun onError() {
        super.onError()
        binding.promoCodeShimmerLayout.root.gone()
    }

    override fun onLoading() {
        super.onLoading()
        binding.promoCodeShimmerLayout.root.visible()
    }
}
