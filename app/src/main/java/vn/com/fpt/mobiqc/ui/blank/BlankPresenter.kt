package vn.com.fpt.mobiqc.ui.blank

import vn.com.fpt.mobiqc.data.network.api.ApiMobiNetService
import vn.com.fpt.mobiqc.data.network.api.ApiService
import vn.com.fpt.mobiqc.data.network.api.ApiWsMobiNetService
import vn.com.fpt.mobiqc.ui.base.BasePresenter
import javax.inject.Inject

/**
 * * Created by Anh Pham on 08/09/2018.     **
 * * Copyright (c) 2018 by FPT Telecom      **
 */
class BlankPresenter @Inject constructor(private val apiService: ApiService, private val apiMobiNet: ApiMobiNetService, private val apiWsMobiNetService: ApiWsMobiNetService): BasePresenter<BlankContract.View>(), BlankContract.Presenter  {
//    override fun getMaintenanceContractDetail(map: HashMap<String, Any>) {
//        addSubscribe(apiMobiNet.getMaintenanceContractDetail(map)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe({
//                    view?.loadMaintenanceContractDetail(it)
//                }, {
//                    view?.handleError(it.message.toString())
//                }))
//    }
}