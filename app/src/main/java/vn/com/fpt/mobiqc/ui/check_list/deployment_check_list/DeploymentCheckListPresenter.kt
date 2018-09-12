package vn.com.fpt.mobiqc.ui.check_list.deployment_check_list

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import vn.com.fpt.mobiqc.data.network.api.ApiService
import vn.com.fpt.mobiqc.ui.base.BasePresenter
import javax.inject.Inject

/**
 * * Created by Anh Pham on 08/09/2018.     **
 * * Copyright (c) 2018 by FPT Telecom      **
 */
class DeploymentCheckListPresenter @Inject constructor(private val apiService: ApiService) : BasePresenter<DeploymentCheckListContract.DeploymentCheckListView>(), DeploymentCheckListContract.DeploymentCheckListPresenter {

    override fun getAllCheckList(map: HashMap<String, Any>) {
        addSubscribe(apiService.getAllCheckList(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    view?.loadAllCheckList(it)
                }, {
                    view?.handleError(it.message.toString())
                }))
    }
}