package vn.com.fpt.mobiqc.ui.error.update

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import vn.com.fpt.mobiqc.data.network.api.ApiMobiNetService
import vn.com.fpt.mobiqc.data.network.api.ApiService
import vn.com.fpt.mobiqc.data.network.api.ApiWsMobiNetService
import vn.com.fpt.mobiqc.ui.base.BasePresenter
import javax.inject.Inject

/**
 * * Created by Anh Pham on 08/09/2018.     **
 * * Copyright (c) 2018 by FPT Telecom      **
 */
class ErrorPresenter @Inject constructor(private val apiService: ApiService, private val apiMobiNet: ApiMobiNetService, private val apiWsMobiNetService: ApiWsMobiNetService) : BasePresenter<ErrorContract.ErrorView>(), ErrorContract.ErrorPresenter {

    override fun getCoordinateContract(map: HashMap<String, Any>) {

        addSubscribe(apiService.getCoordinateContract(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    view?.loadCoordinateContract(it)
                }, {
                    view?.handleError(it.message.toString())
                }))
    }

    override fun postUpdateError(map: HashMap<String, Any>) {
        addSubscribe(apiService.postUpdateError(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    view?.loadUpdateError(it)
                }, {
                    view?.handleError(it.message.toString())
                }))
    }
}