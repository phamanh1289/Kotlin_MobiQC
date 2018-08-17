package mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.blank

import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.base.BaseView

/**
 * * Created by Anh Pham on 08/09/2018.     **
 * * Copyright (c) 2018 by FPT Telecom      **
 */
interface SearchContract {
    interface SearchView : BaseView {
        fun handleError(error : String)
    }

    interface SearchPresenter {
    }
}