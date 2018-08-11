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
        var apiKey = ""
        when (type) {
            ApiConfigType.DEVELOP -> {
                url = "http://wsmobiqc.fpt.vn/MobiQC.svc/"
                urlMobiNet = "http://mobinet.fpt.vn/MobiNet.svc/"
                urlWsMobiNet = "http://wsmobinet.fpt.vn/MobiNet.svc/"
            }
            ApiConfigType.STAGING -> url = "url staging"
            ApiConfigType.PRELIVE -> {
                url = "url"
                apiKey = "key"
            }
            ApiConfigType.LIVE -> {
                url = "url"
                apiKey = "key"
            }
        }
        return ApiConfigDetail(baseURL = url, apiKey = apiKey, urlMobiNet = urlMobiNet, urlWsMobiNet = urlWsMobiNet)
    }
}