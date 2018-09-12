package vn.com.fpt.mobiqc.ui.login

import vn.com.fpt.mobiqc.data.network.model.ResponseAccountGroupModel
import vn.com.fpt.mobiqc.data.network.model.ResponseModel
import vn.com.fpt.mobiqc.ui.base.BaseView

/**
 * * Created by Anh Pham on 08/02/2018.     **
 * * Copyright (c) 2018 by FPT Telecom      **
 */
interface LoginContract {
    interface LoginView : BaseView {
        fun loadMobiAccount(response : ResponseAccountGroupModel)
        fun loadLogin(response: ResponseModel)
        fun handleError(response: String)
    }

    interface LoginPresenter {
        fun postLogin(map: HashMap<String,Any>)
        fun postMobiAccount(map: HashMap<String,Any>)
    }
}