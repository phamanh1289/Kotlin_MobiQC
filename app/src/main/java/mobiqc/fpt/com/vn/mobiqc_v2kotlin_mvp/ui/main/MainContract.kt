package mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.main

import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.data.network.model.ResponseModel
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.base.BaseView

/**
 * * Created by Anh Pham on 08/02/2018.     **
 * * Copyright (c) 2018 by FPT Telecom      **
 */
interface MainContract {
    interface MainView : BaseView {
        fun loadIpWan(ip: String)
        fun loadAssetToken(data: ResponseModel)
        fun handleError(error: String)
    }

    interface MainPresenter {
        fun getIpWan()
        fun getAssetToken(map: HashMap<String, Any>)
    }
}