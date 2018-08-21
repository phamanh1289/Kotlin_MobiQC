package mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.check_list.create_pre_check_list

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.data.network.api.ApiService
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.base.BasePresenter
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.blank.CreatePreCheckListContract
import javax.inject.Inject

/**
 * * Created by Anh Pham on 08/09/2018.     **
 * * Copyright (c) 2018 by FPT Telecom      **
 */
class CreatePreCheckListPresenter @Inject constructor(private val apiService: ApiService) : BasePresenter<CreatePreCheckListContract.CreatePreCheckListView>(), CreatePreCheckListContract.CreatePreCheckListPresenter {

    override fun postCreatePreChecklist(map: HashMap<String, Any>) {
        addSubscribe(apiService.postCreatePreChecklist(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    view?.loadCreatePreChecklist(it)
                }, {
                    view?.handleError(it.message.toString())
                }))
    }

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
}