package vn.com.fpt.mobiqc.ui.maps

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import vn.com.fpt.mobiqc.data.network.api.ApiService
import vn.com.fpt.mobiqc.others.service.LocationService
import vn.com.fpt.mobiqc.ui.base.BasePresenter
import javax.inject.Inject

/**
 * * Created by Anh Pham on 08/09/2018.     **
 * * Copyright (c) 2018 by FPT Telecom      **
 */
class MapsPresenter @Inject constructor(private val apiService: ApiService) : BasePresenter<MapsContract.MapsView>(), MapsContract.MapsPresenter {

    override fun getPolyline(url: String) {
        addSubscribe(Observable.just("")
                .map { LocationService.getLatLngPolyline(url) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    view?.loadPolyline(it)
                }
        )
    }
}