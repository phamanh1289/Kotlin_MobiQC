package mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.data.network.model

/**
 * * Created by Anh Pham on 08/24/2018.     **
 * * Copyright (c) 2018 by FPT Telecom      **
 */
data class ResponseResultModel(val ErrorCode: Int, val Message: String, val Token: String, val Results : ArrayList<ResultImageModel>)