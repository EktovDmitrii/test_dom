package com.custom.rgs_android_dom.ui.property.add.select_type

import android.os.Bundle
import android.view.View
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.databinding.FragmentSelectPropertyTypeBinding
import com.custom.rgs_android_dom.domain.address.models.AddressItemModel
import com.custom.rgs_android_dom.ui.base.BaseFragment
import com.custom.rgs_android_dom.ui.confirm.ConfirmBottomSheetFragment
import com.custom.rgs_android_dom.utils.*
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.parameter.parametersOf

class SelectPropertyTypeFragment : BaseFragment<SelectPropertyTypeViewModel, FragmentSelectPropertyTypeBinding>(R.layout.fragment_select_property_type),
    ConfirmBottomSheetFragment.ConfirmListener{

    companion object {
        private const val ARG_PROPERTY_NAME = "ARG_PROPERTY_NAME"
        private const val ARG_PROPERTY_ADDRESS = "ARG_PROPERTY_ADDRESS"

        fun newInstance(propertyName: String, propertyAddress: AddressItemModel): SelectPropertyTypeFragment {
            return SelectPropertyTypeFragment().args {
                putString(ARG_PROPERTY_NAME, propertyName)
                putString(ARG_PROPERTY_ADDRESS, AddressItemModel.toString(propertyAddress))
            }
        }
    }

    override fun getParameters(): ParametersDefinition = {
        parametersOf(
            requireArguments().getString(ARG_PROPERTY_NAME),
            requireArguments().getString(ARG_PROPERTY_ADDRESS)
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        hideSoftwareKeyboard()

        binding.backImageView.setOnDebouncedClickListener {
            viewModel.onBackClick()
        }

        binding.nextTextView.setOnDebouncedClickListener {
            viewModel.onNextClick()
        }

        binding.selectHomeLinearLayout.setOnDebouncedClickListener {
            viewModel.onSelectHomeClick()
        }

        binding.selectAppartmentLinearLayout.setOnDebouncedClickListener {
            viewModel.onSelectAppartmentClick()
        }

        subscribe(viewModel.selectPropertyTypeViewStateObserver){
            binding.selectAppartmentLinearLayout.isSelected = it.isSelectAppartmentLinearLayoutSelected
            binding.selectHomeLinearLayout.isSelected = it.isSelectHomeLinearLayoutSelected
            binding.nextTextView.isEnabled = it.isNextTextViewEnabled
        }


    }


}