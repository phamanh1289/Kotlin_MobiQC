package mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.blank

import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.data.network.model.ResponseModel
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.base.BaseView

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
    }
}