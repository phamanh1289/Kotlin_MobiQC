package mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.splash_screen

import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.data.network.model.ResponseErrorDataModel
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.data.network.model.ResponseModel
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.base.BaseView

/**
 * * Created by Anh Pham on 08/06/2018.     **
 * * Copyright (c) 2018 by FPT Telecom      **
 */
interface SplashScreenContract {
    interface SplashScreenView : BaseView {
        fun loadAppVersion(response: ResponseModel)
        fun loadCheckImei(response: ResponseModel)
        fun loadNewErrorData(response: ResponseErrorDataModel, data: String?)
        fun loadNewFileVersion(percent: Float)
        fun handleError(error: String)
    }

    interface SplashScreenPresenter {
        fun getAppVersion(map: HashMap<String, Any>)
        fun postCheckImei(map: HashMap<String, Any>)
        fun getNewErrorData(map: HashMap<String, Any>)
        fun getNewFileVersion(url: String)
    }
}