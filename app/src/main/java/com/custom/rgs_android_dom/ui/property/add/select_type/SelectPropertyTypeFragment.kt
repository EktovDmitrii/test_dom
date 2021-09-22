package com.custom.rgs_android_dom.ui.property.add.select_type

import android.os.Bundle
import android.view.View
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.databinding.FragmentSelectPropertyTypeBinding
import com.custom.rgs_android_dom.ui.base.BaseFragment
import com.custom.rgs_android_dom.utils.*
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.parameter.parametersOf

class SelectPropertyTypeFragment : BaseFragment<SelectPropertyTypeViewModel, FragmentSelectPropertyTypeBinding>(R.layout.fragment_select_property_type) {

    companion object {
        private const val ARG_PROPERTY_COUNT = "ARG_PROPERTY_COUNT"

        fun newInstance(propertyCount: Int): SelectPropertyTypeFragment {
            return SelectPropertyTypeFragment().args {
                putInt(ARG_PROPERTY_COUNT, propertyCount)
            }
        }
    }

    override fun getParameters(): ParametersDefinition = {
        parametersOf(
            requireArguments().getInt(ARG_PROPERTY_COUNT)
        )
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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