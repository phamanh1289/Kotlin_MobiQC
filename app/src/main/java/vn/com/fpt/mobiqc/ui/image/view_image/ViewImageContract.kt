package vn.com.fpt.mobiqc.ui.image.view_image

import vn.com.fpt.mobiqc.data.network.model.ResponseModel
import vn.com.fpt.mobiqc.ui.base.BaseView

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