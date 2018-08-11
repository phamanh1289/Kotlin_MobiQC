package mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.contract.info_contract

import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.data.network.model.ResponseModel
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.base.BaseView

/**
 * * Created by Anh Pham on 08/08/2018.     **
 * * Copyright (c) 2018 by FPT Telecom      **
 */
interface InfoContract {
    interface InfoContractView : BaseView {
        fun loadCompletedContract(response : ResponseModel)
        fun loadDeploymentContractGroup(response : ResponseModel)
        fun loadDeploymentContractList(response : ResponseModel)
        fun loadMaintenanceContractGroup(response : ResponseModel)
        fun loadMaintenanceContractList(response : ResponseModel)
        fun handleError(error : String)
    }

    interface InfoContractPresenter {
        fun getCompletedContract(map: HashMap<String, Any>)
        fun getDeploymentContractGroup(map: HashMap<String, Any>)
        fun getDeploymentContractList(map: HashMap<String, Any>)
        fun getMaintenanceContractGroup(map: HashMap<String, Any>)
        fun getMaintenanceContractList(map: HashMap<String, Any>)
    }
}