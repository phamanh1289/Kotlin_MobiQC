package vn.com.fpt.mobiqc.ui.error.create

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import vn.com.fpt.mobiqc.data.network.api.ApiService
import vn.com.fpt.mobiqc.ui.base.BasePresenter
import javax.inject.Inject

/**
 * * Created by Anh Pham on 08/09/2018.     **
 * * Copyright (c) 2018 by FPT Telecom      **
 */
class CreateErrorPresenter @Inject constructor(private val apiService: ApiService) : BasePresenter<CreateErrorContract.CreateErrorView>(), CreateErrorContract.CreateErrorPresenter {

    override fun postInsertErrorInfrastructure(map: HashMap<String, Any>) {
        addSubscribe(apiService.postInsertErrorInfrastructure(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    view?.loadInsertErrorInfrastructure(it)
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

}