package vn.com.fpt.mobiqc.ui.splash_screen

import vn.com.fpt.mobiqc.data.network.model.ResponseErrorDataModel
import vn.com.fpt.mobiqc.data.network.model.ResponseModel
import vn.com.fpt.mobiqc.ui.base.BaseView

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