package mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.data.network.model

/**
 * * Created by Anh Pham on 08/09/2018.     **
 * * Copyright (c) 2018 by FPT Telecom      **
 */
//status : true -> show image. false -> hide image
data class TitleAndMenuModel(val title: String = "", val image: Int = 0, val status: Boolean = true) :BaseModel()