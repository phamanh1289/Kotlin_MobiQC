package mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.check_list.create_check_list

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.data.network.api.ApiService
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.base.BasePresenter
import javax.inject.Inject

/**
 * * Created by Anh Pham on 08/09/2018.     **
 * * Copyright (c) 2018 by FPT Telecom      **
 */
class CreateCheckListPresenter @Inject constructor(private val apiService: ApiService) : BasePresenter<CreateCheckListContract.CreateCheckListView>(), CreateCheckListContract.CreateCheckListPresenter {
    override fun postSupportListRemainCheck(map: HashMap<String, Any>) {
        addSubscribe(apiService.postSupportListRemainCheck(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    view?.loadSupportListRemainCheck(it)
                }, {
                    view?.handleError(it.message.toString())
                }))
    }

    override fun postCheckRemainPTC(map: HashMap<String, Any>) {
        addSubscribe(apiService.postCheckRemainPTC(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    view?.loadCheckRemainPTC(it)
                }, {
                    view?.handleError(it.message.toString())
                }))
    }

    override fun postCreateChecklist(map: HashMap<String, Any>) {
        addSubscribe(apiService.postCreateChecklist(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    view?.loadCreateChecklist(it)
                }, {
                    view?.handleError(it.message.toString())
                }))
    }

    override fun postSupportListAssignInsert(map: HashMap<String, Any>) {
        addSubscribe(apiService.postSupportListAssignInsert(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    view?.loadSupportListAssignInsert(it)
                }, {
                    view?.handleError(it.message.toString())
                }))
    }

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