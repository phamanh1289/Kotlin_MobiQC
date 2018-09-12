package vn.com.fpt.mobiqc.ui.reprot

import vn.com.fpt.mobiqc.data.network.model.ResponseModel
import vn.com.fpt.mobiqc.ui.base.BaseView

/**
 * * Created by Anh Pham on 08/09/2018.     **
 * * Copyright (c) 2018 by FPT Telecom      **
 */
interface ReportContract {
    interface ReportView : BaseView {
        fun loadControlErrorReport(response: ResponseModel)
        fun handleError(error: String)
    }

    interface ReportPresenter {
        fun getControlErrorReport(map: HashMap<String, Any>)
    }
}