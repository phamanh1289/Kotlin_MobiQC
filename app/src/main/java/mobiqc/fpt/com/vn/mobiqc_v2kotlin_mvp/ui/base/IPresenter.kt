package mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.base

/**
 * * Created by Anh Pham on 08/02/2018.     **
 * * Copyright (c) 2018 by FPT Telecom      **
 */
interface IPresenter<V : BaseView> {
    val isViewAttach: Boolean
    fun onAttach(mvpView: V)
    fun onDetach()
}