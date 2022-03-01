package com.custom.rgs_android_dom.ui.property.info

import android.os.Bundle
import android.view.View
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.data.network.url.GlideUrlProvider
import com.custom.rgs_android_dom.databinding.FragmentEditPropertyInfoBinding
import com.custom.rgs_android_dom.domain.property.models.PropertyType
import com.custom.rgs_android_dom.ui.address.suggestions.AddressSuggestionsFragment
import com.custom.rgs_android_dom.ui.base.BaseFragment
import com.custom.rgs_android_dom.utils.*
import com.custom.rgs_android_dom.views.MSDYesNoSelector
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.parameter.parametersOf

class EditPropertyInfoFragment :
    BaseFragment<EditPropertyInfoViewModel, FragmentEditPropertyInfoBinding>(R.layout.fragment_edit_property_info) {

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
        binding.saveTextView.setOnDebouncedClickListener {
            hideSoftwareKeyboard(true)
        }
        binding.editImagePropertyTextView.setOnDebouncedClickListener {
            val editPropertyAvatarBottomSheetFragment = EditPropertyAvatarBottomSheetFragment.newInstance(
                viewModel.isExistAvatarObserver.value == true
            )
            editPropertyAvatarBottomSheetFragment.show(childFragmentManager, editPropertyAvatarBottomSheetFragment.TAG)
        }
        binding.addressApartmentClickView.setOnDebouncedClickListener {
            val addressSuggestionsFragment = AddressSuggestionsFragment()
            addressSuggestionsFragment.show(childFragmentManager, addressSuggestionsFragment.TAG)
        }
        binding.propertyTypeRadioGroup.setOnCheckedChangeListener { _, id ->
            setVisibleFlatFields(id == R.id.flatTypeRadioButton)

            viewModel.onPropertyTypeChanged(
                if (id == R.id.flatTypeRadioButton) PropertyType.APARTMENT.type
                else PropertyType.HOUSE.type
            )
        }
        binding.nameApartmentTextInputLayout.addTextWatcher {
            viewModel.onPropertyNameChanged(it)
        }
        binding.floorTextInputLayout.addTextWatcher {
            viewModel.onFloorChanged(it)
        }
        binding.entranceTextInputLayout.addTextWatcher {
            viewModel.onEntranceChanged(it)
        }
        binding.totalAreaInputLayout.addTextWatcher {
            viewModel.onTotalAreaChanged(it)
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
        binding.commentInputLayout.addTextWatcher {
            viewModel.onCommentChanged(it)
        }
        binding.saveTextView.setOnDebouncedClickListener {
            viewModel.onSaveClicked()
        }
        subscribe(viewModel.propertyDetailsObserver) { propertyViewState ->
            binding.nameApartmentTextInputLayout.setText(propertyViewState.name)

            binding.propertyTypeRadioGroup.check(
                if (propertyViewState.type == PropertyType.APARTMENT.type) R.id.flatTypeRadioButton
                else R.id.houseTypeRadioButton
            )
            if (!propertyViewState.photoLink.isNullOrEmpty()) {
                GlideApp.with(requireContext())
                    .load(GlideUrlProvider.makeHeadersGlideUrl(propertyViewState.photoLink))
                    .transform(
                        CenterCrop(),
                        RoundedCorners(16.dp(requireContext()))
                    )
                    .into(binding.propertyAvatarImageView)
            } else {
                binding.propertyAvatarImageView.setImageResource(0)
            }

            propertyViewState.isOwn?.let {
                val isOwn = if (it == MSDYesNoSelector.Selection.YES.selectionString) MSDYesNoSelector.Selection.YES else MSDYesNoSelector.Selection.NO
                binding.isOwnSelector.setSelection(isOwn)
            }
            propertyViewState.isRent?.let {
                val isRent = if (it == MSDYesNoSelector.Selection.YES.selectionString) MSDYesNoSelector.Selection.YES else MSDYesNoSelector.Selection.NO
                binding.isInRentSelector.setSelection(isRent)
            }
            propertyViewState.isTemporary?.let {
                val isTemporary = if (it == MSDYesNoSelector.Selection.YES.selectionString) MSDYesNoSelector.Selection.YES else MSDYesNoSelector.Selection.NO
                binding.isTemporarySelector.setSelection(isTemporary)
            }

            binding.cityApartmentTextInputLayout.setText(propertyViewState.address.cityName.ifEmpty { "Не определено" })
            binding.addressApartmentTextInputLayout.setText(propertyViewState.address.addressString)
            binding.totalAreaInputLayout.setText(if (propertyViewState.totalArea.isNotEmpty()) "${propertyViewState.totalArea} м²" else "")
            binding.floorTextInputLayout.setText(propertyViewState.floor)
            binding.entranceTextInputLayout.setText(propertyViewState.entrance)
            binding.commentInputLayout.setText(propertyViewState.comment)
        }
        subscribe(viewModel.selectAddressItemModelObserver) {
            binding.cityApartmentTextInputLayout.setText(it.cityName.ifEmpty { "Не определено" })
            binding.addressApartmentTextInputLayout.setText(it.addressString)
        }
        subscribe(viewModel.isEnabledSaveButtonObserver) {
            binding.saveTextView.isEnabled = it
        }
        subscribe(viewModel.uploadedLocallyAvatarObserver) {
            if (it.isNotEmpty()) {
                GlideApp.with(requireContext())
                    .load(GlideUrlProvider.makeHeadersGlideUrl(it))
                    .transform(
                        CenterCrop(),
                        RoundedCorners(16.dp(requireContext()))
                    )
                    .into(binding.propertyAvatarImageView)
            } else {
                binding.propertyAvatarImageView.setImageResource(0)
            }
        }
    }

    private fun setVisibleFlatFields(isVisible: Boolean) {
        binding.floorTextInputLayout.toggleEnable(isVisible)
        binding.entranceTextInputLayout.toggleEnable(isVisible)
        binding.floorTextInputLayout.isEnabled = isVisible
        binding.entranceTextInputLayout.isEnabled = isVisible
    }
}