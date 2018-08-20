package mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.check_list.create_check_list

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.data.network.api.ApiMobiNetService
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.data.network.api.ApiService
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.data.network.api.ApiWsMobiNetService
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.base.BasePresenter
import javax.inject.Inject

/**
 * * Created by Anh Pham on 08/09/2018.     **
 * * Copyright (c) 2018 by FPT Telecom      **
 */
class CreateCheckListPresenter @Inject constructor(private val apiService: ApiService, private val apiMobiNet: ApiMobiNetService, private val apiWsMobiNetService: ApiWsMobiNetService) : BasePresenter<CreateCheckListContract.CreateCheckListView>(), CreateCheckListContract.CreateCheckListPresenter {
    override fun getSubTeamID(map: HashMap<String, Any>) {
        addSubscribe(apiService.getSubTeamID(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    view?.loadSubTeamID(it)
                }, {
                    view?.handleError(it.message.toString())
                }))
    }

    override fun getOwnerTypeByPopManage(map: HashMap<String, Any>) {
        addSubscribe(apiService.getOwnerTypeByPopManage(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    view?.loadOwnerType(it)
                }, {
                    view?.handleError(it.message.toString())
                }))
    }

    override fun getPartnerTimezoneAbilityList(map: HashMap<String, Any>) {
        addSubscribe(apiService.getPartnerTimezoneAbilityList(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    view?.loadPartnerTimezoneAbilityList(it)
                }, {
                    view?.handleError(it.message.toString())
                }))
    }

    override fun getOwnerTypeByInitStatus(map: HashMap<String, Any>) {
        addSubscribe(apiService.getOwnerTypeByInitStatus(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    view?.loadOwnerType(it)
                }, {
                    view?.handleError(it.message.toString())
                }))
    }


}