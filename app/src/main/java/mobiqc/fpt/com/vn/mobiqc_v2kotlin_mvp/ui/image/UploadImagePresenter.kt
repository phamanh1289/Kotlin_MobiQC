package mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.image

import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.data.network.api.ApiMobiNetService
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.data.network.api.ApiService
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.data.network.api.ApiWsMobiNetService
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.base.BasePresenter
import javax.inject.Inject

/**
 * * Created by Anh Pham on 08/09/2018.     **
 * * Copyright (c) 2018 by FPT Telecom      **
 */
class UploadImagePresenter @Inject constructor(private val apiService: ApiService, private val apiMobiNet: ApiMobiNetService, private val apiWsMobiNetService: ApiWsMobiNetService): BasePresenter<UploadImageContract.UploadImageView>(), UploadImageContract.UploadImagePresenter  {

//    override fun getMaintenanceContractDetail(map: HashMap<String, Any>) {
//        addSubscribe(apiMobiNet.getMaintenanceContractDetail(map)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe({
//                    view?.loadMaintenanceContractDetail(it)
//                }, {
//                    view?.handleError(it.message.toString())
//                }))
//    }
}