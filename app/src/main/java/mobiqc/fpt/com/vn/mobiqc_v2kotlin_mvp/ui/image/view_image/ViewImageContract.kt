package mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.image.view_image

import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.data.network.model.ResponseModel
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.base.BaseView

/**
 * * Created by Anh Pham on 08/09/2018.     **
 * * Copyright (c) 2018 by FPT Telecom      **
 */
interface ViewImageContract {
    interface ViewImageView : BaseView {
        fun loadAlbumCode(response: ResponseModel)
        fun handleError(error : String)
    }

    interface ViewImagePresenter {
        fun getAlbumCode(token:String,code: String)
    }
}