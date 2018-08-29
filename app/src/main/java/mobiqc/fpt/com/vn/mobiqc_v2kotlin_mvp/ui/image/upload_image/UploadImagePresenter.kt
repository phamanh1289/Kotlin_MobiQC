package mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.image.upload_image

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.data.network.api.ApiIstorageService
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.data.network.api.ApiUploadImageService
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.base.BasePresenter
import okhttp3.RequestBody
import javax.inject.Inject

/**
 * * Created by Anh Pham on 08/09/2018.     **
 * * Copyright (c) 2018 by FPT Telecom      **
 */
class UploadImagePresenter @Inject constructor(private val apiUploadImageService: ApiUploadImageService, private val apiIstorageService: ApiIstorageService) : BasePresenter<UploadImageContract.UploadImageView>(), UploadImageContract.UploadImagePresenter {

    override fun postUploadImage(token: String, part: RequestBody) {
        addSubscribe(apiUploadImageService.postUploadImage(token, part)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    view?.loadUploadImageToServer(it)
                }, {
                    view?.handleError(it.message.toString())
                }))
    }

    override fun postCreateImage(map: HashMap<String, Any>) {
        addSubscribe(apiIstorageService.postCreateImage(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    view?.loadCreateImage(it)
                }, {
                    view?.handleError(it.message.toString())
                }))
    }

    override fun postAddImage(map: HashMap<String, Any>) {
        addSubscribe(apiIstorageService.postAddImage(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    view?.loadAddImage(it)
                }, {
                    view?.handleError(it.message.toString())
                }))
    }
}