package mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.check_list.all_check_list

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
class AllCheckListPresenter @Inject constructor(private val apiService: ApiService, private val apiMobiNet: ApiMobiNetService, private val apiWsMobiNetService: ApiWsMobiNetService): BasePresenter<AllCheckListContract.AllCheckListView>(), AllCheckListContract.AllCheckListPresenter  {

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