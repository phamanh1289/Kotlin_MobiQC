package vn.com.fpt.mobiqc.ui.blank

import vn.com.fpt.mobiqc.ui.base.BaseView

/**
 * * Created by Anh Pham on 08/09/2018.     **
 * * Copyright (c) 2018 by FPT Telecom      **
 */
interface BlankContract {
    interface View : BaseView {
        fun handleError(error : String)
    }

    interface Presenter {
    }
}