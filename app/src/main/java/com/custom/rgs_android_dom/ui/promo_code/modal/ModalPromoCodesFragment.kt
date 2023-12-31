package com.custom.rgs_android_dom.ui.promo_code.modal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.custom.rgs_android_dom.databinding.FragmentModalPromoCodesBinding
import com.custom.rgs_android_dom.domain.promo_codes.model.PromoCodeItemModel
import com.custom.rgs_android_dom.domain.purchase.models.PurchaseModel
import com.custom.rgs_android_dom.ui.base.BaseBottomSheetModalFragment
import com.custom.rgs_android_dom.ui.constants.SIZE_FOR_FULL_SCREEN
import com.custom.rgs_android_dom.ui.promo_code.PromoCodesAdapter
import com.custom.rgs_android_dom.utils.*
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.parameter.parametersOf
import java.io.Serializable

class ModalPromoCodesFragment :
    BaseBottomSheetModalFragment<ModalPromoCodesViewModel, FragmentModalPromoCodesBinding>() {

    companion object {

        private const val ARG_PURCHASE_SERVICE_MODEL = "ARG_PURCHASE_SERVICE_MODEL"
        private const val ARG_PROMO_CODE_MODEL = "ARG_PROMO_CODE_MODEL"

        fun newInstance(
            purchaseModel: PurchaseModel,
            promoCodeItemModel: PromoCodeItemModel?
        ) = ModalPromoCodesFragment().args {
            putSerializable(ARG_PURCHASE_SERVICE_MODEL, purchaseModel)
            if (promoCodeItemModel != null) putSerializable(ARG_PROMO_CODE_MODEL, promoCodeItemModel)
        }
    }

    override fun getParameters(): ParametersDefinition = {
        parametersOf(requireArguments().getSerializable(ARG_PURCHASE_SERVICE_MODEL))
    }

    private val promoCodesAdapter: PromoCodesAdapter
        get() = binding.dataStateLayout.recyclerView.adapter as PromoCodesAdapter

    private var isFullScreenClick = false
    private var purchasePromoCodeListener: PurchasePromoCodeListener? = null
    private var promoCode : PromoCodeItemModel? = null

    override val TAG: String = "MODAL_PROMO_CODES_FRAGMENT"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        if (parentFragment is PurchasePromoCodeListener) {
            purchasePromoCodeListener = parentFragment as PurchasePromoCodeListener
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val selectedPromoCodeItem =
            if (requireArguments().containsKey(ARG_PROMO_CODE_MODEL)) requireArguments().getSerializable(
                ARG_PROMO_CODE_MODEL
            ) as PromoCodeItemModel else null

        binding.dataStateLayout.recyclerView.adapter = PromoCodesAdapter(
            onPromoCodeClick = { promoCode = it },
            onShowApplyButton = { isShowApplyButton ->
                binding.applyButtonLayout.visibleIf(isShowApplyButton)
                if (isShowApplyButton) {
                    if (isFullScreenClick) { onFullScreen() }
                    binding.nestedScrollLayout.setMargins(bottom = 150)
                } else {
                    binding.nestedScrollLayout.setMargins(bottom = 0)
                }
            }
        )

        if (selectedPromoCodeItem != null) {
            binding.nestedScrollLayout.setMargins(bottom = 150)
        }

        binding.emptyStateLayout.root.setOnDebouncedClickListener {
            viewModel.onAddClick(parentFragmentManager)
        }

        binding.addImageView.setOnDebouncedClickListener {
            viewModel.onAddClick(parentFragmentManager)
        }

        binding.applyButtonLayout.visibleIf(selectedPromoCodeItem != null)

        binding.applyButton.setOnDebouncedClickListener {
            if (promoCode == null && selectedPromoCodeItem != null) {
                promoCode = selectedPromoCodeItem
            }
            promoCode?.let {
                purchasePromoCodeListener?.onSavePromoCodeClick(it)
                onClose()
            }
        }

        subscribe(viewModel.agentCodeObserver) {
            purchasePromoCodeListener?.onAgentCode(it)
        }

        subscribe(viewModel.promoCodesObserver) {
            isFullScreenClick = it.size > SIZE_FOR_FULL_SCREEN
            if (isFullScreenClick && selectedPromoCodeItem != null) {
                onFullScreen()
            }
            binding.emptyStateLayout.root.visibleIf(it.isEmpty())
            binding.addImageView.visibleIf(it.isNotEmpty())
            binding.dataStateLayout.root.visibleIf(it.isNotEmpty())
            promoCodesAdapter.setItems(it, true, selectedPromoCodeItem)
        }
    }

    interface PurchasePromoCodeListener : Serializable {
        fun onSavePromoCodeClick(promoCode: PromoCodeItemModel)
        fun onAgentCode(isAgentCode: Boolean)
    }
}
