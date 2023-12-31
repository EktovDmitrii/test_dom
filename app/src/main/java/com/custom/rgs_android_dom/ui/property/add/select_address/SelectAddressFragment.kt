package com.custom.rgs_android_dom.ui.property.add.select_address

import android.Manifest
import android.graphics.Rect
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.EditText
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.databinding.FragmentSelectAddressBinding
import com.custom.rgs_android_dom.domain.property.models.LocationPointModel
import com.custom.rgs_android_dom.domain.translations.TranslationInteractor
import com.custom.rgs_android_dom.ui.MainActivity
import com.custom.rgs_android_dom.ui.base.BaseFragment
import com.custom.rgs_android_dom.ui.confirm.ConfirmBottomSheetFragment
import com.custom.rgs_android_dom.ui.address.suggestions.AddressSuggestionsFragment
import com.custom.rgs_android_dom.ui.navigation.ADD_PROPERTY
import com.custom.rgs_android_dom.ui.navigation.ScreenManager
import com.custom.rgs_android_dom.ui.rationale.RequestRationaleFragment
import com.custom.rgs_android_dom.utils.*
import com.custom.rgs_android_dom.utils.activity.hideKeyboardForced
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.*
import com.yandex.metrica.YandexMetrica
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.parameter.parametersOf

class SelectAddressFragment : BaseFragment<SelectAddressViewModel, FragmentSelectAddressBinding>(
    R.layout.fragment_select_address
), RequestRationaleFragment.OnRequestRationaleDismissListener, ConfirmBottomSheetFragment.ConfirmListener, MainActivity.DispatchTouchEventListener {

    companion object {
        private const val ARG_PROPERTY_COUNT = "ARG_PROPERTY_COUNT"

        private const val AZIMUTH = 0.0f
        private const val TILT = 0.0f
        private const val ANIMATION_DURATION = 1f
        private const val REQUEST_CODE_LOCATION = 3

        fun newInstance(propertyCount: Int = 0): SelectAddressFragment {
            return SelectAddressFragment().args {
                putInt(ARG_PROPERTY_COUNT, propertyCount)
            }
        }
    }

    private var pinRect: Rect? = null

    private val cameraListener =
        CameraListener { _, _, _, hasCompleted ->
            if (pinRect == null){
                pinRect = Rect()
                binding.locationPinImageView.getGlobalVisibleRect(pinRect)
                val screenPoint = pinRect!!.toScreenPoint()
                binding.mapView.focusPoint = screenPoint
            }
            if (hasCompleted){
                if(binding.locationPinImageView.isActivated) binding.locationPinImageView.isActivated = false
                pinRect?.let { pinRect->
                    val screenPoint = pinRect.toScreenPoint()
                    val worldPoint = binding.mapView.screenToWorld(screenPoint)
                    binding.mapView.zoomFocusPoint = screenPoint
                    viewModel.onLocationChanged(worldPoint)
                }
            } else {
                if(!binding.locationPinImageView.isActivated) binding.locationPinImageView.isActivated = true
                binding.nextTextView.isEnabled = false
            }
        }

    private val requestLocationPermissionAction =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissionsResult ->
            if (permissionsResult[Manifest.permission.ACCESS_FINE_LOCATION] == true
                && permissionsResult[Manifest.permission.ACCESS_COARSE_LOCATION] == true) {
                viewModel.onLocationPermissionsGranted()
            }
            else if (!ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                && !ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)) {
                viewModel.onShouldRequestLocationPermissionsRationale()
            } else {
                viewModel.onLocationPermissionsNotGranted()
            }
        }

    override fun getParameters(): ParametersDefinition = {
        parametersOf(
            requireArguments().getInt(ARG_PROPERTY_COUNT)
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MapKitFactory.initialize(requireContext())
    }

   override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requestLocationPermissionAction.launch(arrayOf( Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION))

        binding.backImageView.setOnDebouncedClickListener {
            viewModel.onBackClick()
        }

        binding.myLocationImageView.setOnDebouncedClickListener {
            viewModel.onMyLocationClick()
        }

       binding.mapView.map.addCameraListener(cameraListener)
       binding.mapView.map.isRotateGesturesEnabled = false
       binding.mapView.zoomFocusPointMode = ZoomFocusPointMode.AFFECTS_ALL_GESTURES

       binding.editAddressTextView.setOnDebouncedClickListener {
           val addressSuggestionsFragment = AddressSuggestionsFragment()
           addressSuggestionsFragment.show(childFragmentManager, addressSuggestionsFragment.TAG)
       }

       binding.propertyNameTextInputLayout.addTextWatcher {
           viewModel.onPropertyNameChanged(it)
       }

       binding.nextTextView.setOnDebouncedClickListener {
           YandexMetrica.reportEvent("profile_object_add_address")
           YandexMetrica.reportEvent("profile_object_add_name")

           hideSoftwareKeyboard(true)
           viewModel.onNextClick()
       }

        subscribe(viewModel.selectAddressViewStateObserver){selectAddressViewState->
            if (selectAddressViewState.updatePropertyNameEditText){
                binding.propertyNameTextInputLayout.setText(selectAddressViewState.propertyName)
            }
            binding.nextTextView.isEnabled = selectAddressViewState.isNextTextViewEnabled
            binding.addressPrimaryTextView.text =
                if (selectAddressViewState.propertyAddress.addressString.isNotEmpty())
                    selectAddressViewState.propertyAddress.addressString
                else "Данные не найдены"


            var secondaryText = selectAddressViewState.propertyAddress.cityName
            if (secondaryText.trim().isNotEmpty()){
                secondaryText = "$secondaryText, "
            }
            secondaryText = "$secondaryText${selectAddressViewState.propertyAddress.regionName}"
            binding.addressSecondaryTextView.text = secondaryText
        }

        subscribe(viewModel.locationObserver){
           moveToLocation(it)
        }

        subscribe(viewModel.showLocationPermissionsRationaleObserver){
            showRequestLocationRationaleDialog()
        }

       subscribe(viewModel.showConfirmCloseObserver){
           val confirmDialog = ConfirmBottomSheetFragment.newInstance(
               icon = R.drawable.ic_confirm_cancel,
               title = TranslationInteractor.getTranslation("app.object.add.step_cancel.question.title"),
               description = TranslationInteractor.getTranslation("app.object.add.step_cancel.question.subtitle"),
               confirmText = TranslationInteractor.getTranslation("app.object.add.step_cancel.buttons.confirm"),
               cancelText = TranslationInteractor.getTranslation("app.object.add.step_cancel.buttons.confirm_decline")
           )
           confirmDialog.show(childFragmentManager, ConfirmBottomSheetFragment.TAG)
       }

    }

    override fun onStart() {
        super.onStart()
        binding.mapView.onStart();
        MapKitFactory.getInstance().onStart();
    }

    override fun onStop() {
        super.onStop()
        binding.mapView.onStop();
        MapKitFactory.getInstance().onStop();
    }

    override fun onClose() {
        viewModel.onBackClick()
    }

    override fun onLoading() {
        super.onLoading()
        binding.veilContainer.veilLayout.veil()
        binding.veilContainer.root.visible()
        binding.addressDataConstraintLayout.gone()
        binding.nextTextView.isEnabled = false
    }

    override fun onContent() {
        super.onContent()
        binding.veilContainer.veilLayout.unVeil()
        binding.veilContainer.root.gone()
        binding.addressDataConstraintLayout.visible()
        binding.nextTextView.isEnabled = binding.propertyNameTextInputLayout.getText().isNotEmpty()
    }

    override fun onRequestRationaleDismiss(requestCode: Int?) {
        viewModel.onRequestLocationRationaleDialogClosed()
    }

    override fun onConfirmClick() {
        YandexMetrica.reportEvent("profile_object_add_abort_yes")

        ScreenManager.closeScope(ADD_PROPERTY)
    }

    override fun onCancelClick() {
        YandexMetrica.reportEvent("profile_object_add_abort_no")
    }

    override fun dispatchTouchEvent(event: MotionEvent) {
        if (event.action == MotionEvent.ACTION_DOWN) {
            val v: View? = requireActivity().currentFocus
            if (v is EditText) {
                val outRect = Rect()
                v.getGlobalVisibleRect(outRect)
                if (!outRect.contains(event.rawX.toInt(), event.rawY.toInt())) {
                    v.clearFocus()
                    requireActivity().hideKeyboardForced()
                }
            }
        }
    }

    private fun moveToLocation(location: LocationPointModel){
        binding.mapView.map.move(
            CameraPosition(location.point, location.zoom, AZIMUTH, TILT),
            Animation(Animation.Type.SMOOTH, ANIMATION_DURATION),
            null
        )
    }

    private fun showRequestLocationRationaleDialog(){
        val requestLocationRationaleDialog = RequestRationaleFragment.newInstance(
            requestCode = REQUEST_CODE_LOCATION,
            description = TranslationInteractor.getTranslation("app.object.add.step_setting.title"),
            icon = R.drawable.device_location
        )
        requestLocationRationaleDialog.show(childFragmentManager, requestLocationRationaleDialog.TAG)
    }

}