package com.custom.rgs_android_dom.ui.managers

import android.annotation.SuppressLint
import android.content.Context
import android.os.Looper
import com.google.android.gms.location.*
import com.yandex.mapkit.geometry.Point
import java.util.concurrent.TimeUnit

class LocationManager(private val context: Context){

    private val defaultLocation = Point(55.713136, 37.647504)

    @SuppressLint("MissingPermission")
    fun getCurrentLocation(onSuccess: (Point) -> Unit = {}, onError: (Throwable) -> Unit = {}) {
        val locationClient = LocationServices.getFusedLocationProviderClient(context)
        locationClient.lastLocation
            .addOnSuccessListener { location ->
                if (location != null) {
                    val locationPoint = Point(location.latitude, location.longitude)
                    onSuccess(locationPoint)
                } else {
                    onLastKnownLocationError(locationClient, onSuccess, onError)
                }
            }
            .addOnFailureListener {
                onError(it)
            }
    }

    fun getDefaultLocation(): Point {
        return defaultLocation
    }

    @SuppressLint("MissingPermission")
    private fun onLastKnownLocationError(locationClient: FusedLocationProviderClient, onSuccess: (Point) -> Unit = {}, onError: (Throwable) -> Unit = {}){
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
                    onSuccess(locationPoint)
                } else {
                    onError(Throwable("Location is null"))
                }
                locationCallback?.let {
                    locationClient.removeLocationUpdates(it)
                }
            }
        }
        locationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
    }

}