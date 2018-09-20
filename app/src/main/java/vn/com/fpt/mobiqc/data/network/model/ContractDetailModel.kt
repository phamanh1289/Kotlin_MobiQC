package vn.com.fpt.mobiqc.data.network.model

/**
 * * Created by Anh Pham on 08/09/2018.     **
 * * Copyright (c) 2018 by FPT Telecom      **
 */
data class ContractDetailModel(
        var ID: Int = 0,
        var ObjID: Int = 0,
        var Name: String = "",
        var LocalType: String = "",
        var FullName: String = "",
        var Phone: String = "",
        var Indoor: Int = 0,
        var Outdoor: Int = 0,
        var ODCCableType: String = "",
        var Description: String = "",
        var Contract: String = "",
        var Eoc: Int = 0,

        //Deployment
        var Address: String = "",
        var Coordinate: String,
        var CreateDate: String,
        var NewLocalType: String = "",
        var LoopRemind: String = "",
        var LanQuantity: Int = 0,
        var OutDoorReUsed: Int = 0,
        var Image: String = "",
        var ModemType: String = "",
        var ModemAmount: Int = 0,
        var STBType: String = "",
        var STBAmount: Int = 0,
        var HDBoxAmount: String = "",
        var IsUpgrade: String = "",
        var BackModemType: String = "",
        var BackModemAmount: Int = 0,
        var BackSTBType: String = "",
        var BackSTBAmount: Int = 0,
        var BackHDBoxAmount: Int = 0,
        var DeploymentID: String = "",
        var DescriptionCS: String = "",
        var GroupPoint: String = "",
        var GroupPoints: String = "",
        var FinishDate: String = "",
        var Deposits: Double = 0.0,

        //Maintenance
        var Support_Location: String = "",
        var Init_Status: String = "",
        var Init_StatusD: String = "",
        var Init_Desc: String = "",
        var CableType: String = "",
        var DivisionDesc: String = "",
        var Link1: String = "",
        var Link2: String = "",
        var ThirtyRepeat: String = "",
        var MaintainID: Int = 0,
        var ODCLength: Int = 0,
        var IDCLength: Int = 0,
        var OwnerType: Int = 0
) : BaseModel()