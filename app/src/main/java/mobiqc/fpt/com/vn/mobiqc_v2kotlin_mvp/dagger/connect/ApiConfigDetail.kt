package mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.dagger.connect

/**
 * * Created by Anh Pham on 08/02/2018.     **
 * * Copyright (c) 2018 by FPT Telecom      **
 */
data class ApiConfigDetail(val baseURL: String, var urlMobiNet: String = "", var urlWsMobiNet: String = "", val urlUploadImage: String, val urlStorage : String)
