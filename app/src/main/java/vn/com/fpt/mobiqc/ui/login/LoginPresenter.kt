package vn.com.fpt.mobiqc.ui.login

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import vn.com.fpt.mobiqc.data.network.api.ApiService
import vn.com.fpt.mobiqc.data.network.model.LocationUserModel
import vn.com.fpt.mobiqc.data.realm.location.LocationRealmManager
import vn.com.fpt.mobiqc.others.constant.Constants
import vn.com.fpt.mobiqc.ui.base.BasePresenter
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
                .subscribe({ it ->
                    if (it.Code == Constants.REQUEST_SUCCESS) {
                        val list: ArrayList<LocationUserModel> = Gson().fromJson(it.Data.toString(), object : TypeToken<ArrayList<LocationUserModel>>() {}.type)
                        if (list.size != 0) {
                            list.forEach { item ->
                                LocationRealmManager().insertLocation(item)
                            }
                        }
                    }
                    view?.loadLogin(it)
                }, {
                    view?.handleError(it.message.toString())
                }))
    }
}