package mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.dagger.connect

/**
 * * Created by Anh Pham on 08/02/2018.     **
 * * Copyright (c) 2018 by FPT Telecom      **
 */
object ApiConfig {
    fun createConnectionDetail(typeApi: ApiConfigType?): ApiConfigDetail {
        val type = typeApi ?: ApiConfigType.DEVELOP
        var url = ""
        var urlMobiNet = ""
        var urlWsMobiNet = ""
        var urlUploadImage = ""
        var urlGetToken = ""
        when (type) {
            ApiConfigType.DEVELOP -> {
                url = "http://wsmobiqc.fpt.vn/MobiQC.svc/"
                urlMobiNet = "http://mobinet.fpt.vn/MobiNet.svc/"
                urlWsMobiNet = "http://wsmobinet.fpt.vn/MobiNet.svc/"
                urlUploadImage = "http://dev.androidcoban.com/blog/"
                urlGetToken = "http://istorage.fpt.net/api/"
            }
            else -> {
            }
        }
        return ApiConfigDetail(baseURL = url, urlMobiNet = urlMobiNet, urlWsMobiNet = urlWsMobiNet, urlUploadImage = urlUploadImage, urlStorage = urlGetToken)
    }
}