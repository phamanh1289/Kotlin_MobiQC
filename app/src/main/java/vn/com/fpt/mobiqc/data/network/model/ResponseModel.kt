package vn.com.fpt.mobiqc.data.network.model

/**
 * * Created by Anh Pham on 08/02/2018.     **
 * * Copyright (c) 2018 by FPT Telecom      **
 */
class ResponseModel (val Code: Int, val Description: String, val Data: Any, val Username: String, val Link : String, val ResponseResult : ResponseResultModel, val message : String) : BaseModel()