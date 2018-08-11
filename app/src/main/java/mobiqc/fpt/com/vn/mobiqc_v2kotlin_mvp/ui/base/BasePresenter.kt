package mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.base

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

/**
 * * Created by Anh Pham on 08/02/2018.     **
 * * Copyright (c) 2018 by FPT Telecom      **
 */
open class BasePresenter<V : BaseView> : IPresenter<V> {

    var view: V? = null
    private val compositeDisposable: CompositeDisposable?

    override val isViewAttach: Boolean
        get() = view != null

    init {
        this.compositeDisposable = CompositeDisposable()
    }

    override fun onAttach(mvpView: V) {
        this.view = mvpView
    }

    override fun onDetach() {
        this.view = null
        unsubscribe()
    }

    private fun unsubscribe() {
        compositeDisposable?.dispose()
    }

    protected fun addSubscribe(disposable: Disposable) {
        compositeDisposable?.add(disposable)
    }
}