package vn.com.fpt.mobiqc.data.network.model

/**
 * * Created by Anh Pham on 08/25/2018.     **
 * * Copyright (c) 2018 by FPT Telecom      **
 */
data class ErrorInfrastructureModel(
        var ID: Int = 0,
        var Area: String,
        var Branch: String,
        var Type: String,
        var Element: String,
        var Description: String,
        var Partner: String,
        var Note: String,
        var ImageCode: String,
        var MailTo: String,
        var Status: Int = 0,
        var CreateBy: String,
        var CreateDate: String,
        var UpdateBy: String,
        var UpdateDate: String
) : BaseModel()