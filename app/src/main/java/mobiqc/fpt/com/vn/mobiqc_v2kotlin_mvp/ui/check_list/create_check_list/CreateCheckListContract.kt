package mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.check_list.create_check_list

import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.data.network.model.ResponseLowCaseModel
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.base.BaseView

/**
 * * Created by Anh Pham on 08/09/2018.     **
 * * Copyright (c) 2018 by FPT Telecom      **
 */
interface CreateCheckListContract {
    interface CreateCheckListView : BaseView {
        fun loadSubTeamID(response: ResponseLowCaseModel)
        fun loadOwnerType(response: ResponseLowCaseModel)
        fun loadPartnerTimezoneAbilityList(response: ResponseLowCaseModel)
        fun handleError(error: String)
    }

    interface CreateCheckListPresenter {
        fun getOwnerTypeByInitStatus(map: HashMap<String, Any>)
        fun getSubTeamID(map: HashMap<String, Any>)
        fun getOwnerTypeByPopManage(map: HashMap<String, Any>)
        fun getPartnerTimezoneAbilityList(map: HashMap<String, Any>)
    }
}