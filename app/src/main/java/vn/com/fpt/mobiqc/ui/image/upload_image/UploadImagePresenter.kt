package vn.com.fpt.mobiqc.ui.image.upload_image

import android.content.Context
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import vn.com.fpt.mobiqc.data.network.api.ApiIstorageService
import vn.com.fpt.mobiqc.data.network.api.ApiUploadImageService
import vn.com.fpt.mobiqc.data.network.model.UploadImageModel
import vn.com.fpt.mobiqc.others.service.UploadService
import vn.com.fpt.mobiqc.ui.base.BasePresenter
import javax.inject.Inject

/**
 * * Created by Anh Pham on 08/09/2018.     **
 * * Copyright (c) 2018 by FPT Telecom      **
 */
class UploadImagePresenter @Inject constructor(private val apiUploadImageService: ApiUploadImageService, private val apiIstorageService: ApiIstorageService) : BasePresenter<UploadImageContract.UploadImageView>(), UploadImageContract.UploadImagePresenter {

    override fun postUploadImage(context: Context?, token: String, list: ArrayList<UploadImageModel>) {
        UploadService(context, list, token, view).UpLoadImageToServer().execute()
    }

    override fun postCreateImage(token: String, map: HashMap<String, Any>) {
        addSubscribe(apiIstorageService.postCreateImage(token,map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    view?.loadCreateImage(it)
                }, {
                    view?.handleError(it.message.toString())
                }))
    }

    override fun postAddImage(token: String, map: HashMap<String, Any>) {
        addSubscribe(apiIstorageService.postAddImage(token,map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    view?.loadAddImage(it)
                }, {
                    view?.handleError(it.message.toString())
                }))
    }
}