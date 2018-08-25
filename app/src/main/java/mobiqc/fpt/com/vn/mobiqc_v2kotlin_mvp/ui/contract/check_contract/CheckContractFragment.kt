package mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.contract.check_contract

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.fragment_check_contract.*
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.R
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.data.network.model.AccountGroup
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.data.network.model.SingleChoiceModel
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.data.network.model.TitleAndMenuModel
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.others.constant.Constants
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.others.datacore.DataCore
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.base.BaseFragment
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.contract.info_contract.InfoContractFragment
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.utils.AppUtils
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.utils.KeyboardUtils

/**
 * * Created by Anh Pham on 08/07/2018.     **
 * * Copyright (c) 2018 by FPT Telecom      **
 */
class CheckContractFragment : BaseFragment() {

    private lateinit var clickListener: View.OnClickListener
    private var dataMobiAcc = ArrayList<SingleChoiceModel>()
    private var dataMobiType = ArrayList<SingleChoiceModel>()
    var dataMobiGroup = ArrayList<AccountGroup>()

    var positionMobiGroup = 0
    var positionMobiAcc = 0
    var positionMobiType = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_check_contract, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTitle(TitleAndMenuModel(title = getString(R.string.menu_kt_hop_dong), status = true, image = R.drawable.ic_notifications))
        activity?.let { KeyboardUtils.setupUI(view, activity = it) }
        initView()
    }

    private fun initView() {
        handleData()
        handleOnClick()
        clickListener.let {
            fragCheckContract_tvMobiGroup.setOnClickListener(it)
            fragCheckContract_tvMobiAcc.setOnClickListener(it)
            fragCheckContract_tvMobiType.setOnClickListener(it)
            fragCheckContract_tvFromDate.setOnClickListener(it)
            fragCheckContract_tvToDate.setOnClickListener(it)
            fragCheckContract_tvSearch.setOnClickListener(it)
        }
        AppUtils.getDefaultDateSearch(fragCheckContract_tvToDate, fragCheckContract_tvFromDate, Constants.LATE_DATE)
    }

    private fun handleData() {
        if (dataMobiGroup.size == 0) {
            dataMobiGroup.addAll(Gson().fromJson(getSharePreferences().mobiAccount, object : TypeToken<ArrayList<AccountGroup>>() {}.type))
            dataMobiGroup[Constants.FIRST_ITEM].status = true
            fragCheckContract_tvMobiGroup.text = dataMobiGroup[Constants.FIRST_ITEM].group
        }
        getDataAcc(Constants.FIRST_ITEM)
        if (dataMobiType.size == 0)
            context?.let {
                dataMobiType = DataCore.getListLoaiTC(it)
                dataMobiType[Constants.FIRST_ITEM].status = true
                fragCheckContract_tvMobiType.text = dataMobiType[Constants.FIRST_ITEM].account
            }
    }

    fun getDataAcc(index: Int) {
        if (dataMobiAcc.size != 0)
            dataMobiAcc.forEach { it.status = false }
        dataMobiAcc = dataMobiGroup[index].accounts
        if (dataMobiAcc.size != 0) {
            dataMobiAcc[Constants.FIRST_ITEM].status = true
            fragCheckContract_tvMobiAcc.text = dataMobiAcc[Constants.FIRST_ITEM].account
        } else
            AppUtils.showDialog(fragmentManager, confirmDialogInterface = null, content = getString(R.string.mobi_acc_no_data))
    }

    private fun handleErrorSearch() {
        context?.let {
            val error = if (fragCheckContract_tvMobiAcc.text.isBlank())
                it.getString(R.string.error_permission)
            else
                AppUtils.handleCheckDate(it, fragCheckContract_tvFromDate.text.toString(), fragCheckContract_tvToDate.text.toString())
            if (error.isNotBlank())
                AppUtils.showDialog(fragmentManager, content = error, confirmDialogInterface = null)
            else {
                val type = if (fragCheckContract_rbProcessing.isChecked) Constants.STATUS_PROCESSING else Constants.STATUS_COMPLETED
                val acc = fragCheckContract_tvMobiAcc.text.toString().trim()
                val checkList = if (fragCheckContract_tvMobiType.text.toString() == getString(R.string.loai_tc_1)) Constants.DEPLOYMENT else Constants.MAINTENANCE
                val from = fragCheckContract_tvFromDate.text.toString()
                val to = fragCheckContract_tvToDate.text.toString()
                addFragment(InfoContractFragment.newInstance(type, acc, checkList, from, to), true, true)
            }
        }
    }

    private fun handleOnClick() {
        clickListener = View.OnClickListener { view ->
            when (view.id) {
                R.id.fragCheckContract_tvMobiGroup -> AppUtils.showDialogSingChoiceGroup(fragmentManager, getString(R.string.don_vi), dataMobiGroup, fragCheckContract_tvMobiGroup, positionMobiGroup)
                R.id.fragCheckContract_tvMobiAcc -> AppUtils.showDialogSingChoice(fragmentManager, getString(R.string.to), dataMobiAcc, fragCheckContract_tvMobiAcc, positionMobiAcc)
                R.id.fragCheckContract_tvMobiType -> AppUtils.showDialogSingChoice(fragmentManager, getString(R.string.to), dataMobiType, fragCheckContract_tvMobiType, positionMobiType)
                R.id.fragCheckContract_tvFromDate -> context?.let { AppUtils.showPickTime(it, fragCheckContract_tvFromDate, Constants.SET_MAX_DATE) }
                R.id.fragCheckContract_tvToDate -> context?.let { AppUtils.showPickTime(it, fragCheckContract_tvToDate, Constants.SET_MAX_DATE) }
                R.id.fragCheckContract_tvSearch -> handleErrorSearch()
            }
        }
    }
}