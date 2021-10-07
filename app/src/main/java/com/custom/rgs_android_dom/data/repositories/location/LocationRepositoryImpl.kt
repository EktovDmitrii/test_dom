package com.custom.rgs_android_dom.data.repositories.location

import android.annotation.SuppressLint
import android.content.Context
import android.os.Looper
import android.util.Log
import com.custom.rgs_android_dom.domain.repositories.LocationRepository
import com.google.android.gms.location.*
import com.yandex.mapkit.geometry.Point
import io.reactivex.Single
import io.reactivex.SingleEmitter
import java.util.concurrent.TimeUnit

class LocationRepositoryImpl(private val context: Context): LocationRepository {

    private val defaultLocation = Point(55.713136, 37.647504)

    @SuppressLint("MissingPermission")
    override fun getLocation(): Single<Point> =
        Single.create { emitter ->
            Log.d("MyLog", "SINGLE CREATE")
            val locationClient = LocationServices.getFusedLocationProviderClient(context)
            locationClient.lastLocation
                .addOnSuccessListener { location ->
                    Log.d("MyLog", "ON SUCCESSS")
                    if (!emitter.isDisposed) {
                        if (location != null) {
                            val locationPoint = Point(location.latitude, location.longitude)
                            emitter.onSuccess(locationPoint)
                        } else {
                            onLastKnownLocationError(locationClient, emitter)
                        }
                    }
                }
                .addOnFailureListener {
                    emitter.onError(Throwable("On failure"))
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
                Log.d("MyLog", "ON loc result")
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

}