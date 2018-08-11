package mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.main

import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.base.BaseView

/**
 * * Created by Anh Pham on 08/02/2018.     **
 * * Copyright (c) 2018 by FPT Telecom      **
 */
interface MainContract {
    interface MainView : BaseView {
    }

    interface MainPresenter {
        fun getListStories()
    }
}