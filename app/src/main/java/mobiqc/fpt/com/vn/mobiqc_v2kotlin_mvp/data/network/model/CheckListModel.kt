package mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.data.network.model

/**
 * * Created by Anh Pham on 08/13/2018.     **
 * * Copyright (c) 2018 by FPT Telecom      **
 */
data class CheckListModel(
        val ID: Int,
        val ObjID: Int,
        val Contract: String,
        val Date: String,
        val Location: String,
        val Support_Location: String,
        val AssignDate: String,
        val AppointmentDate: String,
        val FullName: String,
        val CusType: String,
        val HDBox: String,
        val Status: String,
        val ScheduleTime: Int,
        val RemainHourINF: Int,
        val Color: String,
        val StatusCL: String,
        val LastUpdate: String,
        val UpdateBy: String,
        val Hour: String,
        val Priority: String,
        val TypeSupport: String,
        val TimeLeft: String,
        val HourRemin: Int,
        val RequestFrom: String,
        val OwnerType: Int,
        val ThiryRepeat: String
) : BaseModel()