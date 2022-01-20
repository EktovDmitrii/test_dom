package com.custom.rgs_android_dom.ui.purchase_service.edit_purchase_service_address

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.databinding.FragmentEditPurchaseServiceAddressBinding
import com.custom.rgs_android_dom.domain.property.models.PropertyItemModel
import com.custom.rgs_android_dom.utils.args
import com.custom.rgs_android_dom.utils.setOnDebouncedClickListener
import com.custom.rgs_android_dom.utils.subscribe
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.parameter.parametersOf
import java.io.Serializable

class EditPurchaseServiceAddressFragment : BottomSheetDialogFragment() {

    private val binding: FragmentEditPurchaseServiceAddressBinding by viewBinding(createMethod = CreateMethod.INFLATE)

    private val viewModel: EditPurchaseServiceAddressViewModel by viewModel(parameters = getParameters())

    private var editPurchaseServiceAddressListener: EditPurchaseServiceAddressListener? = null

    private val propertyListAdapter: EditPurchaseServicePropertyAdapter
        get() = binding.propertyRecyclerView.adapter as EditPurchaseServicePropertyAdapter

    companion object {
        const val TAG: String = "EDIT_PURCHASE_SERVICE_ADDRESS_FRAGMENT"
        private const val ARG_PROPERTY_MODEL = "ARG_PROPERTY_MODEL"

        fun newInstance(propertyItemModel: PropertyItemModel?): EditPurchaseServiceAddressFragment =
            EditPurchaseServiceAddressFragment().args {
                putSerializable(ARG_PROPERTY_MODEL, propertyItemModel)
            }
    }

    fun getParameters(): ParametersDefinition = {
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
        binding.propertyRecyclerView.adapter = EditPurchaseServicePropertyAdapter {
            editPurchaseServiceAddressListener?.onSelectPropertyClick(it)
            dismissAllowingStateLoss()
        }
        binding.addNewPropertyBtn.setOnDebouncedClickListener {
            viewModel.propertyControllerObserver.value?.size?.let {
                editPurchaseServiceAddressListener?.onAddPropertyClick()
                dismissAllowingStateLoss()
            }
        }
        subscribe(viewModel.propertyControllerObserver) { propertyList ->
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
