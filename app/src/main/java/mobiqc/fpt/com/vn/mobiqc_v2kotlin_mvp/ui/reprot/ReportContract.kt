package mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.reprot

import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.data.network.model.ResponseModel
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.base.BaseView

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