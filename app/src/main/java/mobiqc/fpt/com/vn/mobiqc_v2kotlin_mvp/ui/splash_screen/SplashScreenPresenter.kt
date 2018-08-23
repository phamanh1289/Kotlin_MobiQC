package mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.splash_screen

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.data.network.api.ApiService
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.data.network.model.ErrorDataModel
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.data.realm.error.ErrorRealmManager
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.others.service.DownloadService
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.base.BasePresenter
import javax.inject.Inject

/**
 * * Created by Anh Pham on 08/06/2018.     **
 * * Copyright (c) 2018 by FPT Telecom      **
 */
class SplashScreenPresenter @Inject constructor(val apiService: ApiService) : BasePresenter<SplashScreenContract.SplashScreenView>(), SplashScreenContract.SplashScreenPresenter {
    override fun getNewFileVersion(url: String) {
        DownloadService(url, view).initDownload()
    }

    override fun getAppVersion(map: HashMap<String, Any>) {
        addSubscribe(apiService.getAppVersion(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    view?.loadAppVersion(it)
                }, {
                    view?.handleError(it.message.toString())
                }))
    }

    override fun postCheckImei(map: HashMap<String, Any>) {
        addSubscribe(apiService.postCheckImei(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    view?.loadCheckImei(it)
                }, {
                    view?.handleError(it.message.toString())
                }))
    }

    override fun getNewErrorData(map: HashMap<String, Any>) {
        addSubscribe(apiService.getNewErrorData(map)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ item ->
                    var maxDate: ErrorDataModel? = null
                    if (item.data.size != 0) {
                        item.data.map { it ->
                            if (ErrorRealmManager().findIdError(it.id))
                                ErrorRealmManager().updateError(it)
                            else ErrorRealmManager().insertError(it)
                        }
                        maxDate = item.data.maxBy { it.date }
                    }
                    view?.loadNewErrorData(item, maxDate?.date)
                }, {
                    view?.handleError(it.message.toString())
                }))
    }
}