package mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.data.network.model

/**
 * * Created by Anh Pham on 08/29/2018.     **
 * * Copyright (c) 2018 by FPT Telecom      **
 */
data class DetailReportModel(var title: String = "", var value: Int = 0, var percent: Float = 0f, val typeCL :Int = 0, val typeError : Int = 0) : BaseModel()