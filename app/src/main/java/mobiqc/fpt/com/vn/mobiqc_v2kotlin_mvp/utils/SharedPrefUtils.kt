package mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.utils

import android.content.Context
import android.content.SharedPreferences
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.others.constant.Constants

/**
 * * Created by Anh Pham on 08/02/2018.     **
 * * Copyright (c) 2018 by FPT Telecom      **
 */
class SharedPrefUtils constructor(app: Context?) {

    companion object {
        private const val SHARED_PREF_NAME = "mobiQC"
        private const val MAX_DATE_ERROR = "maxDateError"
        private const val ACCOUNT_NAME = "accountName"
        private const val IMEI_DEVICE = "imeiDevice"
        private const val MOBI_ACCOUNT = "mobiAccount"
        private const val CREATE_DATE = "createDate"
        private const val LIST_PARAMS = "list_params"
        private const val USER_TOKEN = "assetToken"
        private const val IP_WAN = "ipWan"
    }

    var imeiDevice: String
        get() = sharedPreferences?.getString(IMEI_DEVICE, "")!!
        set(value) = sharedPreferences?.put { putString(IMEI_DEVICE, value) }!!
    var maxDateError: String
        get() = sharedPreferences?.getString(MAX_DATE_ERROR, "")!!
        set(value) = sharedPreferences?.put { putString(MAX_DATE_ERROR, value) }!!
    var accountName: String
        get() = sharedPreferences?.getString(ACCOUNT_NAME, "")!!
        set(value) = sharedPreferences?.put { putString(ACCOUNT_NAME, value) }!!
    var mobiAccount: String
        get() = sharedPreferences?.getString(MOBI_ACCOUNT, "")!!
        set(value) = sharedPreferences?.put { putString(MOBI_ACCOUNT, value) }!!
    var createDate: String
        get() = sharedPreferences?.getString(CREATE_DATE, "")!!
        set(value) = sharedPreferences?.put { putString(CREATE_DATE, value) }!!
    var listParams: String
        get() = sharedPreferences?.getString(LIST_PARAMS, "")!!
        set(value) = sharedPreferences?.put { putString(LIST_PARAMS, value) }!!
    var ipWan: String
        get() = sharedPreferences?.getString(IP_WAN, "")!!
        set(value) = sharedPreferences?.put { putString(IP_WAN, value) }!!
    var userToken: String
        get() = "Bearer ${sharedPreferences?.getString(USER_TOKEN, "")!!}"
        set(value) = sharedPreferences?.put { putString(USER_TOKEN, value) }!!

    private inline fun SharedPreferences.put(body: SharedPreferences.Editor.() -> Unit) {
        val editor = this.edit()
        editor.body()
        editor.apply()
    }

    private val sharedPreferences by lazy(LazyThreadSafetyMode.NONE) {
        app?.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
    }

    fun checkReLogin(): Boolean {
        return if (accountName.isBlank())
            false
        else createDate == AppUtils.getCurrentDate(Constants.CURRENT_DATE)
    }

    fun toClearSessionLogin() {
        accountName = ""
        createDate = ""
    }

}