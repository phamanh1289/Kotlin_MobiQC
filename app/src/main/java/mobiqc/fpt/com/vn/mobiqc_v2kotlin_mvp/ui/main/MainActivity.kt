package mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.main

import android.os.Bundle
import android.support.v4.widget.DrawerLayout
import android.support.v7.widget.LinearLayoutManager
import android.view.Gravity
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.R
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.data.interfaces.ConfirmDialogInterface
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.data.network.model.ItemMenuModel
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.data.network.model.TitleAndMenuModel
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.others.adapter.ItemMenuAdapter
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.others.constant.Constants
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.others.datacore.DataCore
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.base.BaseActivity
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.base.BaseFragment
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.contract.check_contract.CheckContractFragment
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.login.LoginFragment
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.utils.AppUtils
import javax.inject.Inject

/**
 * * Created by Anh Pham on 08/02/2018.     **
 * * Copyright (c) 2018 by FPT Telecom      **
 */
class MainActivity : BaseActivity(), MainContract.MainView, ConfirmDialogInterface {

    private val KT_HOP_DONG = 1

    @Inject
    lateinit var presenter: MainPresenterImp
    lateinit var menuAdapter: ItemMenuAdapter
    private var mData: ArrayList<ItemMenuModel> = ArrayList()
    var mCountBack = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        getActivityComponent().inject(this)
        presenter.onAttach(this)
        initView()
    }

    private fun initView() {
        actMain_ivMenuMain.setOnClickListener {
            if (mCountBack == 0)
                actMain_dlMenu.openDrawer(Gravity.START)
            else onBackPressed()
        }
        mData = DataCore.getListMenu(this)
        menuAdapter = ItemMenuAdapter(mData) { handleActionMenu(mData[it]) }
        actMain_rvMenuMain.apply {
            adapter = menuAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
            setHasFixedSize(true)
        }
        handleActionMenu(mData[KT_HOP_DONG])
    }

    private fun handleActionMenu(itemMenu: ItemMenuModel) {
        when (itemMenu.id) {
            Constants.KT_HOP_DONG -> addFragment(CheckContractFragment(), false, true)
            Constants.CAP_NHAT_LOI -> ""
            Constants.TAO_CHECK_LIST -> ""
            Constants.TAO_PRE_CHECK_LIST -> ""
            Constants.TAO_LOI_MOI -> ""
            Constants.DANH_SACH_LOI -> ""
            Constants.KQ_XAC_MINH -> ""
            Constants.BAO_CAO_SO_LIEU -> ""
            Constants.THONG_TIN -> ""
            Constants.DANG_XUAT -> {
                getSharePreferences().accountName = ""
                addFragment(LoginFragment(), false, true)
            }
        }
        if (itemMenu.id.isNotBlank() || itemMenu.id != Constants.DANG_XUAT) {
//            actMain_tvTitleMain.text = itemMenu.name.replace("-", "").trim()
            actMain_dlMenu.closeDrawers()
        }
    }

    fun handleShowMenu() {
        actMain_dlMenu.setDrawerLockMode(if (mCountBack == 0) DrawerLayout.LOCK_MODE_UNLOCKED else DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        actMain_ivMenuMain.setImageResource(if (mCountBack == 0) R.drawable.ic_menu else R.drawable.ic_back)
    }

    fun setTitleMain(model: TitleAndMenuModel?) {
        model?.let {
            actMain_tvTitleMain.text = if (it.title.contains("-")) it.title.replace("-", "").trim() else it.title.trim()
            actMain_ivNotification.visibility = if (it.status) View.VISIBLE else View.GONE
            actMain_ivNotification.isFocusable = it.status
            actMain_ivNotification.setImageResource(if (it.status) it.image else Constants.NO_IMAGE)
        }
    }

    private fun handleTitleMain() {
        val baseFragment = supportFragmentManager.findFragmentById(android.R.id.tabcontent)
        setTitleMain((baseFragment as? BaseFragment)?.getTitle())
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onDetach()
    }

    override fun onBackPressed() {
        when (mCountBack) {
            0 -> AppUtils.showDialog(supportFragmentManager, content = getString(R.string.content_logout), actionCancel = true, confirmDialogInterface = this)
            else -> {
                super.onBackPressed()
                mCountBack--
                handleTitleMain()
                handleShowMenu()
            }
        }
    }

    override fun onClickOk() {
        finish()
    }

    override fun onClickCancel() {
    }
}
