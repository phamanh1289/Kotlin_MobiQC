package mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.maps

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.gson.Gson
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.R
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.data.network.model.PolylineMapModel
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.data.network.model.TitleAndMenuModel
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.others.constant.Constants
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.others.service.LocationService
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.base.BaseFragment
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.utils.AppUtils
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.utils.KeyboardUtils
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import javax.inject.Inject


/**
 * * Created by Anh Pham on 08/30/2018.     **
 * * Copyright (c) 2018 by FPT Telecom      **
 */
class MapsFragment : BaseFragment(), MapsContract.MapsView, OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private var mapFragment: SupportMapFragment? = null
    private lateinit var mLocationManager: LocationService
    private var markerGuest: Marker? = null
    private var markerMyLoction: Marker? = null
    private var lat = 0.0
    private var lng = 0.0
    private var mBound: LatLngBounds? = null

    @Inject
    lateinit var presenter: MapsPresenter

    companion object {
        fun newInstance(contractNumber: String, lat: String, lng: String): MapsFragment {
            val args = Bundle()
            args.putString(Constants.ARG_CONTRACT_NUMBER, contractNumber)
            args.putString(Constants.ARG_LAT, lat)
            args.putString(Constants.ARG_LNG, lng)
            val fragment = MapsFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getActivityComponent().inject(this)
        presenter.onAttach(this)
        activity?.let { KeyboardUtils.setupUI(view, activity = it) }
        initView()
    }

    private fun initView() {
        mapFragment = childFragmentManager.findFragmentById(R.id.fragMaps_viewMap) as SupportMapFragment
        mapFragment?.getMapAsync(this)
        mLocationManager = LocationService(context)
        mLocationManager.getLocationManager()
    }

    private fun handleArgument() {
        arguments?.let {
            lat = it.getString(Constants.ARG_LAT).toDouble()
            lng = it.getString(Constants.ARG_LNG).toDouble()
            val title = it.getString(Constants.ARG_TITLE) ?: ""
            markerGuest = addMarkerToMap(title)
            setTitle(TitleAndMenuModel(title = title, status = false))
        }
        val builder = LatLngBounds.Builder()
        builder.include(markerGuest?.position)
        builder.include(markerMyLoction?.position)
        mBound = builder.build()
        mMap.setOnMapLoadedCallback { mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(mBound, 150)) }
        DownloadTask().execute(getDirectionsUrl(markerMyLoction?.position!!, markerGuest?.position!!))
    }

    override fun onMapReady(map: GoogleMap?) {
        map?.let {
            mMap = it
            if (ActivityCompat.checkSelfPermission(context!!, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                mMap.isMyLocationEnabled = true
            if (mLocationManager.getStatusGps()!!) {
                val myLocation = mLocationManager.getLocationUser()
                lat = myLocation.latitude
                lng = myLocation.longitude
                markerMyLoction = addMarkerToMap("")
                mMap.clear()
            }
            handleArgument()
        }
    }

    private fun addMarkerToMap(title: String): Marker {
        return mMap.addMarker(MarkerOptions().position(LatLng(lat, lng)).title(title))
    }

    override fun handleError(error: String) {
        hideLoading()
        AppUtils.showDialog(fragmentManager, content = error, confirmDialogInterface = null)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onDetach()
    }

    private fun getDirectionsUrl(origin: LatLng, dest: LatLng): String {
        // Origin of route
        val str_origin = "origin=" + origin.latitude + "," + origin.longitude
        // Destination of route
        val str_dest = "destination=" + dest.latitude + "," + dest.longitude
        // Sensor enabled
        val sensor = "sensor=false"
        val mode = "mode=driving"
        // Building the parameters to the web service
        val parameters = "$str_origin&$str_dest&$sensor&$mode"
        // Output format
        val output = "json"
        // Building the url to the web service
        return "https://maps.googleapis.com/maps/api/directions/$output?$parameters"
    }

    private inner class DownloadTask : AsyncTask<String, String, String>() {
        override fun doInBackground(vararg url: String): String {
            var data = ""
            try {
                data = downloadUrl(url[0])
            } catch (e: Exception) {
            }
            return data
        }

        override fun onPostExecute(result: String) {
            super.onPostExecute(result)
            drawPolylineToMap(result)
        }
    }

    @Throws(IOException::class)
    private fun downloadUrl(strUrl: String): String {
        var data = ""
        var iStream: InputStream? = null
        var urlConnection: HttpURLConnection? = null
        try {
            val url = URL(strUrl)
            urlConnection = url.openConnection() as HttpURLConnection
            urlConnection.connect()
            iStream = urlConnection.inputStream
            val br = BufferedReader(InputStreamReader(iStream))
            val sb = StringBuffer()
            var line = br.readLine()
            while (line != null) {
                sb.append(line)
                line = br.readLine()
            }
            data = sb.toString()
            br.close()
        } catch (e: Exception) {
        } finally {
            iStream!!.close()
            urlConnection!!.disconnect()
        }
        return data
    }


    private fun drawPolylineToMap(data: String) {
        val model: PolylineMapModel? = Gson().fromJson(data, PolylineMapModel::class.java)
        model?.let {
            val item = model.routes[Constants.FIRST_ITEM]
            val leg = item.legs[Constants.FIRST_ITEM]
            leg.steps.forEach { itemStep ->
                mMap.addPolyline(PolylineOptions()
                        .add(LatLng(itemStep.start_location.lat, itemStep.start_location.lng), LatLng(itemStep.end_location.lat, itemStep.end_location.lng)).width(5f)
                        .color(Color.RED))
            }
        }
    }
}