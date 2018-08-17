package mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.login

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.data.network.api.ApiService
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.base.BasePresenter
import javax.inject.Inject

/**
 * * Created by Anh Pham on 08/02/2018.     **
 * * Copyright (c) 2018 by FPT Telecom      **
 */
class LoginPresenter @Inject constructor(val apiService: ApiService) : BasePresenter<LoginContract.LoginView>(), LoginContract.LoginPresenter {

    override fun postMobiAccount(map: HashMap<String, Any>) {
        addSubscribe(apiService.getMobiAccount(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    view?.loadMobiAccount(it)
                }, {
                    view?.handleError(it.message.toString())
                }))
    }

    override fun postLogin(map: HashMap<String, Any>) {
        addSubscribe(apiService.postLogin(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    view?.loadLogin(it)
                }, {
                    view?.handleError(it.message.toString())
                }))
    }
}