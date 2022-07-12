package com.custom.rgs_android_dom.ui.promo_code

import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.databinding.FragmentPromoCodesBinding
import com.custom.rgs_android_dom.ui.base.BaseFragment
import com.custom.rgs_android_dom.ui.constants.VIEW_ROOT_HEIGHT
import com.custom.rgs_android_dom.ui.promo_code.dialogs.PromoCodeDialogFragment
import com.custom.rgs_android_dom.utils.*

class PromoCodesFragment :
    BaseFragment<PromoCodesViewModel, FragmentPromoCodesBinding>(R.layout.fragment_promo_codes),
    PromoCodeDialogFragment.PromoCodesListener{

    private val promoCodesAdapter: PromoCodesAdapter
        get() = binding.dataStateLayout.recyclerView.adapter as PromoCodesAdapter

    private var keyboardListener: ViewTreeObserver.OnGlobalLayoutListener? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        keyboardListener = ViewTreeObserver.OnGlobalLayoutListener {
            if (binding.root.height < VIEW_ROOT_HEIGHT) {
                hideSoftwareKeyboard()
            }
        }

        binding.root.viewTreeObserver.addOnGlobalLayoutListener(keyboardListener)

        binding.dataStateLayout.recyclerView.adapter = PromoCodesAdapter(
            onPromoCodeClick = { viewModel.onItemClick(it, childFragmentManager) },
            onShowApplyButton = {}
        )

        binding.backImageView.setOnDebouncedClickListener {
            binding.root.viewTreeObserver.removeOnGlobalLayoutListener(keyboardListener)
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
            promoCodesAdapter.setItems(it, false, null)
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

    override fun onGetNewPromoCodesList(isLoadingList: Boolean) {
        if (isLoadingList) viewModel.getPromoCodes()
    }

    override fun onStop() {
        super.onStop()
        binding.root.viewTreeObserver.removeOnGlobalLayoutListener(keyboardListener)
    }

}
