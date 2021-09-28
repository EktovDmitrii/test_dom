package com.custom.rgs_android_dom.ui.property.add.details

import android.os.Bundle
import android.view.View
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.databinding.FragmentPropertyDetailsBinding
import com.custom.rgs_android_dom.domain.property.details.exceptions.PropertyField
import com.custom.rgs_android_dom.domain.property.models.PropertyType
import com.custom.rgs_android_dom.ui.base.BaseFragment
import com.custom.rgs_android_dom.ui.confirm.ConfirmBottomSheetFragment
import com.custom.rgs_android_dom.ui.navigation.ADD_PROPERTY
import com.custom.rgs_android_dom.ui.navigation.ScreenManager
import com.custom.rgs_android_dom.utils.*
import com.custom.rgs_android_dom.views.edit_text.MSDTextInputLayout
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.parameter.parametersOf

class PropertyDetailsFragment : BaseFragment<PropertyDetailsViewModel, FragmentPropertyDetailsBinding>(R.layout.fragment_property_details) {

    companion object {
        private const val ARG_PROPERTY_COUNT = "ARG_PROPERTY_COUNT"
        private const val ARG_PROPERTY_TYPE = "ARG_PROPERTY_TYPE"

        fun newInstance(propertyCount: Int, propertyType: PropertyType): PropertyDetailsFragment {
            return PropertyDetailsFragment().args {
                putInt(ARG_PROPERTY_COUNT, propertyCount)
                putSerializable(ARG_PROPERTY_TYPE, propertyType)
            }
        }
    }

    override fun getParameters(): ParametersDefinition = {
        parametersOf(
            requireArguments().getInt(ARG_PROPERTY_COUNT),
            requireArguments().getSerializable(ARG_PROPERTY_TYPE)
        )
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.backImageView.setOnDebouncedClickListener {
            hideSoftwareKeyboard()
            viewModel.onBackClick()
        }

        binding.addTextView.setOnDebouncedClickListener {
            hideSoftwareKeyboard()
            viewModel.onAddClick()
        }

        binding.addressTextInputLayout.addTextWatcher {
            binding.addressTextInputLayout.setState(MSDTextInputLayout.State.NORMAL)
            viewModel.onAddressChanged(it)
        }

        binding.entranceTextInputLayout.addTextWatcher {
            viewModel.onEntranceChanged(it)
        }

        binding.corpusTextInputLayout.addTextWatcher {
            viewModel.onCorpusChanged(it)
        }

        binding.floorTextInputLayout.addTextWatcher {
            viewModel.onFloorChanged(it)
        }

        binding.flatTextInputLayout.addTextWatcher {
            viewModel.onFlatChanged(it)
        }

        binding.totalAreaInputLayout.addTextWatcher {
            viewModel.onTotalAreaChanged(it)
        }

        binding.commentInputLayout.addTextWatcher {
            viewModel.onCommentChanged(it)
        }

        binding.isOwnSelector.setSelectionListener {
            viewModel.onIsOwnSelected(it)
        }

        binding.isInRentSelector.setSelectionListener {
            viewModel.onIsInRentSelected(it)
        }

        binding.isTemporarySelector.setSelectionListener {
            viewModel.onIsTemporarySelected(it)
        }

        subscribe(viewModel.propertyDetailsObserver){
            binding.addTextView.isEnabled = it.isAddTextViewEnabled
        }

        subscribe(viewModel.validateExceptionObserver){
            when(it.field){
                PropertyField.ADDRESS -> {
                    binding.addressTextInputLayout.setState(MSDTextInputLayout.State.ERROR)
                }
            }
        }

        subscribe(viewModel.networkErrorObserver){
            toast(it)
        }

        subscribe(viewModel.notificationObserver){
            notification(it)
        }
    }

    override fun onError() {
        super.onError()
        binding.addTextView.setLoading(false)
    }

    override fun onLoading() {
        super.onLoading()
        binding.addTextView.setLoading(true)
    }

    override fun onContent() {
        super.onContent()
        binding.addTextView.setLoading(false)
    }
}