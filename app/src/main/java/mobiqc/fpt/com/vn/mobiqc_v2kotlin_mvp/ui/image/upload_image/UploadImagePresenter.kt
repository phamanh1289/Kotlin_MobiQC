package mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.image.upload_image

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.data.network.api.ApiIstorageService
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.data.network.api.ApiUploadImageService
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.base.BasePresenter
import okhttp3.MultipartBody
import javax.inject.Inject

/**
 * * Created by Anh Pham on 08/09/2018.     **
 * * Copyright (c) 2018 by FPT Telecom      **
 */
class UploadImagePresenter @Inject constructor(private val apiUploadImageService: ApiUploadImageService, private val apiIstorageService: ApiIstorageService) : BasePresenter<UploadImageContract.UploadImageView>(), UploadImageContract.UploadImagePresenter {

    override fun postUploadImage(token: String, part: MultipartBody.Part) {
        addSubscribe(apiUploadImageService.postUploadImageDemo(part)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    view?.loadUploadImageToServer(it)
                }, {
                    view?.handleError(it.message.toString())
                }))
    }
}