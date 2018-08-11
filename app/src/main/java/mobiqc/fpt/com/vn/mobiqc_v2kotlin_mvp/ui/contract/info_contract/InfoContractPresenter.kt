package mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.contract.info_contract

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.data.network.api.ApiMobiNetService
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.data.network.api.ApiService
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.data.network.api.ApiWsMobiNetService
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.base.BasePresenter
import javax.inject.Inject

/**
 * * Created by Anh Pham on 08/08/2018.     **
 * * Copyright (c) 2018 by FPT Telecom      **
 */
class InfoContractPresenter @Inject constructor(private val apiService: ApiService, private val apiMobiNet: ApiMobiNetService, private val apiWsMobiNetService: ApiWsMobiNetService) : BasePresenter<InfoContract.InfoContractView>(), InfoContract.InfoContractPresenter {

    override fun getCompletedContract(map: HashMap<String, Any>) {
        addSubscribe(apiService.getCompletedContract(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    view?.loadCompletedContract(it)
                }, {
                    view?.handleError(it.message.toString())
                }))
    }

    override fun getDeploymentContractGroup(map: HashMap<String, Any>) {
        addSubscribe(apiWsMobiNetService.getDeploymentContractGroup(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    view?.loadDeploymentContractGroup(it)
                }, {
                    view?.handleError(it.message.toString())
                }))
    }

    override fun getDeploymentContractList(map: HashMap<String, Any>) {
        addSubscribe(apiWsMobiNetService.getDeploymentContractList(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    view?.loadDeploymentContractList(it)
                }, {
                    view?.handleError(it.message.toString())
                }))
    }

    override fun getMaintenanceContractList(map: HashMap<String, Any>) {
        addSubscribe(apiMobiNet.getMaintenanceContractList(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    view?.loadMaintenanceContractList(it)
                }, {
                    view?.handleError(it.message.toString())
                }))
    }

    override fun getMaintenanceContractGroup(map: HashMap<String, Any>) {
        addSubscribe(apiMobiNet.getMaintenanceContractGroup(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    view?.loadMaintenanceContractGroup(it)
                }, {
                    view?.handleError(it.message.toString())
                }))
    }
}