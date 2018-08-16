package mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.others.service

import android.Manifest
import android.content.Context
import android.content.Context.LOCATION_SERVICE
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat

/**
 * * Created by Anh Pham on 08/16/2018.     **
 * * Copyright (c) 2018 by FPT Telecom      **
 */
class LocationService(val context: Context?) {

    companion object {
        const val ONE_MIN = 1000 * 60 * 1
        const val TEN_METERS = 1
    }

    private var locationManager: LocationManager? = null
    private lateinit var location: Location

    fun getLocationManager() {
        try {
            locationManager = context?.getSystemService(LOCATION_SERVICE) as LocationManager
            locationManager?.let {
                if (getNetworkLocation()!!)
                    setLocationUser(context, it , LocationManager.NETWORK_PROVIDER)
                if (getStatusGps()!!)
                    setLocationUser(context, it,LocationManager.GPS_PROVIDER)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun setLocationUser(context: Context?,locationManager: LocationManager, type: String) {
        context?.let { it ->
            if (ActivityCompat.checkSelfPermission(it, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locationManager.requestLocationUpdates(type, ONE_MIN.toLong(), TEN_METERS.toFloat(), locationListener)
                location = locationManager.getLastKnownLocation(type)
            }
        }
    }

    fun getStatusGps(): Boolean? {
        return locationManager?.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    fun getNetworkLocation(): Boolean? {
        return locationManager?.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    fun getLocationUser(): Location {
        return location
    }

    fun stopListtenerLocation() {
        locationManager?.removeUpdates(locationListener)
    }

    private val locationListener: LocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            this@LocationService.location = location
        }

        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
        override fun onProviderEnabled(provider: String) {}
        override fun onProviderDisabled(provider: String) {}
    }
}