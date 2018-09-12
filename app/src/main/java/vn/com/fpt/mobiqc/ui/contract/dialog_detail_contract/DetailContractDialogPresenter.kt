package vn.com.fpt.mobiqc.ui.contract.dialog_detail_contract

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import vn.com.fpt.mobiqc.data.network.api.ApiMobiNetService
import vn.com.fpt.mobiqc.data.network.api.ApiService
import vn.com.fpt.mobiqc.ui.base.BasePresenter
import javax.inject.Inject

/**
 * * Created by Anh Pham on 08/09/2018.     **
 * * Copyright (c) 2018 by FPT Telecom      **
 */
class DetailContractDialogPresenter @Inject constructor(private val apiService: ApiService, private val apiMobiNet: ApiMobiNetService) : BasePresenter<DetailContractDialogContract.DetailContractDialogView>(), DetailContractDialogContract.DetailContractDialogPresenter {

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
}