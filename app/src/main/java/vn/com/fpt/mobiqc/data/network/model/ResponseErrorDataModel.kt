package vn.com.fpt.mobiqc.data.network.model

/**
 * * Created by Anh Pham on 08/02/2018.     **
 * * Copyright (c) 2018 by FPT Telecom      **
 */
class ResponseErrorDataModel(val code: Int, val description: String, var data: MutableList<ErrorDataModel>) : BaseModel()