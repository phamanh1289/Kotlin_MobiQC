package vn.com.fpt.mobiqc.ui.base

import vn.com.fpt.mobiqc.dagger.component.ActivityComponent
import vn.com.fpt.mobiqc.utils.SharedPrefUtils

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