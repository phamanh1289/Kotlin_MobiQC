package vn.com.fpt.mobiqc.ui.error.create

import vn.com.fpt.mobiqc.data.network.model.ResponseModel
import vn.com.fpt.mobiqc.ui.base.BaseView

/**
 * * Created by Anh Pham on 08/09/2018.     **
 * * Copyright (c) 2018 by FPT Telecom      **
 */
interface CreateErrorContract {
    interface CreateErrorView : BaseView {
        fun loadInsertErrorInfrastructure(response: ResponseModel)
        fun loadUpdateErrorInfrastructure(response: ResponseModel)
        fun handleError(error: String)
    }

    interface CreateErrorPresenter {
        fun postInsertErrorInfrastructure(map: HashMap<String, Any>)
        fun postUpdateErrorInfrastructure(map: HashMap<String, Any>)
    }
}