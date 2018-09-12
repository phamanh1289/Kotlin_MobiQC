package vn.com.fpt.mobiqc.ui.contract.dialog_detail_contract

import vn.com.fpt.mobiqc.data.network.model.ResponseModel
import vn.com.fpt.mobiqc.ui.base.BaseView

/**
 * * Created by Anh Pham on 08/09/2018.     **
 * * Copyright (c) 2018 by FPT Telecom      **
 */
interface DetailContractDialogContract {
    interface DetailContractDialogView : BaseView {
        fun loadDepositsContract(response: ResponseModel)
        fun loadCoordinateContract(response: ResponseModel)
        fun loadPortViewInfoCollection(response: ResponseModel)
        fun loadAllPhoneNumber(response: ResponseModel)
        fun handleError(error: String)
    }

    interface DetailContractDialogPresenter {
        fun getDepositsContract(map: HashMap<String, Any>)
        fun getCoordinateContract(map: HashMap<String, Any>)
        fun getPortViewInfoCollection(map: HashMap<String, Any>)
        fun getAllPhoneNumber(map: HashMap<String, Any>)
    }
}