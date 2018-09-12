package vn.com.fpt.mobiqc.ui.maps

import vn.com.fpt.mobiqc.ui.base.BaseView

/**
 * * Created by Anh Pham on 08/09/2018.     **
 * * Copyright (c) 2018 by FPT Telecom      **
 */
interface MapsContract {
    interface MapsView : BaseView {
        fun loadPolyline(data: String)
        fun handleError(error: String)
    }

    interface MapsPresenter {
        fun getPolyline(url: String)
    }
}