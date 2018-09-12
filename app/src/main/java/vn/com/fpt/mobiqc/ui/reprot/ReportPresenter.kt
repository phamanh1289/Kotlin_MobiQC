package vn.com.fpt.mobiqc.ui.reprot

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import vn.com.fpt.mobiqc.data.network.api.ApiService
import vn.com.fpt.mobiqc.ui.base.BasePresenter
import javax.inject.Inject

/**
 * * Created by Anh Pham on 08/09/2018.     **
 * * Copyright (c) 2018 by FPT Telecom      **
 */
class ReportPresenter @Inject constructor(private val apiService: ApiService) : BasePresenter<ReportContract.ReportView>(), ReportContract.ReportPresenter {

    override fun getControlErrorReport(map: HashMap<String, Any>) {
        addSubscribe(apiService.getControlErrorReport(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    view?.loadControlErrorReport(it)
                }, {
                    view?.handleError(it.message.toString())
                }))
    }
}