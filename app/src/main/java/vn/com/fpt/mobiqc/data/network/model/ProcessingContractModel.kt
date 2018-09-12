package vn.com.fpt.mobiqc.data.network.model

/**
 * * Created by Anh Pham on 08/09/2018.     **
 * * Copyright (c) 2018 by FPT Telecom      **
 */
data class ProcessingContractModel(val Assign: Int, val Finish: Int, val InStock: Int, val UpcomingSchedule: Int, val OverSchedule: Int, val SLA24: Int, val SLA48: Int, val SLA241: Int, val SLA481: Int, val NoAppointment: Int, val AppointmentOver: Int, val AppointmentInTime: Int, val AppointmentNext: Int) :BaseModel()