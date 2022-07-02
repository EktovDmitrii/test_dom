package com.custom.rgs_android_dom.ui.promo_code.modal

import android.os.Bundle
import android.view.View
import com.custom.rgs_android_dom.databinding.FragmentModalPromoCodesBinding
import com.custom.rgs_android_dom.domain.purchase.models.PurchaseModel
import com.custom.rgs_android_dom.ui.base.BaseBottomSheetModalFragment
import com.custom.rgs_android_dom.ui.promo_code.PromoCodesAdapter
import com.custom.rgs_android_dom.utils.*
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.parameter.parametersOf

class ModalPromoCodesFragment :
    BaseBottomSheetModalFragment<ModalPromoCodesViewModel, FragmentModalPromoCodesBinding>() {

    companion object {

        private const val ARG_PURCHASE_SERVICE_MODEL = "ARG_PURCHASE_SERVICE_MODEL"

        fun newInstance(
            purchaseModel: PurchaseModel
        ) = ModalPromoCodesFragment().args {
            putSerializable(ARG_PURCHASE_SERVICE_MODEL, purchaseModel)
        }
    }

    override fun getParameters(): ParametersDefinition = {
        parametersOf(requireArguments().getSerializable(ARG_PURCHASE_SERVICE_MODEL))
    }

    private val promoCodesAdapter: PromoCodesAdapter
        get() = binding.dataStateLayout.recyclerView.adapter as PromoCodesAdapter

    override val TAG: String = "MODAL_PROMO_CODES_FRAGMENT"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.dataStateLayout.recyclerView.adapter = PromoCodesAdapter { promoCodeModel ->
            binding.applyButtonLayout.visible()
            viewModel.saveApplyPromoCode(promoCodeModel)
            onFullScreen()
        }

        binding.emptyStateLayout.root.setOnDebouncedClickListener {
            viewModel.onAddClick(parentFragmentManager)
        }

        binding.addImageView.setOnDebouncedClickListener {
            viewModel.onAddClick(parentFragmentManager)
        }

        binding.applyButton.setOnDebouncedClickListener {
            viewModel.onApplyClick()
        }

        subscribe(viewModel.promoCodesObserver) {
            binding.emptyStateLayout.root.visibleIf(it.isEmpty())
            binding.addImageView.visibleIf(it.isNotEmpty())
            binding.dataStateLayout.root.visibleIf(it.isNotEmpty())
            promoCodesAdapter.setItems(it, true)
        }
    }
}
