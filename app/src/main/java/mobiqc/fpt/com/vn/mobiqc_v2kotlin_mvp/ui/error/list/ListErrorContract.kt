package mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.error.list

import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.data.network.model.ResponseModel
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.data.network.model.ResultEmailModel
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.base.BaseView

/**
 * * Created by Anh Pham on 08/09/2018.     **
 * * Copyright (c) 2018 by FPT Telecom      **
 */
interface ListErrorContract {
    interface ListErrorView : BaseView {
        fun loadErrorInfrastructure(response: ResponseModel)
        fun loadUpdateErrorInfrastructure(response: ResponseModel)
        fun loadSendMail(response: ResultEmailModel)
        fun loadAlbumCode(response: ResponseModel)
        fun handleError(error: String)
    }

    interface ListErrorPresenter {
        fun getErrorInfrastructure(map: HashMap<String, Any>)
        fun postUpdateErrorInfrastructure(map: HashMap<String, Any>)
        fun postSendMail(map: HashMap<String, Any>)
        fun getAlbumCode(token:String,code: String)
    }
}