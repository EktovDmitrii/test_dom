package com.custom.rgs_android_dom.ui.property.info.more

import android.os.Bundle
import android.view.View
import com.custom.rgs_android_dom.databinding.FragmentPropertyMoreBinding
import com.custom.rgs_android_dom.domain.property.models.PropertyItemModel
import com.custom.rgs_android_dom.ui.base.BaseBottomSheetModalFragment
import com.custom.rgs_android_dom.ui.property.delete.DeletePropertyFragment
import com.custom.rgs_android_dom.utils.activity.hideKeyboardForced
import com.custom.rgs_android_dom.utils.args
import com.custom.rgs_android_dom.utils.setOnDebouncedClickListener
import com.custom.rgs_android_dom.utils.subscribe
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.parameter.parametersOf

class PropertyMoreFragment() : BaseBottomSheetModalFragment<PropertyMoreViewModel, FragmentPropertyMoreBinding>(){

    companion object {
        private const val ARG_PROPERTY = "ARG_PROPERTY"

        fun newInstance(property: PropertyItemModel): PropertyMoreFragment {
            return PropertyMoreFragment().args {
                putSerializable(ARG_PROPERTY, property)
            }
        }
    }

    override val TAG: String = "PROPERTY_MORE_FRAGMENT"

    override fun getParameters(): ParametersDefinition = {
        parametersOf(requireArguments().getSerializable(ARG_PROPERTY) as PropertyItemModel)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.editPropertyTextView.setOnDebouncedClickListener {
            viewModel.onEditPropertyClick()
        }

        binding.deletePropertyTextView.setOnDebouncedClickListener {
            viewModel.onDeletePropertyClick()
        }

        subscribe(viewModel.deletePropertyObserver){
            dismissAllowingStateLoss()
            val deletePropertyFragment = DeletePropertyFragment.newInstance(it)
            deletePropertyFragment.show(parentFragmentManager, deletePropertyFragment.TAG)
        }

    }

}