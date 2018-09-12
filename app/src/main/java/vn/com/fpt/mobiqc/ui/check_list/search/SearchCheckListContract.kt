package vn.com.fpt.mobiqc.ui.blank

import vn.com.fpt.mobiqc.data.network.model.ResponseModel
import vn.com.fpt.mobiqc.ui.base.BaseView

/**
 * * Created by Anh Pham on 08/09/2018.     **
 * * Copyright (c) 2018 by FPT Telecom      **
 */
interface SearchCheckListContract {
    interface SearchCheckListView : BaseView {
        fun loadListContract(response: ResponseModel)
        fun handleError(error: String)
    }

    interface SearchCheckListPresenter {
        fun getListContract(map: HashMap<String, Any>)
        fun getReportControlErrorDetail(map: HashMap<String, Any>)
    }
}