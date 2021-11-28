package com.custom.rgs_android_dom.ui.property.add.details

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.PopupWindow
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.databinding.FragmentPropertyDetailsBinding
import com.custom.rgs_android_dom.domain.address.models.AddressItemModel
import com.custom.rgs_android_dom.domain.property.details.exceptions.PropertyField
import com.custom.rgs_android_dom.domain.property.models.PropertyType
import com.custom.rgs_android_dom.ui.base.BaseFragment
import com.custom.rgs_android_dom.utils.*
import com.custom.rgs_android_dom.views.edit_text.MSDTextInputLayout
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.parameter.parametersOf


class PropertyDetailsFragment :
    BaseFragment<PropertyDetailsViewModel, FragmentPropertyDetailsBinding>(R.layout.fragment_property_details) {

    companion object {
        private const val ARG_PROPERTY_NAME = "ARG_PROPERTY_NAME"
        private const val ARG_PROPERTY_ADDRESS = "ARG_PROPERTY_ADDRESS"
        private const val ARG_PROPERTY_TYPE = "ARG_PROPERTY_TYPE"

        fun newInstance(
            propertyName: String,
            propertyAddress: String,
            propertyType: PropertyType
        ): PropertyDetailsFragment {
            return PropertyDetailsFragment().args {
                putString(ARG_PROPERTY_NAME, propertyName)
                putString(ARG_PROPERTY_ADDRESS, propertyAddress)
                putSerializable(ARG_PROPERTY_TYPE, propertyType)
            }
        }
    }

    override fun getParameters(): ParametersDefinition = {
        parametersOf(
            requireArguments().getString(ARG_PROPERTY_NAME),
            AddressItemModel.fromString(requireArguments().getString(ARG_PROPERTY_ADDRESS, "")),
            requireArguments().getSerializable(ARG_PROPERTY_TYPE)
        )
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.backImageView.setOnDebouncedClickListener {
            hideSoftwareKeyboard(true)
            viewModel.onBackClick()
        }

        binding.addTextView.setOnDebouncedClickListener {
            hideSoftwareKeyboard(true)
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

        binding.isOwnInfoImageView.also { isOwnInfoImageView ->
            isOwnInfoImageView.setOnDebouncedClickListener {
                showPopUpWindow(isOwnInfoImageView, true)
            }
        }

        binding.isInRentInfoImageView.also { isInRentInfoImageView ->
            isInRentInfoImageView.setOnDebouncedClickListener {
                showPopUpWindow(isInRentInfoImageView, true)
            }
        }

        binding.isTemporaryInfoImageView.also { isTemporaryInfoImageView ->
            isTemporaryInfoImageView.setOnDebouncedClickListener {
                showPopUpWindow(isTemporaryInfoImageView, false)
            }
        }

        subscribe(viewModel.propertyDetailsObserver) {
            binding.addTextView.isEnabled = it.isAddTextViewEnabled
            if (it.updatePropertyAddressEditText) {
                binding.addressTextInputLayout.setText(it.address.addressString)
            }
        }

        subscribe(viewModel.validateExceptionObserver) {
            when (it.field) {
                PropertyField.ADDRESS -> {
                    binding.addressTextInputLayout.setState(MSDTextInputLayout.State.ERROR)
                }
            }
        }

        subscribe(viewModel.networkErrorObserver) {
            toast(it)
        }

        subscribe(viewModel.notificationObserver) {
            notification(it)
        }
    }


    @SuppressLint("ClickableViewAccessibility")
    private fun showPopUpWindow(anchorView: View, belowAnchorView: Boolean) {

        LayoutInflater.from(anchorView.context)
            .inflate(
                if (belowAnchorView) {
                    R.layout.popup_window_below_info_icon
                } else {
                    R.layout.popup_window_over_info_icon
                }, null, false
            )
            .also { contentView ->

                val popupWindow = PopupWindow(contentView).apply {
                    this.width = WindowManager.LayoutParams.WRAP_CONTENT
                    this.height = WindowManager.LayoutParams.WRAP_CONTENT
                    this.isFocusable = true

                    val view = this.contentView
                    view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
                    this.setBackgroundDrawable(
                        /*ResourcesCompat.getDrawable(
                        resources,
                        R.drawable.ic_pin,
                        null
                    )*/ColorDrawable(Color.GRAY)
                    )
                    this.showAsDropDown(
                        anchorView,
                        anchorView.width - view.measuredWidth + anchorView.width / 2,
                        if (belowAnchorView) {
                            0
                        } else {
                            -anchorView.height - view.measuredHeight
                        }

                    )

                    /*val offsetX = (-(192 + 100)).dp(anchorView.context)
                    this.setBackgroundDrawable(
                        *//*ResourcesCompat.getDrawable(
                            resources,
                            R.drawable.ic_pin,
                            null
                        )*//*ColorDrawable(Color.GRAY)
                    )
                    this.showAsDropDown(anchorView, offsetX, 0, Gravity.START)*/
                }

                contentView.setOnTouchListener { _, _ ->
                    popupWindow.dismiss()
                    true
                }
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
