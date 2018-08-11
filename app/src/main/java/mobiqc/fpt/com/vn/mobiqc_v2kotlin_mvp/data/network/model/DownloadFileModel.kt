package mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.data.network.model

/**
 * * Created by Anh Pham on 08/08/2018.     **
 * * Copyright (c) 2018 by FPT Telecom      **
 */
data class DownloadFileModel(var progress: Int = 0, var currentFileSize: Int = 0, var totalFileSize: Int = 0) : BaseModel()