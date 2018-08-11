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
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.others.dialog.SelectSingleGroupPopup
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.others.dialog.SelectSinglePopup
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.base.BaseFragment
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.contract.info_contract.InfoContractFragment
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.utils.AppUtils
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.utils.KeyboardUtils
import javax.inject.Inject

/**
 * * Created by Anh Pham on 08/07/2018.     **
 * * Copyright (c) 2018 by FPT Telecom      **
 */
class CheckContractFragment : BaseFragment(), CheckContract.CheckContractView {

    @Inject
    lateinit var presenter: CheckContractPresenter
    lateinit var clickListener: View.OnClickListener
    private lateinit var choiceMobiAcc: SelectSinglePopup
    private lateinit var choiceMobiType: SelectSinglePopup
    private lateinit var choiceMobiGroup: SelectSingleGroupPopup
    private var dataMobiAcc = ArrayList<SingleChoiceModel>()
    private var dataMobiType = ArrayList<SingleChoiceModel>()
    private var dataMobiGroup = ArrayList<AccountGroup>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_check_contract, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getActivityComponent().inject(this)
        presenter.onAttach(this)
        setTitle(TitleAndMenuModel(title = getString(R.string.menu_kt_hop_dong), status = true,image = R.drawable.ic_notifications))
        activity?.let { KeyboardUtils.setupUI(view, activity = it) }
        initView()
    }

    private fun initView() {
        handleData()
        initPopUp()
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

    private fun getDataAcc(index: Int) {
        if (dataMobiAcc.size != 0) {
            dataMobiAcc[choiceMobiAcc.singleAdapter.indexSelect].status = false
            choiceMobiAcc.singleAdapter.indexSelect = Constants.FIRST_ITEM
        }
        dataMobiAcc = dataMobiGroup[index].accounts
        dataMobiAcc[Constants.FIRST_ITEM].status = true
        fragCheckContract_tvMobiAcc.text = dataMobiAcc[Constants.FIRST_ITEM].account
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
                val checkList = dataMobiType[choiceMobiType.getSelectIndex()].id
                val from = fragCheckContract_tvFromDate.text.toString()
                val to = fragCheckContract_tvToDate.text.toString()
                addFragment(InfoContractFragment.newInstance(type, acc, checkList, from, to), true, true)
            }
        }
    }

    private fun initPopUp() {
        choiceMobiGroup = SelectSingleGroupPopup(dataMobiGroup
        ) { index ->
            dataMobiGroup[choiceMobiGroup.getSelectIndex()].status = false
            choiceMobiGroup.singleAdapter.notifyItemChanged(choiceMobiGroup.getSelectIndex())
            dataMobiGroup[index].status = true
            choiceMobiGroup.singleAdapter.notifyItemChanged(index)
            choiceMobiGroup.dismiss()
            fragCheckContract_tvMobiGroup.text = dataMobiGroup[index].group
            getDataAcc(index)
            choiceMobiAcc.singleAdapter.notifyDataSetChanged()
        }
        choiceMobiGroup.onCreateView(context = context!!)
        choiceMobiAcc = SelectSinglePopup(dataMobiAcc
        ) { index ->
            dataMobiAcc[choiceMobiAcc.getSelectIndex()].status = false
            choiceMobiAcc.singleAdapter.notifyItemChanged(choiceMobiAcc.getSelectIndex())
            dataMobiAcc[index].status = true
            choiceMobiAcc.singleAdapter.notifyItemChanged(index)
            choiceMobiAcc.dismiss()
            fragCheckContract_tvMobiAcc.text = dataMobiAcc[index].account
        }
        choiceMobiAcc.onCreateView(context = context!!)

        choiceMobiType = SelectSinglePopup(dataMobiType
        ) { index ->
            dataMobiType[choiceMobiType.getSelectIndex()].status = false
            choiceMobiType.singleAdapter.notifyItemChanged(choiceMobiType.getSelectIndex())
            dataMobiType[index].status = true
            choiceMobiType.singleAdapter.notifyItemChanged(index)
            choiceMobiType.dismiss()
            fragCheckContract_tvMobiType.text = dataMobiType[index].account
        }
        choiceMobiType.onCreateView(context = context!!)
    }

    private fun handleOnClick() {
        clickListener = View.OnClickListener { view ->
            when (view.id) {
                R.id.fragCheckContract_tvMobiGroup -> choiceMobiGroup.showAsDropDown(view)
                R.id.fragCheckContract_tvMobiAcc -> choiceMobiAcc.showAsDropDown(view)
                R.id.fragCheckContract_tvMobiType -> choiceMobiType.showAsDropDown(view)
                R.id.fragCheckContract_tvFromDate -> context?.let { AppUtils.showPickTime(it, fragCheckContract_tvFromDate) }

                R.id.fragCheckContract_tvToDate -> context?.let { AppUtils.showPickTime(it, fragCheckContract_tvToDate) }

                R.id.fragCheckContract_tvSearch -> handleErrorSearch()
            }
        }
    }
}