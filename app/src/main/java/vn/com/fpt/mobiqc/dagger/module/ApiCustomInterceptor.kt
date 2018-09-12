package vn.com.fpt.mobiqc.dagger.module

import vn.com.fpt.mobiqc.data.network.model.ErrorServerModel
import vn.com.fpt.mobiqc.others.constant.Constants
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.ResponseBody
import java.io.IOException

/**
 * * Created by Anh Pham on 12/09/2018.     **
 * * Copyright (c) 2018 by AppsCyclone      **
 */
class ApiCustomInterceptor : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())
        if (response.code() >= 400) {
            throw IOException(ErrorServerModel.getErrorString(response))
        }
        val stringResponse: String = response.body()!!.string()
        try {
            return response.newBuilder()
                    .message(Constants.SUCCESSFUL)
                    .body(ResponseBody.create(response.body()!!.contentType(), stringResponse))
                    .build()
        } catch (e: Exception) {
            throw IOException(e.message)
        }
    }
}