package vn.com.fpt.mobiqc.dagger.connect

/**
 * * Created by Anh Pham on 08/02/2018.     **
 * * Copyright (c) 2018 by FPT Telecom      **
 */
enum class ApiConfigType(var title: String?) {
    STAGING("staging"),
    DEVELOP("develop"),
    PRELIVE("prelive"),
    LIVE("live")
}