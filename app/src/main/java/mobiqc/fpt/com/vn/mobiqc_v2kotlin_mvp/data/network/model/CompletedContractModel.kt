package mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.data.network.model

/**
 * * Created by Anh Pham on 08/08/2018.     **
 * * Copyright (c) 2018 by FPT Telecom      **
 */
data class CompletedContractModel(
        var ID: Int = 0,
        var ObjID: Int = 0,
        var Contract: String = "",
        var Date: String = "",
        var Location: String = "",
        var Support_Location: String = "",
        var AssignDate: String = "",
        var FullName: String = "",
        var CusType: String = "",
        var HDBox: String = "",
        var AppointmentDate: String = "",
        var Status: String = "",
        var ScheduleTime: Int = 0,
        var TimeSLA: Int = 0,
        var Wolf: Any,
        var RemainHourINF: Int = 0,
        var Color: String = "") :BaseModel()