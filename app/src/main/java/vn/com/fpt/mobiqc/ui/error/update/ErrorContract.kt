package vn.com.fpt.mobiqc.ui.error.update

import vn.com.fpt.mobiqc.data.network.model.ResponseModel
import vn.com.fpt.mobiqc.ui.base.BaseView

/**
 * * Created by Anh Pham on 08/09/2018.     **
 * * Copyright (c) 2018 by FPT Telecom      **
 */
interface ErrorContract {
    interface ErrorView : BaseView {
        fun loadCoordinateContract(response: ResponseModel)
        fun loadUpdateError(response: ResponseModel)
        fun handleError(error : String)
    }

    interface ErrorPresenter {
        fun getCoordinateContract(map: HashMap<String, Any>)
        fun postUpdateError(map: HashMap<String, Any>)
    }
}