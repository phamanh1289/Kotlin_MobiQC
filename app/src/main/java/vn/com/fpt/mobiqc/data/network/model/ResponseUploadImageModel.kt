package vn.com.fpt.mobiqc.data.network.model

/**
 * * Created by Anh Pham on 06/09/2018.     **
 * * Copyright (c) 2018 by AppsCyclone      **
 */
data class ResponseUploadImageModel(var ErrorCode: Int = 0, val ImgPath: String) : BaseModel()