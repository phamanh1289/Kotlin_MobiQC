package mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.base

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.tbruyelle.rxpermissions2.RxPermissions
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.R
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.dagger.component.ActivityComponent
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.data.local.CustomTransaction
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.others.dialog.LoadingDialog
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.main.MainActivity
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.utils.AppUtils
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.utils.SharedPrefUtils
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper

/**
 * * Created by Anh Pham on 08/02/2018.     **
 * * Copyright (c) 2018 by FPT Telecom      **
 */
open class BaseActivity : AppCompatActivity(), BaseView {

    private lateinit var mActivityComponent: ActivityComponent
    private lateinit var sharePreferences: SharedPrefUtils
    private var mDialogView: LoadingDialog? = null
    private lateinit var rxPermissions: RxPermissions
    private var ischeckShowDialog = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mActivityComponent = BaseApplication.instance.getApplicationComponent().getActivityComponent()
        sharePreferences = SharedPrefUtils(this)
        rxPermissions = RxPermissions(this@BaseActivity)
    }

    override fun getSharePreferences(): SharedPrefUtils {
        return sharePreferences
    }

    override fun getCurrentFragment(): BaseFragment {
        val fragment = supportFragmentManager.findFragmentById(android.R.id.tabcontent)
        return fragment as? BaseFragment ?: BaseFragment()
    }

    fun getPermission(): RxPermissions {
        return rxPermissions
    }

    override fun getActivityComponent(): ActivityComponent {
        return mActivityComponent
    }

    override fun showLoading() {
        if (isNetworkConnected()) {
            if (mDialogView == null)
                mDialogView = LoadingDialog()
            if (!ischeckShowDialog) {
                mDialogView?.show(supportFragmentManager, LoadingDialog::class.java.simpleName)
                ischeckShowDialog = true
            }
        }
    }

    override fun hideLoading() {
        mDialogView?.dismiss()
        ischeckShowDialog = false
    }

    override fun isNetworkConnected(): Boolean {
        return AppUtils.getNetwork(this)
    }

    override fun addFragment(fragment: BaseFragment, isAddToBackStack: Boolean, isAnimation: Boolean) {
        addReplaceFragment(CustomTransaction(isAnimation = isAnimation), fragment, false, isAddToBackStack)
    }

    override fun replaceFragment(fragment: BaseFragment, isAddToBackStack: Boolean, isAnimation: Boolean) {
        addReplaceFragment(CustomTransaction(isAnimation = isAnimation), fragment, true, isAddToBackStack)
    }

    @SuppressLint("CommitTransaction")
    private fun addReplaceFragment(customTransaction: CustomTransaction, fragment: BaseFragment?, isReplace: Boolean, isAddToBackStack: Boolean) {

        fun getCurrentViewID(view: Int): Int {
            return when {
                view != 0 -> view
                this is MainActivity -> android.R.id.tabcontent
                else -> R.id.frmContainer
            }
        }

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager?.beginTransaction()
        fragmentTransaction?.let { it ->
            if (customTransaction.isAnimation)
                it.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left,
                        R.anim.enter_from_left, R.anim.exit_to_right)
            if (isReplace)
                it.replace(getCurrentViewID(customTransaction.containerViewId), fragment)
            else {
                val currentFragment = supportFragmentManager.findFragmentById(getCurrentViewID(customTransaction.containerViewId))
                currentFragment?.let { current -> it.hide(current) }
                it.add(getCurrentViewID(customTransaction.containerViewId), fragment, fragment!!::class.java.simpleName)
            }
            if (isAddToBackStack)
                it.addToBackStack(fragment!!::class.java.simpleName)
            it.commit()
        }
    }

    fun clearAllBackStack() {
        val fm = supportFragmentManager
        val count = fm?.backStackEntryCount
        count?.let {
            for (i in 0 until it) {
                onBackPressed()
            }
        }
    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
    }
}