package vn.com.fpt.mobiqc.others.service

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException


/**
 * * Created by Anh Pham on 08/16/2018.     **
 * * Copyright (c) 2018 by FPT Telecom      **
 */
class LocationService(val context: Context?) {

    private var typeProvider = ""

    companion object {
        const val ONE_MIN = 1000 * 60 * 1
        const val TEN_METERS = 1
        fun getLatLngPolyline(url: String): String {
            val client = OkHttpClient()
            var result = ""
            try {
                val request = Request.Builder().url(url).build()
                val response = client.newCall(request).execute()
                val responseBody = response.body()
                if (responseBody != null) result = responseBody.string()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            return result
        }
    }

    private var locationManager: LocationManager? = null
    private var bestLocation: Location? = null

    fun getLocationManager() {
        context?.let {
            locationManager = it.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            locationManager?.let { location ->
                setLocationUser(it, location)
            }
        }
    }

    private fun setLocationUser(context: Context, locationManager: LocationManager) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            val listProvider: List<String> = locationManager.getProviders(true)
            listProvider.forEach {
                val location = locationManager.getLastKnownLocation(it)
                location?.let { pointer ->
                    if (bestLocation == null || pointer.accuracy < bestLocation?.accuracy!!) {
                        typeProvider = it
                        bestLocation = pointer
                    }
                }
            }
            if (typeProvider.isNotBlank())
                locationManager.requestLocationUpdates(typeProvider, ONE_MIN.toLong(), TEN_METERS.toFloat(), locationListener)
        }
    }

    fun getStatusGps(): Boolean? {
        return locationManager?.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    fun getLocationUser(): Location? {
        return bestLocation
    }

    fun stopListenerLocation() {
        locationManager?.removeUpdates(locationListener)
    }

    private val locationListener: LocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            this@LocationService.bestLocation = location
        }

        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
        override fun onProviderEnabled(provider: String) {}
        override fun onProviderDisabled(provider: String) {}
    }

}