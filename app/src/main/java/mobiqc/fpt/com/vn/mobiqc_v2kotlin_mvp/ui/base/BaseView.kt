package mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.base

import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.dagger.component.ActivityComponent
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.utils.SharedPrefUtils

/**
 * * Created by Anh Pham on 08/02/2018.     **
 * * Copyright (c) 2018 by FPT Telecom      **
 */
interface BaseView {
    fun showLoading()

    fun hideLoading()

    fun getActivityComponent(): ActivityComponent

    fun getSharePreferences(): SharedPrefUtils

    fun getCurrentFragment(): BaseFragment

    fun isNetworkConnected(): Boolean

    fun addFragment(fragment: BaseFragment, isAddToBackStack: Boolean, isAnimation: Boolean)

    fun replaceFragment(fragment: BaseFragment, isAddToBackStack: Boolean, isAnimation: Boolean)
}