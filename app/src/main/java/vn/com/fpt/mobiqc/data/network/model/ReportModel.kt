package vn.com.fpt.mobiqc.data.network.model

/**
 * * Created by Anh Pham on 08/28/2018.     **
 * * Copyright (c) 2018 by FPT Telecom      **
 */
data class ReportModel(
        var TK: Int = 0,
        var BT: Int = 0,
        var PTTB: Int = 0,
        var Nong: Int = 0,
        var Nguoi: Int = 0,
        var TK_Nong: Int = 0,
        var TK_Nguoi: Int = 0,
        var BT_Nong: Int = 0,
        var BT_Nguoi: Int = 0,
        var ChuyenDe: Int = 0,
        var Swap: Int = 0
)