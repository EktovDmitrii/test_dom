package com.custom.rgs_android_dom.ui.property.info.edit

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Size
import android.view.*
import android.widget.PopupWindow
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.data.network.url.GlideUrlProvider
import com.custom.rgs_android_dom.databinding.FragmentEditPropertyInfoBinding
import com.custom.rgs_android_dom.domain.property.models.PropertyType
import com.custom.rgs_android_dom.ui.address.suggestions.AddressSuggestionsFragment
import com.custom.rgs_android_dom.ui.base.BaseFragment
import com.custom.rgs_android_dom.ui.property.info.edit.avatar.EditPropertyAvatarBottomSheetFragment
import com.custom.rgs_android_dom.ui.property.info.edit.request_edit.RequestPropertyInfoEditFragment
import com.custom.rgs_android_dom.utils.*
import com.custom.rgs_android_dom.views.MSDYesNoSelector
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.parameter.parametersOf

class EditPropertyInfoFragment :
    BaseFragment<EditPropertyInfoViewModel, FragmentEditPropertyInfoBinding>(R.layout.fragment_edit_property_info) {

    companion object {
        private const val ARG_OBJECT_ID = "ARG_OBJECT_ID"
        private const val ARG_IS_EDITABLE = "ARG_IS_EDITABLE"

        fun newInstance(objectId: String, isEditable: Boolean): EditPropertyInfoFragment {
            return EditPropertyInfoFragment().args {
                putString(ARG_OBJECT_ID, objectId)
                putBoolean(ARG_IS_EDITABLE, isEditable)
            }
        }
    }

    override fun getParameters(): ParametersDefinition = {
        parametersOf(
            requireArguments().getString(ARG_OBJECT_ID),
            requireArguments().getBoolean(ARG_IS_EDITABLE)
        )
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
            val editPropertyAvatarBottomSheetFragment =
                EditPropertyAvatarBottomSheetFragment.newInstance(
                    viewModel.isExistAvatarObserver.value == true
                )
            editPropertyAvatarBottomSheetFragment.show(childFragmentManager, editPropertyAvatarBottomSheetFragment.TAG)
        }
        binding.addressApartmentClickView.setOnDebouncedClickListener {
            val addressSuggestionsFragment = AddressSuggestionsFragment()
            addressSuggestionsFragment.show(childFragmentManager, addressSuggestionsFragment.TAG)
        }
        binding.propertyTypeRadioGroup.setOnCheckedChangeListener { _, id ->
            val isFlatSelected = id == R.id.flatTypeRadioButton
            toggleFlatFields(isFlatSelected, isFlatSelected && viewModel.isPropertyEditableObserver.value ?: true)

            viewModel.onPropertyTypeChanged(
                if (id == R.id.flatTypeRadioButton) PropertyType.APARTMENT.type
                else PropertyType.HOUSE.type
            )
        }
        binding.isOwnInfoImageView.setOnDebouncedClickListener {
            showPopUpWindow(binding.isOwnInfoImageView)
        }
        binding.isInRentInfoImageView.setOnDebouncedClickListener {
            showPopUpWindow(binding.isInRentInfoImageView)
        }
        binding.isTemporaryInfoImageView.setOnDebouncedClickListener {
            showPopUpWindow(binding.isTemporaryInfoImageView)
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
        makeRequestLink()

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
            binding.totalAreaInputLayout.setText(propertyViewState.totalArea)
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
        subscribe(viewModel.editPropertyRequestedObserver) { wasRequested ->
            binding.editRequestTextView.goneIf(wasRequested)
            binding.descriptionTextView.text = if (wasRequested) {
                "Заявка на изменение типа, адреса и информации о недвижимости успешно отправлена."
            } else {
                "Тип, адрес и информацию о недвижимости можно изменить только по запросу"
            }
        }
        subscribe(viewModel.isPropertyEditableObserver) { isPropertyEditable ->
            binding.requestEditLinearLayout.visibleIf(!isPropertyEditable)
            binding.layoutTemp.deepForEach { isEnabled = isPropertyEditable }
        }
    }

    private fun makeRequestLink() {
        binding.editRequestTextView.makeStringWithLink(
            resources.getColor(R.color.primary500,null),
            Pair(
                "оставьте заявку",
                View.OnClickListener {
                    viewModel.objectIdObserver.value?.let { objectId ->
                        val requestPropertyInfoEditFragment =
                            RequestPropertyInfoEditFragment.newInstance(objectId)
                        requestPropertyInfoEditFragment.show(
                            childFragmentManager,
                            requestPropertyInfoEditFragment.TAG
                        )
                    }
                })
        )
    }

    private fun toggleFlatFields(isFlatSelected: Boolean, isEnabled: Boolean) {
        binding.floorTextInputLayout.isSelected = isFlatSelected
        binding.entranceTextInputLayout.isSelected = isFlatSelected

        binding.floorTextInputLayout.toggleEnable(isEnabled)
        binding.entranceTextInputLayout.toggleEnable(isEnabled)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun showPopUpWindow(anchorView: View) {
        val context = anchorView.context
        val triangleHeight = 8.dp(context)
        PopupWindow().apply {
            width = WindowManager.LayoutParams.WRAP_CONTENT
            height = WindowManager.LayoutParams.WRAP_CONTENT
            isFocusable = true
            isClippingEnabled = false
            val inflater: LayoutInflater =
                (context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater)

            contentView = inflater.inflate(R.layout.popup_window_below_info_icon, null, false)
            contentView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)

            val anchorViewLocation = IntArray(2)
            anchorView.getLocationOnScreen(anchorViewLocation)

            val bottomBarLocation = IntArray(2)
            binding.saveBottomAppBar.getLocationOnScreen(bottomBarLocation)

            val showBelow: Boolean
            contentView = if (bottomBarLocation[1] - anchorViewLocation[1] > contentView.measuredHeight) {
                showBelow = true
                inflater.inflate(R.layout.popup_window_below_info_icon, null, false)
            } else {
                showBelow = false
                inflater.inflate(R.layout.popup_window_above_info_icon, null, false)
            }
            contentView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)

            val contentViewDimensions = Size(
                contentView.measuredWidth,
                contentView.measuredHeight
            )
            val infoTextView = contentView.findViewById<View>(R.id.infoTextView)
            if (showBelow) {
                showAtLocation(
                    anchorView,
                    Gravity.START or Gravity.TOP,
                    anchorViewLocation[0] - contentViewDimensions.width
                            +(contentViewDimensions.width - infoTextView.measuredWidth),
                    anchorViewLocation[1] + triangleHeight)
            } else {
                showAtLocation(
                    anchorView,
                    Gravity.START or Gravity.TOP,
                    anchorViewLocation[0] - contentViewDimensions.width
                            +(contentViewDimensions.width - infoTextView.measuredWidth),
                    anchorViewLocation[1] - contentViewDimensions.height + (contentViewDimensions.height - infoTextView.measuredHeight)/2 + triangleHeight )
            }

            contentView.setOnTouchListener { _, _ ->
                this.dismiss()
                true
            }
        }
    }
}