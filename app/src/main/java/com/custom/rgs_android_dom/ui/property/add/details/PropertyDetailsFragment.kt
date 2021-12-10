package com.custom.rgs_android_dom.ui.property.add.details

import android.annotation.SuppressLint
import android.content.Context.LAYOUT_INFLATER_SERVICE
import android.os.Bundle
import android.util.Size
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.PopupWindow
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.databinding.FragmentPropertyDetailsBinding
import com.custom.rgs_android_dom.domain.address.models.AddressItemModel
import com.custom.rgs_android_dom.domain.property.details.view_states.PropertyDetailsViewState
import com.custom.rgs_android_dom.domain.property.models.PropertyType
import com.custom.rgs_android_dom.ui.base.BaseFragment
import com.custom.rgs_android_dom.ui.confirm.ConfirmBottomSheetFragment
import com.custom.rgs_android_dom.ui.navigation.ADD_PROPERTY
import com.custom.rgs_android_dom.ui.navigation.ScreenManager
import com.custom.rgs_android_dom.ui.property.add.details.files.PropertyUploadDocumentsAdapter
import com.custom.rgs_android_dom.ui.property.add.details.files.PropertyUploadDocumentsFragment
import com.custom.rgs_android_dom.utils.*
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.parameter.parametersOf

class PropertyDetailsFragment : BaseFragment<PropertyDetailsViewModel, FragmentPropertyDetailsBinding>(R.layout.fragment_property_details),
    ConfirmBottomSheetFragment.ConfirmListener {

    companion object {
        private const val ARG_PROPERTY_NAME = "ARG_PROPERTY_NAME"
        private const val ARG_PROPERTY_ADDRESS = "ARG_PROPERTY_ADDRESS"
        private const val ARG_PROPERTY_TYPE = "ARG_PROPERTY_TYPE"

        fun newInstance(propertyName: String, propertyAddress: String, propertyType: PropertyType): PropertyDetailsFragment {
            return PropertyDetailsFragment().args {
                putString(ARG_PROPERTY_NAME, propertyName)
                putString(ARG_PROPERTY_ADDRESS, propertyAddress)
                putSerializable(ARG_PROPERTY_TYPE, propertyType)
            }
        }
    }

    private val adapter: PropertyUploadDocumentsAdapter
        get() = binding.listDocumentsRecyclerView.adapter as PropertyUploadDocumentsAdapter

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

        binding.entranceTextInputLayout.addTextWatcher {
            viewModel.onEntranceChanged(it)
        }

        binding.corpusApartmentTextInputLayout.addTextWatcher {
            viewModel.onCorpusChanged(it)
        }

        binding.corpusHomeTextInputLayout.addTextWatcher {
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

        binding.listDocumentsRecyclerView.adapter = PropertyUploadDocumentsAdapter { uri -> viewModel.onRemoveDocumentClick(uri) }

        binding.uploadDocumentFrameLayout.setOnDebouncedClickListener {
            val propertyUploadFilesFragment = PropertyUploadDocumentsFragment()
            propertyUploadFilesFragment.show(childFragmentManager, propertyUploadFilesFragment.TAG)
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

        subscribe(viewModel.propertyDetailsObserver){
            binding.addTextView.isEnabled = it.isAddTextViewEnabled
            binding.addressTextInputLayout.setText(it.address.addressString)
            adapter.setItems(it.documents)
            when(it.type){
                PropertyType.APARTMENT.type -> { showApartmentLayout(it) }
                PropertyType.HOUSE.type -> { showHouseLayout(it) }
            }
        }

        subscribe(viewModel.networkErrorObserver){
            toast(it)
        }

        subscribe(viewModel.notificationObserver){
            notification(it)
        }

    }

    override fun onConfirmClick() {
        ScreenManager.closeScope(ADD_PROPERTY)
    }

    private fun showHouseLayout(propertyDetailsViewState: PropertyDetailsViewState) {
        binding.apartmentDataLinearLayout.gone()
        binding.homeDataLinearLayout.visible()
        val cityName = propertyDetailsViewState.address.cityName
        binding.cityNameHomeTextInputLayout.setText( if ( cityName.isNotEmpty() ) { cityName } else {"Не определено"} )
        binding.corpusHomeTextInputLayout.setText(propertyDetailsViewState.corpus)
    }

    private fun showApartmentLayout(propertyDetailsViewState: PropertyDetailsViewState) {
        binding.apartmentDataLinearLayout.visible()
        binding.homeDataLinearLayout.gone()
        val cityName = propertyDetailsViewState.address.cityName
        binding.cityNameApartmentTextInputLayout.setText( if ( cityName.isNotEmpty() ) { cityName } else {"Не определено"} )
        binding.corpusApartmentTextInputLayout.setText(propertyDetailsViewState.corpus)
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
                (context.getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater)

            contentView = inflater.inflate(R.layout.popup_window_below_info_icon, null, false)
            contentView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)

            val anchorViewLocation = IntArray(2)
            anchorView.getLocationOnScreen(anchorViewLocation)

            val bottomBarLocation = IntArray(2)
            binding.actionsBottomAppBar.getLocationOnScreen(bottomBarLocation)

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