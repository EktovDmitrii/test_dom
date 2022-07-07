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
import com.custom.rgs_android_dom.utils.args
import com.custom.rgs_android_dom.utils.setOnDebouncedClickListener
import com.custom.rgs_android_dom.utils.subscribe
import com.custom.rgs_android_dom.utils.visibleIf
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.parameter.parametersOf
import java.io.Serializable

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

        binding.dataStateLayout.recyclerView.adapter = PromoCodesAdapter(
            onPromoCodeClick = { promoCodeModelClick ->
                promoCode = promoCodeModelClick
                if (isFullScreenClick) { onFullScreen() }
            },
            onShowApplyButton = { binding.applyButtonLayout.visibleIf(it) }
        )

        binding.emptyStateLayout.root.setOnDebouncedClickListener {
            viewModel.onAddClick(parentFragmentManager)
        }

        binding.addImageView.setOnDebouncedClickListener {
            viewModel.onAddClick(parentFragmentManager)
        }

        binding.applyButton.setOnDebouncedClickListener {
            promoCode?.let {
                purchasePromoCodeListener?.onSavePromoCodeClick(it)
                onClose()
            }
        }

        subscribe(viewModel.promoCodesObserver) {
            isFullScreenClick = it.size > SIZE_FOR_FULL_SCREEN
            binding.emptyStateLayout.root.visibleIf(it.isEmpty())
            binding.addImageView.visibleIf(it.isNotEmpty())
            binding.dataStateLayout.root.visibleIf(it.isNotEmpty())
            promoCodesAdapter.setItems(it, true)
        }
    }

    interface PurchasePromoCodeListener : Serializable {
        fun onSavePromoCodeClick(promoCode: PromoCodeItemModel)
    }
}
