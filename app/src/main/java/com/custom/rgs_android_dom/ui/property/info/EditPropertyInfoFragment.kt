package com.custom.rgs_android_dom.ui.property.info

import android.os.Bundle
import android.view.View
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.databinding.FragmentEditPropertyInfoBinding
import com.custom.rgs_android_dom.domain.property.models.PropertyType
import com.custom.rgs_android_dom.ui.address.suggestions.AddressSuggestionsFragment
import com.custom.rgs_android_dom.ui.base.BaseFragment
import com.custom.rgs_android_dom.utils.*
import com.custom.rgs_android_dom.views.MSDYesNoSelector
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.parameter.parametersOf

class EditPropertyInfoFragment :
    BaseFragment<EditPropertyInfoViewModel, FragmentEditPropertyInfoBinding>(R.layout.fragment_edit_property_info),
    EditPropertyAvatarBottomSheetFragment.EditPropertyAvatarInfoListener {

    companion object {
        private const val ARG_OBJECT_ID = "ARG_OBJECT_ID"

        fun newInstance(objectId: String): EditPropertyInfoFragment {
            return EditPropertyInfoFragment().args {
                putString(ARG_OBJECT_ID, objectId)
            }
        }
    }

    override fun getParameters(): ParametersDefinition = {
        parametersOf(requireArguments().getString(ARG_OBJECT_ID))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.backImageView.setOnDebouncedClickListener {
            hideSoftwareKeyboard(true)
            viewModel.onBackClick()
        }
        binding.addTextView.setOnDebouncedClickListener {
            hideSoftwareKeyboard(true)
        }
        binding.editImagePropertyTextView.setOnDebouncedClickListener {
            val editPropertyAvatarBottomSheetFragment = EditPropertyAvatarBottomSheetFragment.newInstance(true)
            editPropertyAvatarBottomSheetFragment.show(childFragmentManager, EditPropertyAvatarBottomSheetFragment.TAG)
        }
        binding.addressApartmentTextInputLayout.setOnDebouncedClickListener {
            val addressSuggestionsFragment = AddressSuggestionsFragment()
            addressSuggestionsFragment.show(childFragmentManager, addressSuggestionsFragment.TAG)
        }
        subscribe(viewModel.propertyItemObserver) { propertyItem ->
            binding.nameApartmentTextInputLayout.setText(propertyItem.name)
            binding.flatTypeRadioButton.isChecked = propertyItem.type == PropertyType.APARTMENT
            binding.houseTypeRadioButton.isChecked = propertyItem.type == PropertyType.HOUSE

            propertyItem.isOwn?.let {
                val isOwn = if (it) MSDYesNoSelector.Selection.YES else MSDYesNoSelector.Selection.NO
                binding.isOwnSelector.setSelection(isOwn)
            }
            propertyItem.isRent?.let {
                val isRent = if (it) MSDYesNoSelector.Selection.YES else MSDYesNoSelector.Selection.NO
                binding.isInRentSelector.setSelection(isRent)
            }
            propertyItem.isTemporary?.let {
                val isTemporary = if (it) MSDYesNoSelector.Selection.YES else MSDYesNoSelector.Selection.NO
                binding.isTemporarySelector.setSelection(isTemporary)
            }

            binding.addressApartmentTextInputLayout.setText(propertyItem.address?.cityName ?: "")
            binding.totalAreaInputLayout.setText("${propertyItem.totalArea} м²")
            binding.commentInputLayout.setText(propertyItem.comment)
        }
    }

    override fun onLoadAvatarClicked() {
        TODO("Not yet implemented")
    }

    override fun onDeleteAvatarClicked() {
        TODO("Not yet implemented")
    }

}