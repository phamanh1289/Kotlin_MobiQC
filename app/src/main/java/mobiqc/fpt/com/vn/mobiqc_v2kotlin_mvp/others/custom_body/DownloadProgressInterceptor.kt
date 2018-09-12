package mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.others.custom_body

import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.splash_screen.SplashScreenContract
import okhttp3.Interceptor
import okhttp3.Response

/**
 * * Created by Anh Pham on 11/09/2018.     **
 * * Copyright (c) 2018 by AppsCyclone      **
 */
class DownloadProgressInterceptor(val view: SplashScreenContract.SplashScreenView?) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {

        val originalResponse = chain.proceed(chain.request())
        return originalResponse.newBuilder()
                .body(DownloadProgressResponseBody(originalResponse.body(), view))
                .build()
    }
}