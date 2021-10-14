package com.custom.rgs_android_dom.data.repositories.location

import android.annotation.SuppressLint
import android.content.Context
import android.os.Looper
import com.custom.rgs_android_dom.data.network.MSDApi
import com.custom.rgs_android_dom.data.network.mappers.LocationMapper
import com.custom.rgs_android_dom.domain.location.models.AddressItemModel
import com.custom.rgs_android_dom.domain.repositories.LocationRepository
import com.google.android.gms.location.*
import com.yandex.mapkit.geometry.Point
import io.reactivex.Single
import io.reactivex.SingleEmitter
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit

class LocationRepositoryImpl(private val context: Context,
                             private val api: MSDApi
): LocationRepository {

    companion object {
        private const val COUNTRY = "Россия"
    }

    private val defaultLocation = Point(55.713136, 37.647504)
    private val selectedAddressSubject = PublishSubject.create<AddressItemModel>()

    @SuppressLint("MissingPermission")
    override fun getCurrentLocation(): Single<Point> =
        Single.create { emitter ->
            val locationClient = LocationServices.getFusedLocationProviderClient(context)
            locationClient.lastLocation
                .addOnSuccessListener { location ->
                    if (location != null) {
                        val locationPoint = Point(location.latitude, location.longitude)
                        emitter.onSuccess(locationPoint)
                    } else {
                        onLastKnownLocationError(locationClient, emitter)
                    }
                }
                .addOnFailureListener {
                    emitter.onError(it)
                }
        }

    override fun getDefaultLocation(): Point {
        return defaultLocation
    }

    @SuppressLint("MissingPermission")
    private fun onLastKnownLocationError(locationClient: FusedLocationProviderClient, emitter: SingleEmitter<Point>){
        val locationRequest = LocationRequest.create().apply {
            interval = TimeUnit.SECONDS.toMillis(40)
            fastestInterval = TimeUnit.SECONDS.toMillis(10)
            maxWaitTime = TimeUnit.MINUTES.toMillis(1)
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        var locationCallback: LocationCallback? = null
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                val location = locationResult.lastLocation
                if (location != null){
                    val locationPoint = Point(location.latitude, location.longitude)
                    emitter.onSuccess(locationPoint)
                } else {
                    emitter.onError(Throwable("Location is null"))
                }
                locationClient.removeLocationUpdates(locationCallback)
            }
        }
        locationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
    }

    override fun decodeLocation(newLocation: Point): Single<AddressItemModel> {
        return api.getAddressByCoordinates(newLocation.latitude, newLocation.longitude).map {
            LocationMapper.responseToAddress(it.results[0])
        }
    }

    override fun selectAddress(addressModel: AddressItemModel) {
        selectedAddressSubject.onNext(addressModel)
    }

    override fun geSelectedAddressSubject(): PublishSubject<AddressItemModel> {
        return selectedAddressSubject
    }

    override fun getAddressSuggestions(query: String): Single<List<AddressItemModel>> {
        return api.getAddressSuggestions(query, COUNTRY).map {response->
            response.results.map {
                LocationMapper.responseToAddress(it)
            }
        }
    }

}