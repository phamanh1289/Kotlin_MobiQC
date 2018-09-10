package mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.maps

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.provider.Settings
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
import com.google.maps.android.PolyUtil
import com.google.maps.android.SphericalUtil
import kotlinx.android.synthetic.main.fragment_maps.*
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.R
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.data.interfaces.ConfirmDialogInterface
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.data.network.model.PolylineMapModel
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.data.network.model.TitleAndMenuModel
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.others.constant.Constants
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.others.service.LocationService
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.base.BaseFragment
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.utils.AppUtils
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.utils.KeyboardUtils
import javax.inject.Inject


/**
 * * Created by Anh Pham on 08/30/2018.     **
 * * Copyright (c) 2018 by FPT Telecom      **
 */
class MapsFragment : BaseFragment(), MapsContract.MapsView, OnMapReadyCallback {

    private var mapFragment: SupportMapFragment? = null
    private lateinit var mLocationManager: LocationService
    private var markerGuest: Marker? = null
    private var mMyLocation: LatLng? = null
    private var lat = 0.0
    private var lng = 0.0
    private var mBound: LatLngBounds? = null
    private var contractNumber = ""
    private var fullName = ""
    private var address = ""
    @Inject
    lateinit var presenter: MapsPresenter

    companion object {
        const val CHECK_GPS = 1010
        private lateinit var mMap: GoogleMap
        fun newInstance(contractNumber: String, fullName: String, lat: String, lng: String, address: String): MapsFragment {
            val args = Bundle()
            args.putString(Constants.ARG_CONTRACT_NUMBER, contractNumber)
            args.putString(Constants.ARG_TITLE, fullName)
            args.putString(Constants.ARG_LAT, lat)
            args.putString(Constants.ARG_LNG, lng)
            args.putString(Constants.ARG_LOCATION, address)
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
        showLoading()
        getMyCurrentLocation()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CHECK_GPS)
            if (mLocationManager.getStatusGps()!!) {
                enableMyLocation()
                drawPolyline()
            } else
                AppUtils.showDialog(fragmentManager, content = getString(R.string.notify_off_GPS_again), confirmDialogInterface = object : ConfirmDialogInterface {
                    override fun onClickOk() {
                        activity?.onBackPressed()
                    }

                    override fun onClickCancel() {
                    }

                })
    }

    private fun enableMyLocation() {
        if (ActivityCompat.checkSelfPermission(context!!, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.isMyLocationEnabled = true
            mMap.setOnMyLocationButtonClickListener {
                showLoading()
                mMap.clear()
                mMap.addMarker(MarkerOptions().position(LatLng(lat, lng)).title("$contractNumber - $fullName"))
                markerGuest?.snippet = address
                getMyCurrentLocation()
                mMyLocation?.let {
                    presenter.getPolyline(AppUtils.getUrlPolyline(context, it, markerGuest?.position!!))
                }
                false
            }
        }
    }

    private fun drawPolyline() {
        if (!mLocationManager.getStatusGps()!!) {
            AppUtils.showDialog(fragmentManager, content = getString(R.string.notify_off_GPS), actionCancel = true, confirmDialogInterface = object : ConfirmDialogInterface {
                override fun onClickOk() {
                    val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                    startActivityForResult(intent, CHECK_GPS)
                }

                override fun onClickCancel() {
                    activity?.onBackPressed()
                }

            })
            hideLoading()
        } else {
            getMyCurrentLocation()
            handleArgument()
        }
    }

    override fun onMapReady(map: GoogleMap?) {
        map?.let {
            mMap = it
            enableMyLocation()
            drawPolyline()
        }
    }

    private fun handleArgument() {
        arguments?.let {
            lat = it.getString(Constants.ARG_LAT).toDouble()
            lng = it.getString(Constants.ARG_LNG).toDouble()
            contractNumber = it.getString(Constants.ARG_CONTRACT_NUMBER) ?: ""
            fullName = it.getString(Constants.ARG_TITLE) ?: ""
            address = it.getString(Constants.ARG_LOCATION) ?: ""
            markerGuest = mMap.addMarker(MarkerOptions().position(LatLng(lat, lng)).title("$contractNumber - $fullName"))
            markerGuest?.snippet = address
            setTitle(TitleAndMenuModel(title = contractNumber, status = false))
        }
        mMyLocation?.let {
            val builder = LatLngBounds.Builder()
            builder.include(markerGuest?.position)
            builder.include(it)
            mBound = builder.build()
            fragMaps_tvDistance.run {
                val distance = SphericalUtil.computeDistanceBetween(it, markerGuest?.position)
                text = getString(R.string.distance, AppUtils.changeFormatDistance(distance))
            }
            mMap.setOnMapLoadedCallback { mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(mBound, 150)) }
            presenter.getPolyline(AppUtils.getUrlPolyline(context, it, markerGuest?.position!!))
        }
    }

    private fun getMyCurrentLocation() {
        mLocationManager.getLocationManager()
        val myLocation = mLocationManager.getLocationUser()
        myLocation?.let {
            mMyLocation = LatLng(it.latitude, it.longitude)
        }
    }

    private fun drawPolylineToMap(data: String) {
        val model: PolylineMapModel? = Gson().fromJson(data, PolylineMapModel::class.java)
        model?.let {
            val item = it.routes[Constants.FIRST_ITEM]
            val overviewPolyline = item.overview_polyline
            val listLatLng = PolyUtil.decode(overviewPolyline.points)
            var startLocation = listLatLng[Constants.FIRST_ITEM]
            listLatLng.forEach { itemLatLng ->
                mMap.addPolyline(PolylineOptions()
                        .add(LatLng(startLocation.latitude, startLocation.longitude), LatLng(itemLatLng.latitude, itemLatLng.longitude)).width(8f)
                        .color(Color.RED))
                startLocation = itemLatLng
            }
        }
        hideLoading()
    }

    override fun loadPolyline(data: String) {
        drawPolylineToMap(data)
    }

    override fun handleError(error: String) {
        hideLoading()
        AppUtils.showDialog(fragmentManager, content = error, confirmDialogInterface = null)
    }

    override fun onDestroy() {
        super.onDestroy()
        mLocationManager.stopListenerLocation()
        presenter.onDetach()
    }
}