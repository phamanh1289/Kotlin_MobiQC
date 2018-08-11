package mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.login

import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.data.network.model.ResponseAccountGroupModel
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.data.network.model.ResponseModel
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.base.BaseView

/**
 * * Created by Anh Pham on 08/02/2018.     **
 * * Copyright (c) 2018 by FPT Telecom      **
 */
interface LoginContract {
    interface LoginView : BaseView {
        fun loadMobiAccount(response : ResponseAccountGroupModel)
        fun loadUser(response: ResponseModel)
        fun handleError(response: String)
    }

    interface LoginPresenter {
        fun postLogin(map: HashMap<String,Any>)
        fun postMobiAccount(map: HashMap<String,Any>)
    }
}