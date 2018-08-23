package mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.data.network.model

import android.graphics.Bitmap

/**
 * * Created by Anh Pham on 08/23/2018.     **
 * * Copyright (c) 2018 by FPT Telecom      **
 */
data class UploadImageModel(var id: Int = 0, val bitmap : Bitmap) : BaseModel()