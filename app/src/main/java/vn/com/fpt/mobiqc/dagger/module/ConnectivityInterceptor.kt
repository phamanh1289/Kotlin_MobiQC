package vn.com.fpt.mobiqc.dagger.module

import vn.com.fpt.mobiqc.others.constant.Constants
import vn.com.fpt.mobiqc.ui.base.BaseApplication
import vn.com.fpt.mobiqc.utils.AppUtils
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

/**
 * * Created by Anh Pham on 12/09/2018.     **
 * * Copyright (c) 2018 by AppsCyclone      **
 */
class ConnectivityInterceptor : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        if (!AppUtils.getNetwork(BaseApplication.instance)) {
            throw IOException(Constants.ERROR_NETWORK)
        }
        val builder = chain.request().newBuilder()
        return chain.proceed(builder.build())
    }
}