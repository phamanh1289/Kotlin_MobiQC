package vn.com.fpt.mobiqc.ui.check_list.create_check_list

import vn.com.fpt.mobiqc.data.network.model.ResponseLowCaseModel
import vn.com.fpt.mobiqc.ui.base.BaseView

/**
 * * Created by Anh Pham on 08/09/2018.     **
 * * Copyright (c) 2018 by FPT Telecom      **
 */
interface CreateCheckListContract {
    interface CreateCheckListView : BaseView {
        fun loadSupportListRemainCheck(response: ResponseLowCaseModel)
        fun loadCheckRemainPTC(response: ResponseLowCaseModel)
        fun loadCreateChecklist(response: ResponseLowCaseModel)
        fun loadSupportListAssignInsert(response: ResponseLowCaseModel)
        fun loadSubTeamID(response: ResponseLowCaseModel)
        fun loadOwnerType(response: ResponseLowCaseModel)
        fun loadPartnerTimezoneAbilityList(response: ResponseLowCaseModel)
        fun handleError(error: String)
    }

    interface CreateCheckListPresenter {
        fun postSupportListRemainCheck(map: HashMap<String, Any>)
        fun postCheckRemainPTC(map: HashMap<String, Any>)
        fun postCreateChecklist(map: HashMap<String, Any>)
        fun postSupportListAssignInsert(map: HashMap<String, Any>)
        fun getOwnerTypeByInitStatus(map: HashMap<String, Any>)
        fun getSubTeamID(map: HashMap<String, Any>)
        fun getOwnerTypeByPopManage(map: HashMap<String, Any>)
        fun getPartnerTimezoneAbilityList(map: HashMap<String, Any>)
    }
}