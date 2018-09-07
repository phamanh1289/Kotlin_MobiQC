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
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.data.network.model.ResponseModel
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.data.network.model.ResponseResultModel
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.data.network.model.TitleAndMenuModel
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.data.realm.location.LocationRealmManager
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.others.adapter.ItemMenuAdapter
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.others.constant.Constants
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.others.datacore.DataCore
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.base.BaseActivity
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.base.BaseFragment
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.check_list.all_check_list.AllCheckListFragment
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.check_list.create_check_list.CreateCheckListFragment
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.check_list.create_pre_check_list.CreatePreCheckListFragment
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.contract.check_contract.CheckContractFragment
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.contract.detail_contract.DetailContractFragment
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.contract.search_contract.SearchFragment
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.error.create.CreateErrorFragment
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.error.list.ListErrorFragment
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.error.update.UpdateErrorFragment
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.infomation.InformationFragment
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.reprot.ReportFragment
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.utils.AppUtils
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.utils.StartActivityUtils
import javax.inject.Inject

/**
 * * Created by Anh Pham on 08/02/2018.     **
 * * Copyright (c) 2018 by FPT Telecom      **
 */
class MainActivity : BaseActivity(), MainContract.MainView, ConfirmDialogInterface {
    private val KT_HOP_DONG = 1

    @Inject
    lateinit var presenter: MainPresenterImp

    private lateinit var menuAdapter: ItemMenuAdapter
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
            val layout = LinearLayoutManager(this@MainActivity)
            layout.orientation = LinearLayoutManager.VERTICAL
            layoutManager = layout
            setHasFixedSize(true)
        }
        handleActionMenu(mData[KT_HOP_DONG])
        actMain_tvVersion.text = getString(R.string.version_app_name, packageManager.getPackageInfo(packageName, 0).versionName)
        actMain_ivNotification.setOnClickListener {
            handleActionNotify()
        }
        getIpExternal()
    }

    private fun getIpExternal() {
        showLoading()
        presenter.getIpWan()
    }

    override fun loadIpWan(ip: String) {
        if (ip.isNotBlank()) {
            getSharePreferences().ipWan = ip
            presenter.let {
                val map = HashMap<String, Any>()
                map[Constants.PARAMS_API_KEY] = getString(R.string.api_key_storage)
                map[Constants.PARAMS_USER_NAME_LOW] = getSharePreferences().accountName
                map[Constants.PARAMS_IP_CLIENT] = ip
                it.getAssetToken(map)
            }
        } else hideLoading()
    }

    override fun loadAssetToken(data: ResponseModel) {
        val result: ResponseResultModel? = data.ResponseResult
        result?.let {
            if (it.ErrorCode == Constants.REQUEST_TOKEN_SUCCESS)
                getSharePreferences().userToken = it.Token
        }
        hideLoading()
    }

    override fun handleError(error: String) {
        hideLoading()
        AppUtils.showDialog(supportFragmentManager, confirmDialogInterface = null, content = error)
    }

    private fun handleActionNotify() {
        val fragment = getCurrentFragment()
        when (fragment) {
            is DetailContractFragment -> fragment.onClickContractNumber()
            is AllCheckListFragment -> fragment.showDialogDetailContract()
            is UpdateErrorFragment -> fragment.showDetailContract()
            is CreatePreCheckListFragment -> fragment.showDialogDetailContract()
            is CreateCheckListFragment -> fragment.showDialogDetailContract()
        }
    }

    private fun handleActionMenu(itemMenu: ItemMenuModel) {
        when (itemMenu.id) {
            Constants.KT_HOP_DONG -> addFragment(CheckContractFragment(), false, true)
            Constants.CAP_NHAT_LOI -> addFragment(SearchFragment.newInstance(Constants.ARG_MENU_CNL, itemMenu.name), false, true)
            Constants.TAO_CHECK_LIST -> addFragment(SearchFragment.newInstance(Constants.ARG_MENU_CL, itemMenu.name), false, true)
            Constants.TAO_PRE_CHECK_LIST -> addFragment(SearchFragment.newInstance(Constants.ARG_MENU_PCL, itemMenu.name), false, true)
            Constants.TAO_LOI_MOI -> addFragment(CreateErrorFragment(), false, true)
            Constants.DANH_SACH_LOI -> addFragment(ListErrorFragment.newInstance(itemMenu.name), false, true)
            Constants.KQ_XAC_MINH ->
                AppUtils.showDialog(supportFragmentManager, content = getString(R.string.action_feature), confirmDialogInterface = null)
            Constants.BAO_CAO_SO_LIEU -> addFragment(ReportFragment(), false, true)
            Constants.THONG_TIN -> addFragment(InformationFragment.newInstance(itemMenu.name), false, true)
            Constants.DANG_XUAT -> {
                AppUtils.showDialog(fragmentManager = supportFragmentManager, content = getString(R.string.mess_log_out), actionCancel = true, confirmDialogInterface = object : ConfirmDialogInterface {
                    override fun onClickOk() {
                        getSharePreferences().toClearSessionLogin()
                        LocationRealmManager().deleteAllLocation()
                        StartActivityUtils().toSplashActivity(this@MainActivity)
                    }

                    override fun onClickCancel() {

                    }
                })
            }
        }
        if (itemMenu.id.isNotBlank() || itemMenu.id != Constants.DANG_XUAT)
            actMain_dlMenu.closeDrawers()
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
                val fragError = supportFragmentManager.findFragmentById(android.R.id.tabcontent)
                fragError?.let {
                    if (it is ListErrorFragment) {
                        it.adapterError.notifyItemChanged(it.positionListError)
                    }
                }
            }
        }
    }

    override fun onClickOk() {
        finish()
    }

    override fun onClickCancel() {
    }
}
