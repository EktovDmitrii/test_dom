package com.custom.rgs_android_dom.ui.purchase.select.address

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.databinding.FragmentSelectPurchaseAddressBinding
import com.custom.rgs_android_dom.domain.property.models.PropertyItemModel
import com.custom.rgs_android_dom.ui.base.BaseBottomSheetModalFragment
import com.custom.rgs_android_dom.utils.args
import com.custom.rgs_android_dom.utils.setOnDebouncedClickListener
import com.custom.rgs_android_dom.utils.subscribe
import com.custom.rgs_android_dom.utils.visibleIf
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.parameter.parametersOf
import java.io.Serializable

class SelectPurchaseAddressFragment : BaseBottomSheetModalFragment<SelectPurchaseAddressViewModel, FragmentSelectPurchaseAddressBinding>() {

    companion object {
        private const val ARG_PROPERTY_MODEL = "ARG_PROPERTY_MODEL"

        fun newInstance(propertyItemModel: PropertyItemModel?): SelectPurchaseAddressFragment {
            return SelectPurchaseAddressFragment().args {
                if (propertyItemModel != null){
                    putSerializable(ARG_PROPERTY_MODEL, propertyItemModel)
                }

            }
        }
    }

    override val TAG: String ="PURCHASE_ADDRESS_FRAGMENT"

    private var purchaseAddressListener: SelectPurchaseAddressListener? = null

    private val propertyListAdapter: PurchasePropertyAdapter
        get() = binding.propertyRecyclerView.adapter as PurchasePropertyAdapter

    override fun getParameters(): ParametersDefinition = {
       parametersOf(requireArguments().getSerializable(ARG_PROPERTY_MODEL), null)
    }

    override fun getTheme(): Int {
        return R.style.BottomSheet
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (parentFragment is SelectPurchaseAddressListener) {
            purchaseAddressListener = parentFragment as SelectPurchaseAddressListener
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.propertyRecyclerView.adapter = PurchasePropertyAdapter {
            purchaseAddressListener?.onPropertySelected(it)
            dismissAllowingStateLoss()
        }

        binding.addNewPropertyBtn.setOnDebouncedClickListener {
            viewModel.onAddPropertyClick()
            dismissAllowingStateLoss()
        }

        subscribe(viewModel.propertyObserver) { property->
            binding.contentLinearLayout.visibleIf(property.second.isNotEmpty())
            binding.noPropertyLinearLayout.visibleIf(property.second.isEmpty())

            propertyListAdapter.setItems(property.first, property.second)
        }
    }

}

interface SelectPurchaseAddressListener : Serializable {
    fun onPropertySelected(propertyItemModel: PropertyItemModel)
}
