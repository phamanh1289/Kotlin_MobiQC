package mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.data.network.model

/**
 * * Created by Anh Pham on 08/09/2018.     **
 * * Copyright (c) 2018 by FPT Telecom      **
 */
data class ContractDetailModel(
        val ID: Int,
        val ObjID: Int,
        val Name: String,
        val LocalType: String,
        val FullName: String,
        val Phone: String,
        var Indoor: Int = 0,
        var Outdoor: Int = 0,
        val ODCCableType: String,
        val Description: String,
        val Contract: String,
        var Eoc: Int = 0,

        //Deployment
        val Address: String,
        var Coordinate: String,
        var CreateDate: String,
        val NewLocalType: String,
        val LoopRemind: String,
        var LanQuantity: Int = 0,
        var OutDoorReUsed: Int = 0,
        val Image: String,
        val ModemType: String,
        var ModemAmount: Int = 0,
        val STBType: String,
        var STBAmount: Int = 0,
        val HDBoxAmount: String,
        val IsUpgrade: String,
        val BackModemType: String,
        var BackModemAmount: Int = 0,
        val BackSTBType: String,
        var BackSTBAmount: Int = 0,
        var BackHDBoxAmount: Int = 0,
        val DeploymentID: String,
        val DescriptionCS: String,
        val GroupPoint: String,
        val GroupPoints: String,
        val FinishDate: String,
        var Deposits: Double,

        //Maintenance
        val Support_Location: String,
        val Init_Status: String,
        val Init_StatusD: String,
        val Init_Desc: String,
        val CableType: String,
        val DivisionDesc: String,
        val Link1: String,
        val Link2: String,
        val ThirtyRepeat: String,
        var MaintainID: Int = 0,
        var ODCLength: Int = 0,
        var IDCLength: Int = 0,
        var OwnerType: Int = 0
) : BaseModel()