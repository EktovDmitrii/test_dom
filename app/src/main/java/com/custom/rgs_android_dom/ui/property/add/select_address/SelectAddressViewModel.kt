package com.custom.rgs_android_dom.ui.property.add.select_address

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.custom.rgs_android_dom.domain.location.LocationInteractor
import com.custom.rgs_android_dom.domain.property.PropertyInteractor
import com.custom.rgs_android_dom.domain.property.view_states.SelectAddressViewState
import com.custom.rgs_android_dom.ui.base.BaseViewModel
import com.custom.rgs_android_dom.ui.navigation.ADD_PROPERTY
import com.custom.rgs_android_dom.ui.navigation.ScreenManager
import com.custom.rgs_android_dom.ui.property.add.select_type.SelectPropertyTypeFragment
import com.custom.rgs_android_dom.utils.logException
import com.yandex.mapkit.geometry.Point
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class SelectAddressViewModel(private val propertyCount: Int,
                             private val propertyInteractor: PropertyInteractor,
                             private val locationInteractor: LocationInteractor
) : BaseViewModel() {

    private val selectAddressViewStateController = MutableLiveData<SelectAddressViewState>()
    val selectAddressViewStateObserver: LiveData<SelectAddressViewState> = selectAddressViewStateController

    private val locationController = MutableLiveData<Point>()
    val locationObserver: LiveData<Point> = locationController

    private val showLocationPermissionsRationaleController = MutableLiveData<Boolean>()
    val showLocationPermissionsRationaleObserver: LiveData<Boolean> = showLocationPermissionsRationaleController

    private val showConfirmCloseController = MutableLiveData<Unit>()
    val showConfirmCloseObserver: LiveData<Unit> = showConfirmCloseController

    private val showPinLoaderController = MutableLiveData<Boolean>()
    val showPinLoaderObserver: LiveData<Boolean> = showPinLoaderController

    init {
        propertyInteractor.selectAddressViewStateSubject
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = {
                    selectAddressViewStateController.value = it
                },
                onError = {
                    logException(this, it)
                }
            ).addTo(dataCompositeDisposable)

        propertyInteractor.initPropertyName(propertyCount)

        locationInteractor.getAddressSelectedSubject()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = {
                    propertyInteractor.onPropertyAddressChanged(it)
                    locationController.value = it.coordinates
                },
                onError = {
                    logException(this, it)
                }
            ).addTo(dataCompositeDisposable)

        loadingStateController.value = LoadingState.LOADING
    }

    fun onBackClick() {
        showConfirmCloseController.value = Unit
    }

    fun onLocationPermissionsGranted(){
        getUserLocation()
    }

    fun onLocationPermissionsNotGranted(){
        locationController.value = locationInteractor.getDefaultLocation()
        //onLocationChanged(locationInteractor.getDefaultLocation())
        propertyInteractor.onFailedToGetLocation()
        loadingStateController.value = LoadingState.CONTENT
    }

    fun onShouldRequestLocationPermissionsRationale(){
        showLocationPermissionsRationaleController.value = true
    }

    fun onRequestLocationRationaleDialogClosed(){
        getUserLocation()
    }

    fun onMyLocationClick(){
        getUserLocation()
    }

    fun onLocationChanged(newLocation: Point){
        locationInteractor.decodeLocation(newLocation)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = {
                    showPinLoaderController.value = false
                    propertyInteractor.onPropertyAddressChanged(it)
                },
                onError = {
                    showPinLoaderController.value = false
                    logException(this, it)
                }
            ).addTo(dataCompositeDisposable)
    }

    fun onPropertyNameChanged(name: String){
        propertyInteractor.onPropertyNameChanged(name)
    }

    fun onNextClick(){
        selectAddressViewStateController.value?.let {
            ScreenManager.showScreenScope(SelectPropertyTypeFragment.newInstance(
                propertyName =  it.propertyName ,
                propertyAddress = it.propertyAddress
            ),
                ADD_PROPERTY)
        }
    }

    fun onMapMoving(){
        showPinLoaderController.value = true
    }

    private fun getUserLocation(){
        locationInteractor.getCurrentLocation()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = {
                    locationController.value = it
                    propertyInteractor.onLocationLoaded()
                    loadingStateController.value = LoadingState.CONTENT

                    //onLocationChanged(it)
                },
                onError = {
                    logException(this, it)
                    propertyInteractor.onFailedToGetLocation()
                    locationController.value = locationInteractor.getDefaultLocation()
                    loadingStateController.value = LoadingState.CONTENT

                    //onLocationChanged(locationInteractor.getDefaultLocation())
                }
            ).addTo(dataCompositeDisposable)
    }

}