package mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.main

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.data.network.api.ApiIstorageService
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.base.BasePresenter
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.utils.AppUtils
import javax.inject.Inject

/**
 * * Created by Anh Pham on 08/02/2018.     **
 * * Copyright (c) 2018 by FPT Telecom      **
 */
class MainPresenterImp @Inject constructor(val apiIstorageService: ApiIstorageService) : BasePresenter<MainContract.MainView>(), MainContract.MainPresenter {
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