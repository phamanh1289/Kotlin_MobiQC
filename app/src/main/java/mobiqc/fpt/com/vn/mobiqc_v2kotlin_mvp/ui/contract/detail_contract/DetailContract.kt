package mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.contract.detail_contract

import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.data.network.model.ResponseModel
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.base.BaseView

/**
 * * Created by Anh Pham on 08/08/2018.     **
 * * Copyright (c) 2018 by FPT Telecom      **
 */
interface DetailContract {
    interface DetailContractView : BaseView {
        fun loadDeploymentContractDetail(response: ResponseModel)
        fun loadMaintenanceContractDetail(response: ResponseModel)
        fun loadCheckListDetail(response: ResponseModel)
        fun loadFinishContractDetail(response: ResponseModel)
        fun loadDepositsContract(response: ResponseModel)
        fun loadCoordinateContract(response: ResponseModel)
        fun loadPortViewInfoCollection(response: ResponseModel)
        fun handleError(error: String)
    }

    interface DetailContractPresenter {
        fun getDeploymentContractDetail(map: HashMap<String, Any>)
        fun getMaintenanceContractDetail(map: HashMap<String, Any>)
        fun getCheckListDetail(map: HashMap<String, Any>)
        fun getFinishContractDetail(map: HashMap<String, Any>)
        fun getDepositsContract(map: HashMap<String, Any>)
        fun getCoordinateContract(map: HashMap<String, Any>)
        fun getPortViewInfoCollection(map: HashMap<String, Any>)
    }
}