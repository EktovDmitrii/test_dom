package com.custom.rgs_android_dom.ui.purchase_service.edit_purchase_service_address

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.databinding.FragmentEditPurchaseServiceAddressBinding
import com.custom.rgs_android_dom.domain.property.models.PropertyItemModel
import com.custom.rgs_android_dom.ui.base.BaseBottomSheetModalFragment
import com.custom.rgs_android_dom.utils.args
import com.custom.rgs_android_dom.utils.setOnDebouncedClickListener
import com.custom.rgs_android_dom.utils.subscribe
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.parameter.parametersOf
import java.io.Serializable

class PurchaseAddressFragment : BaseBottomSheetModalFragment<PurchaseAddressViewModel,FragmentEditPurchaseServiceAddressBinding>() {

    private var editPurchaseServiceAddressListener: EditPurchaseServiceAddressListener? = null

    private val propertyListAdapter: PurchasePropertyAdapter
        get() = binding.propertyRecyclerView.adapter as PurchasePropertyAdapter

    override val TAG: String ="EDIT_PURCHASE_SERVICE_ADDRESS_FRAGMENT"

    companion object {
        private const val ARG_PROPERTY_MODEL = "ARG_PROPERTY_MODEL"

        fun newInstance(propertyItemModel: PropertyItemModel?): PurchaseAddressFragment =
            PurchaseAddressFragment().args {
                putSerializable(ARG_PROPERTY_MODEL, propertyItemModel)
            }
    }

    override fun getParameters(): ParametersDefinition = {
        parametersOf(requireArguments().getSerializable(ARG_PROPERTY_MODEL))
    }

    override fun getTheme(): Int {
        return R.style.BottomSheet
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (parentFragment is EditPurchaseServiceAddressListener) {
            editPurchaseServiceAddressListener =
                parentFragment as EditPurchaseServiceAddressListener
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.propertyRecyclerView.adapter = PurchasePropertyAdapter {
            editPurchaseServiceAddressListener?.onSelectPropertyClick(it)
            dismissAllowingStateLoss()
        }
        binding.addNewPropertyBtn.setOnDebouncedClickListener {
            viewModel.propertyObserver.value?.size?.let {
                editPurchaseServiceAddressListener?.onAddPropertyClick()
                dismissAllowingStateLoss()
            }
        }
        subscribe(viewModel.propertyObserver) { propertyList ->
            if (propertyList.isNotEmpty())
                viewModel.selectedPropertyItemModel?.let {
                    propertyListAdapter.setItems(
                        propertyList,
                        it
                    )
                }
        }
    }

    interface EditPurchaseServiceAddressListener : Serializable {
        fun onSelectPropertyClick(propertyItemModel: PropertyItemModel)
        fun onAddPropertyClick()
    }
}
