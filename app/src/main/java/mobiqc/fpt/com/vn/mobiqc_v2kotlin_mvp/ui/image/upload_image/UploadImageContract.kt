package mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.image.upload_image

import android.content.Context
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.data.network.model.ResponseModel
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.data.network.model.UploadImageModel
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.base.BaseView

/**
 * * Created by Anh Pham on 08/09/2018.     **
 * * Copyright (c) 2018 by FPT Telecom      **
 */
interface UploadImageContract {
    interface UploadImageView : BaseView {
        fun loadUploadImageToServer(response: Int)
        fun loadCreateImage(response: ResponseModel)
        fun loadAddImage(response: ResponseModel)
        fun handleError(error: String)
    }

    interface UploadImagePresenter {
        fun postUploadImage(context: Context?, token: String, list: ArrayList<UploadImageModel>)
        fun postCreateImage(token: String, map: HashMap<String, Any>)
        fun postAddImage(token: String, map: HashMap<String, Any>)
    }
}