package mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.check_list.deployment_check_list

import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.data.network.model.ResponseModel
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.base.BaseView

/**
 * * Created by Anh Pham on 08/09/2018.     **
 * * Copyright (c) 2018 by FPT Telecom      **
 */
interface DeploymentCheckListContract {
    interface DeploymentCheckListView : BaseView {
        fun loadAllCheckList(response: ResponseModel)
        fun handleError(error: String)
    }

    interface DeploymentCheckListPresenter {
        fun getAllCheckList(map: HashMap<String, Any>)
    }
}