package vn.com.fpt.mobiqc.ui.main

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import vn.com.fpt.mobiqc.data.network.api.ApiIstorageService
import vn.com.fpt.mobiqc.ui.base.BasePresenter
import vn.com.fpt.mobiqc.utils.AppUtils
import javax.inject.Inject

/**
 * * Created by Anh Pham on 08/02/2018.     **
 * * Copyright (c) 2018 by FPT Telecom      **
 */
class MainActivityPresenter @Inject constructor(private val apiIstorageService: ApiIstorageService) : BasePresenter<MainActivityContract.MainView>(), MainActivityContract.MainPresenter {
    override fun getAssetToken(map: HashMap<String, Any>) {
        addSubscribe(apiIstorageService.getAccessToken(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    view?.loadAssetToken(it)
                }, {
                    view?.handleError(it.message.toString())
                }))
    }

    override fun getIpWan() {
        addSubscribe(Observable.just("")
                .map { AppUtils.getExternalIp() }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    view?.loadIpWan(it)
                }
        )
    }

}