package vn.com.fpt.mobiqc.ui.blank

import vn.com.fpt.mobiqc.data.network.model.ResponseLowCaseModel
import vn.com.fpt.mobiqc.data.network.model.ResponseModel
import vn.com.fpt.mobiqc.ui.base.BaseView

/**
 * * Created by Anh Pham on 08/09/2018.     **
 * * Copyright (c) 2018 by FPT Telecom      **
 */
interface CreatePreCheckListContract {
    interface CreatePreCheckListView : BaseView {
        fun loadCreatePreChecklist(response : ResponseModel)
        fun loadSupportListRemainCheck(response: ResponseLowCaseModel)
        fun handleError(error : String)
    }

    interface CreatePreCheckListPresenter {
        fun postSupportListRemainCheck(map: HashMap<String, Any>)
        fun postCreatePreChecklist(map : HashMap<String,Any>)
    }
}