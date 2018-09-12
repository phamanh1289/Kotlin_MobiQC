package vn.com.fpt.mobiqc.ui.contract.detail_contract

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import vn.com.fpt.mobiqc.data.network.api.ApiMobiNetService
import vn.com.fpt.mobiqc.data.network.api.ApiService
import vn.com.fpt.mobiqc.data.network.api.ApiWsMobiNetService
import vn.com.fpt.mobiqc.ui.base.BasePresenter
import javax.inject.Inject

/**
 * * Created by Anh Pham on 08/08/2018.     **
 * * Copyright (c) 2018 by FPT Telecom      **
 */
class DetailContractPresenter @Inject constructor(private val apiService: ApiService, private val apiMobiNet: ApiMobiNetService, private val apiWsMobiNetService: ApiWsMobiNetService) : BasePresenter<DetailContract.DetailContractView>(), DetailContract.DetailContractPresenter {

    override fun getFinishContractDetail(map: HashMap<String, Any>) {
        addSubscribe(apiService.getFinishContractDetail(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    view?.loadFinishContractDetail(it)
                }, {
                    view?.handleError(it.message.toString())
                }))
    }

    override fun getAllPhoneNumber(map: HashMap<String, Any>) {
        addSubscribe(apiService.getAllPhoneNumber(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    view?.loadAllPhoneNumber(it)
                }, {
                    view?.handleError(it.message.toString())
                }))
    }

    override fun getCheckListDetail(map: HashMap<String, Any>) {
        addSubscribe(apiService.getCheckListDetail(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    view?.loadCheckListDetail(it)
                }, {
                    view?.handleError(it.message.toString())
                }))
    }

    override fun getMaintenanceContractDetail(map: HashMap<String, Any>) {
        addSubscribe(apiMobiNet.getMaintenanceContractDetail(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    view?.loadMaintenanceContractDetail(it)
                }, {
                    view?.handleError(it.message.toString())
                }))
    }

    override fun getDeploymentContractDetail(map: HashMap<String, Any>) {
        addSubscribe(apiWsMobiNetService.getDeploymentContractDetail(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    view?.loadDeploymentContractDetail(it)
                }, {
                    view?.handleError(it.message.toString())
                }))
    }

    override fun getDepositsContract(map: HashMap<String, Any>) {
        addSubscribe(apiService.getDepositsContract(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    view?.loadDepositsContract(it)
                }, {
                    view?.handleError(it.message.toString())
                }))
    }

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

    override fun getPortViewInfoCollection(map: HashMap<String, Any>) {
        addSubscribe(apiMobiNet.getPortViewInfoCollection(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    view?.loadPortViewInfoCollection(it)
                }, {
                    view?.handleError(it.message.toString())
                }))
    }
}