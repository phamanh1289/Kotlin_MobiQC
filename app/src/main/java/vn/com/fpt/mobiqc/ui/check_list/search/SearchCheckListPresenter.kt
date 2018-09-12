package vn.com.fpt.mobiqc.ui.blank

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import vn.com.fpt.mobiqc.data.network.api.ApiService
import vn.com.fpt.mobiqc.ui.base.BasePresenter
import javax.inject.Inject

/**
 * * Created by Anh Pham on 08/09/2018.     **
 * * Copyright (c) 2018 by FPT Telecom      **
 */
class SearchCheckListPresenter @Inject constructor(private val apiService: ApiService) : BasePresenter<SearchCheckListContract.SearchCheckListView>(), SearchCheckListContract.SearchCheckListPresenter {
    override fun getReportControlErrorDetail(map: HashMap<String, Any>) {
        addSubscribe(apiService.getControlErrorDetailReport(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    view?.loadListContract(it)
                }, {
                    view?.handleError(it.message.toString())
                }))
    }

    override fun getListContract(map: HashMap<String, Any>) {
        addSubscribe(apiService.getListContract(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    view?.loadListContract(it)
                }, {
                    view?.handleError(it.message.toString())
                }))
    }


}