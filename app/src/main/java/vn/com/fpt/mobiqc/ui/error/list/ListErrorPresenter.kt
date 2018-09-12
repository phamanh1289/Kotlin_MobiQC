package vn.com.fpt.mobiqc.ui.error.list

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import vn.com.fpt.mobiqc.data.network.api.ApiIstorageService
import vn.com.fpt.mobiqc.data.network.api.ApiService
import vn.com.fpt.mobiqc.ui.base.BasePresenter
import javax.inject.Inject

/**
 * * Created by Anh Pham on 08/09/2018.     **
 * * Copyright (c) 2018 by FPT Telecom      **
 */
class ListErrorPresenter @Inject constructor(private val apiService: ApiService, private val apiIstorageService: ApiIstorageService) : BasePresenter<ListErrorContract.ListErrorView>(), ListErrorContract.ListErrorPresenter {

    override fun getAlbumCode(token: String, code: String) {
        addSubscribe(apiIstorageService.getAlbum(token, code)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    view?.loadAlbumCode(it)
                }, {
                    view?.handleError(it.message.toString())
                }))
    }

    override fun getErrorInfrastructure(map: HashMap<String, Any>) {
        addSubscribe(apiService.getErrorInfrastructure(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    view?.loadErrorInfrastructure(it)
                }, {
                    view?.handleError(it.message.toString())
                }))
    }


    override fun postUpdateErrorInfrastructure(map: HashMap<String, Any>) {
        addSubscribe(apiService.updateErrorInfrastructure(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    view?.loadUpdateErrorInfrastructure(it)
                }, {
                    view?.handleError(it.message.toString())
                }))
    }

    override fun postSendMail(map: HashMap<String, Any>) {
        addSubscribe(apiService.postSendMail(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    view?.loadSendMail(it)
                }, {
                    view?.handleError(it.message.toString())
                }))
    }
}