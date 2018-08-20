package mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.data.network.model

/**
 * * Created by Anh Pham on 08/20/2018.     **
 * * Copyright (c) 2018 by FPT Telecom      **
 */
data class PartnerTimeZoneModel(val Timezone: String, val TimezoneAbility: Int, val TimezoneCode: Int, val TimeCount: Int, val TimezoneCode18: Int, var status : Boolean = false) : BaseModel()