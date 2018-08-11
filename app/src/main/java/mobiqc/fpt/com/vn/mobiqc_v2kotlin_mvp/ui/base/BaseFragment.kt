package mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.base

import android.support.v4.app.Fragment
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.dagger.component.ActivityComponent
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.data.network.model.TitleAndMenuModel
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.main.MainActivity
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.utils.SharedPrefUtils

/**
 * * Created by Anh Pham on 08/02/2018.     **
 * * Copyright (c) 2018 by FPT Telecom      **
 */
open class BaseFragment : Fragment(), BaseView {

    protected var titleModel: TitleAndMenuModel = TitleAndMenuModel()

    override fun showLoading() {
        if (activity is BaseActivity) {
            (activity as BaseActivity).showLoading()
        }
    }

    override fun hideLoading() {
        if (activity is BaseActivity) {
            (activity as BaseActivity).hideLoading()
        }
    }

    override fun isNetworkConnected(): Boolean {
        return activity is BaseActivity && (activity as BaseActivity).isNetworkConnected()
    }

    override fun addFragment(fragment: BaseFragment, isAddToBackStack: Boolean, isAnimation: Boolean) {
        if (activity is BaseActivity) {
            if (activity is MainActivity) {
                val activity = activity as MainActivity
                if (isAddToBackStack) activity.mCountBack++
                activity.handleShowMenu()
            }
            (activity as BaseActivity).addFragment(fragment, isAddToBackStack, isAnimation)
        }
    }

    override fun replaceFragment(fragment: BaseFragment, isAddToBackStack: Boolean, isAnimation: Boolean) {
        if (activity is BaseActivity) {
            (activity as BaseActivity).replaceFragment(fragment, isAddToBackStack, isAnimation)
        }
    }

    override fun getActivityComponent(): ActivityComponent {
        return (activity as BaseActivity).getActivityComponent()
    }

    override fun getSharePreferences(): SharedPrefUtils {
        return (activity as BaseActivity).getSharePreferences()
    }

    fun clearAllBackStack() {
        if (activity is BaseActivity) {
            (activity as BaseActivity).clearAllBackStack()
        }
    }

    fun setTitle(model: TitleAndMenuModel) {
        if (activity is MainActivity) {
            titleModel = model
            (activity as MainActivity).setTitleMain(titleModel)
        }
    }

    fun getTitle(): TitleAndMenuModel {
        return titleModel
    }
}