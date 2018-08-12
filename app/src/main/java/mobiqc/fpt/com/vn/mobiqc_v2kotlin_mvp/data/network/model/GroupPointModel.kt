package mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.data.network.model

/**
 * * Created by Anh Pham on 08/12/2018.     **
 * * Copyright (c) 2018 by FPT Telecom      **
 */
data class GroupPointModel(val TDName: String, val Street: String, val LengthPop: Int, val Capacity: Int, val Portuse: Int, val Latlng: String) : BaseModel()