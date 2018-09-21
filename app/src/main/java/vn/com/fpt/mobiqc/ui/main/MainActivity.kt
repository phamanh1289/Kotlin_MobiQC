package vn.com.fpt.mobiqc.ui.main

import android.os.Bundle
import android.support.v4.widget.DrawerLayout
import android.support.v7.widget.LinearLayoutManager
import android.view.Gravity
import android.view.View
import com.google.firebase.analytics.FirebaseAnalytics
import kotlinx.android.synthetic.main.activity_main.*
import vn.com.fpt.mobiqc.R
import vn.com.fpt.mobiqc.data.interfaces.ConfirmDialogInterface
import vn.com.fpt.mobiqc.data.network.model.ItemMenuModel
import vn.com.fpt.mobiqc.data.network.model.ResponseModel
import vn.com.fpt.mobiqc.data.network.model.ResponseResultModel
import vn.com.fpt.mobiqc.data.network.model.TitleAndMenuModel
import vn.com.fpt.mobiqc.data.realm.infrastructure.InfrastructureRealmManager
import vn.com.fpt.mobiqc.data.realm.location.LocationRealmManager
import vn.com.fpt.mobiqc.data.realm.partner.PartnerRealmManager
import vn.com.fpt.mobiqc.others.adapter.ItemMenuAdapter
import vn.com.fpt.mobiqc.others.constant.Constants
import vn.com.fpt.mobiqc.others.datacore.DataCore
import vn.com.fpt.mobiqc.ui.base.BaseActivity
import vn.com.fpt.mobiqc.ui.base.BaseFragment
import vn.com.fpt.mobiqc.ui.check_list.all_check_list.AllCheckListFragment
import vn.com.fpt.mobiqc.ui.check_list.create_check_list.CreateCheckListFragment
import vn.com.fpt.mobiqc.ui.check_list.create_pre_check_list.CreatePreCheckListFragment
import vn.com.fpt.mobiqc.ui.contract.check_contract.CheckContractFragment
import vn.com.fpt.mobiqc.ui.contract.detail_contract.DetailContractFragment
import vn.com.fpt.mobiqc.ui.contract.search_contract.SearchFragment
import vn.com.fpt.mobiqc.ui.error.create.CreateErrorFragment
import vn.com.fpt.mobiqc.ui.error.list.ListErrorFragment
import vn.com.fpt.mobiqc.ui.error.update.UpdateErrorFragment
import vn.com.fpt.mobiqc.ui.infomation.InformationFragment
import vn.com.fpt.mobiqc.ui.reprot.ReportFragment
import vn.com.fpt.mobiqc.utils.AppUtils
import vn.com.fpt.mobiqc.utils.StartActivityUtils
import javax.inject.Inject


/**
 * * Created by Anh Pham on 08/02/2018.     **
 * * Copyright (c) 2018 by FPT Telecom      **
 */
class MainActivity : BaseActivity(), MainContract.MainView, ConfirmDialogInterface {
    private val KT_HOP_DONG = 1

    @Inject
    lateinit var presenter: MainPresenterImp

    private var mAnalytics: FirebaseAnalytics? = null
    private lateinit var menuAdapter: ItemMenuAdapter
    private var mData: ArrayList<ItemMenuModel> = ArrayList()
    private var currentPage = ""
    var mCountBack = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        getActivityComponent().inject(this)
        presenter.onAttach(this)
        mAnalytics = FirebaseAnalytics.getInstance(this)
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
        if (PartnerRealmManager().getCountPartner() == 0)
            PartnerRealmManager().importFromJson(resources)
        if (InfrastructureRealmManager().getCountInfrast() == 0)
            InfrastructureRealmManager().importFromJson(resources)
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
        if (currentPage != itemMenu.id) {
            when (itemMenu.id) {
                Constants.KT_HOP_DONG -> addFragment(CheckContractFragment(), false, true)
                Constants.CAP_NHAT_LOI -> addFragment(SearchFragment.newInstance(Constants.ARG_MENU_CNL, itemMenu.name), false, true)
                Constants.TAO_CHECK_LIST -> addFragment(SearchFragment.newInstance(Constants.ARG_MENU_CL, itemMenu.name), false, true)
                Constants.TAO_PRE_CHECK_LIST -> addFragment(SearchFragment.newInstance(Constants.ARG_MENU_PCL, itemMenu.name), false, true)
                Constants.TAO_LOI_MOI -> addFragment(CreateErrorFragment(), false, true)
                Constants.DANH_SACH_LOI -> addFragment(ListErrorFragment.newInstance(itemMenu.name), false, true)
                Constants.KQ_XAC_MINH ->
//                    addFragment(UploadImageFragment.newInstance("IscTest"),false,false)
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
            if (itemMenu.id.isNotBlank()) {
                if (itemMenu.id != Constants.KQ_XAC_MINH)
                    currentPage = itemMenu.id
                actMain_dlMenu.closeDrawers()
                logEventAnalytics(itemMenu)
            }
        } else actMain_dlMenu.closeDrawers()
    }

    fun handleShowMenu() {
        actMain_dlMenu.setDrawerLockMode(if (mCountBack == 0) DrawerLayout.LOCK_MODE_UNLOCKED else DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        actMain_ivMenuMain.setImageResource(if (mCountBack == 0) R.drawable.ic_menu else R.drawable.ic_back)
    }

    fun setTitleMain(model: TitleAndMenuModel?) {
        model?.let {
            actMain_tvTitleMain.text = deletePrefix(it.title)
            actMain_ivNotification.visibility = if (it.status) View.VISIBLE else View.GONE
            actMain_ivNotification.isFocusable = it.status
            actMain_ivNotification.setImageResource(if (it.status) it.image else Constants.NO_IMAGE)
        }
    }

    private fun deletePrefix(title: String): String {
        return if (title.contains("-")) title.replace("-", "").trim() else title.trim()
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

    private fun logEventAnalytics(item: ItemMenuModel) {
        mAnalytics?.let {
            val bundle = Bundle()
            bundle.putString(FirebaseAnalytics.Param.ITEM_ID, item.id)
            bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, deletePrefix(item.name))
            bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, Constants.EVENT_SLIDE_MENU)
            it.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle)
        }
    }
}
