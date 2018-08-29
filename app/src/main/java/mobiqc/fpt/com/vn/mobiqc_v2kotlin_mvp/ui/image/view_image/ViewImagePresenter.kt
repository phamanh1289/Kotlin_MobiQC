package mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.image.view_image

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.data.network.api.ApiIstorageService
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.base.BasePresenter
import javax.inject.Inject

/**
 * * Created by Anh Pham on 08/09/2018.     **
 * * Copyright (c) 2018 by FPT Telecom      **
 */
class ViewImagePresenter @Inject constructor(private val apiIstorageService: ApiIstorageService) : BasePresenter<ViewImageContract.ViewImageView>(), ViewImageContract.ViewImagePresenter {
    override fun getAlbumCode(token: String, code: String) {
        addSubscribe(apiIstorageService.getAlbum(token, code)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    view?.loadAlbumCode(it)
                }, {
                    view?.handleError(it.message.toString())
                }))
    }
}