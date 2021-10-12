package com.custom.rgs_android_dom.ui.property.add.select_address

import android.Manifest
import android.graphics.Rect
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.databinding.FragmentSelectAddressBinding
import com.custom.rgs_android_dom.ui.base.BaseFragment
import com.custom.rgs_android_dom.ui.confirm.ConfirmBottomSheetFragment
import com.custom.rgs_android_dom.ui.location.rationale.RequestLocationRationaleFragment
import com.custom.rgs_android_dom.ui.location.suggestions.AddressSuggestionsFragment
import com.custom.rgs_android_dom.ui.navigation.ADD_PROPERTY
import com.custom.rgs_android_dom.ui.navigation.ScreenManager
import com.custom.rgs_android_dom.utils.*
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.ScreenPoint
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.*
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.parameter.parametersOf

class SelectAddressFragment : BaseFragment<SelectAddressViewModel, FragmentSelectAddressBinding>(
    R.layout.fragment_select_address
), RequestLocationRationaleFragment.OnDismissListener, ConfirmBottomSheetFragment.ConfirmListener{

    companion object {
        private const val ARG_PROPERTY_COUNT = "ARG_PROPERTY_COUNT"

        fun newInstance(propertyCount: Int): SelectAddressFragment {
            return SelectAddressFragment().args {
                putInt(ARG_PROPERTY_COUNT, propertyCount)
            }
        }
    }

    private var locationPin: PlacemarkMapObject? = null
    private var pinRect: Rect? = null

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

        binding.mapView.map.addCameraListener { map, cameraPosition, cameraUpdateReason, hasCompleted ->
            if (pinRect == null){
                pinRect = Rect()
                binding.locationPinImageView.getGlobalVisibleRect(pinRect)
            }
           if (hasCompleted){
               pinRect?.let { pinRect->
                   val screenPoint = ScreenPoint(pinRect.exactCenterX(), pinRect.exactCenterY() - 44.dp(requireContext()))
                   val worldPoint = binding.mapView.screenToWorld(screenPoint)

                   viewModel.onLocationChanged(worldPoint)
                   /*if (locationPin == null){
                       val pinLayout = layoutInflater.inflate(R.layout.location_pin, null)
                       val viewProvide = ViewProvider(pinLayout)
                       locationPin = binding.mapView.map.mapObjects.addPlacemark(worldPoint, viewProvide)
                   } else {
                       locationPin?.geometry = worldPoint
                   }*/
               }
           }
        }
       binding.mapView.map.isRotateGesturesEnabled = false

       binding.editAddressTextView.setOnDebouncedClickListener {
           val addressSuggestionsFragment = AddressSuggestionsFragment()
           addressSuggestionsFragment.show(childFragmentManager, addressSuggestionsFragment.TAG)
       }

       binding.propertyNameTextInputLayout.addTextWatcher {
           viewModel.onPropertyNameChanged(it)
       }

       binding.nextTextView.setOnDebouncedClickListener {
            viewModel.onNextClick()
       }

        subscribe(viewModel.selectAddressViewStateObserver){selectAddressViewState->
            if (selectAddressViewState.updatePropertyNameEditText){
                binding.propertyNameTextInputLayout.setText(selectAddressViewState.propertyName)
            }
            binding.nextTextView.isEnabled = selectAddressViewState.isNextTextViewEnabled
            binding.myLocationImageView.visibleIf(selectAddressViewState.isMyLocationImageViewVisible)
            binding.addressPrimaryTextView.text = selectAddressViewState.propertyAddress
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
               title = "Хотите выйти?",
               description = "Если вы покинете страницу сейчас, данные об объекте недвижимости не сохранятся",
               confirmText = "Да, выйти",
               cancelText = "Нет, остаться"
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
        binding.loadingPinFrameLayout.visible()
        binding.locationPinImageView.invisible()
        binding.nextTextView.isEnabled = false
    }

    override fun onContent() {
        super.onContent()
        binding.veilContainer.veilLayout.unVeil()
        binding.veilContainer.root.gone()
        binding.addressDataConstraintLayout.visible()
        binding.loadingPinFrameLayout.gone()
        binding.locationPinImageView.visible()

        // TODO Find out how we can improve this stuff???
        binding.nextTextView.isEnabled = binding.propertyNameTextInputLayout.getText().isNotEmpty()
    }

    override fun onDismiss() {
        viewModel.onRequestLocationRationaleDialogClosed()
    }

    private fun moveToLocation(location: Point){
        binding.mapView.map.move(
            CameraPosition(location, 14.0f, 0.0f, 0.0f),
            Animation(Animation.Type.SMOOTH, 1f),
            null
        )
    }

    private fun showRequestLocationRationaleDialog(){
        val requestLocationRationaleDialog = RequestLocationRationaleFragment()
        requestLocationRationaleDialog.show(childFragmentManager, requestLocationRationaleDialog.TAG)
    }

    override fun onConfirmClick() {
        super.onConfirmClick()
        hideSoftwareKeyboard()
        ScreenManager.closeScope(ADD_PROPERTY)
    }

}