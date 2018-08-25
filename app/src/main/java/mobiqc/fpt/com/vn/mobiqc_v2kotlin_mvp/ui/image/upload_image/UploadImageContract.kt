package mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.image.upload_image

import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.data.network.model.ResponseModel
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.base.BaseView
import okhttp3.MultipartBody

/**
 * * Created by Anh Pham on 08/09/2018.     **
 * * Copyright (c) 2018 by FPT Telecom      **
 */
interface UploadImageContract {
    interface UploadImageView : BaseView {
        fun loadUploadImageToServer(response: ResponseModel)
        fun handleError(error: String)
    }

    interface UploadImagePresenter {
        fun postUploadImage(token: String, part: MultipartBody.Part)
    }
}