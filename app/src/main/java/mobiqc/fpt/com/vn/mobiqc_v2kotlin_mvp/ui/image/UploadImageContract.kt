package mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.image

import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.base.BaseView

/**
 * * Created by Anh Pham on 08/09/2018.     **
 * * Copyright (c) 2018 by FPT Telecom      **
 */
interface UploadImageContract {
    interface UploadImageView : BaseView {
//        fun loadInsertErrorInfrastructure(response: ResponseModel)
        fun handleError(error : String)
    }

    interface UploadImagePresenter {
//        fun postInsertErrorInfrastructure(map: HashMap<String, Any>)
    }
}