package com.custom.rgs_android_dom.ui.property.add.select_address

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.custom.rgs_android_dom.domain.location.LocationInteractor
import com.custom.rgs_android_dom.domain.property.PropertyInteractor
import com.custom.rgs_android_dom.domain.property.view_states.SelectAddressViewState
import com.custom.rgs_android_dom.ui.base.BaseViewModel
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

        locationController.value = locationInteractor.getDefaultLocation()
        loadingStateController.value = LoadingState.LOADING
    }

    fun onBackClick(){
        closeController.value = Unit
    }

    fun onLocationPermissionsGranted(){
        loadLocation()
    }

    fun onLocationPermissionsNotGranted(){
        locationController.value = locationInteractor.getDefaultLocation()
        propertyInteractor.onFailedToGetLocation()
        loadingStateController.value = LoadingState.CONTENT
    }

    fun onShouldRequestLocationPermissionsRationale(){
        showLocationPermissionsRationaleController.value = true
    }

    fun onRequestLocationRationaleDialogClosed(){
        loadLocation()
    }

    fun onMyLocationClick(){
        loadLocation()
    }

    private fun loadLocation(){
        locationInteractor.getLocation()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = {
                    locationController.value = it
                    propertyInteractor.onLocationLoaded()
                    loadingStateController.value = LoadingState.CONTENT
                },
                onError = {
                    logException(this, it)
                    propertyInteractor.onFailedToGetLocation()
                    locationController.value = locationInteractor.getDefaultLocation()
                    loadingStateController.value = LoadingState.CONTENT
                }
            ).addTo(dataCompositeDisposable)
    }

}