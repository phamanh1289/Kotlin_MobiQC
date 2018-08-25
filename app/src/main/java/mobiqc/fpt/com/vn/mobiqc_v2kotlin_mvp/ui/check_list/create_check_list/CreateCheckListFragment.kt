package mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.check_list.create_check_list

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.fragment_create_check_list.*
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.R
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.data.interfaces.ConfirmDialogInterface
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.data.network.model.*
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.others.constant.Constants
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.others.datacore.DataCore
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.base.BaseFragment
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.check_list.create_check_list.diff.CreateCheckListAdapter
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.contract.dialog_detail_contract.DetailContractDialogFragment
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.utils.AppUtils
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.utils.KeyboardUtils
import javax.inject.Inject

/**
 * * Created by Anh Pham on 08/09/2018.     **
 * * Copyright (c) 2018 by FPT Telecom      **
 */
class CreateCheckListFragment : BaseFragment(), CreateCheckListContract.CreateCheckListView {

    @Inject
    lateinit var presenter: CreateCheckListPresenter

    private lateinit var adapterTimeZone: CreateCheckListAdapter
    private lateinit var contractModel: ContractDetailModel
    private var listTimeZone = ArrayList<PartnerTimeZoneModel>()
    private var listFirstStatus = ArrayList<SingleChoiceModel>()
    private var listParams = HashMap<String, Any>()
    var positionFirstStatus = 0
    private var ownerType = 0
    private var pDepID = 0
    //True -> yêu cầu khẩn cấp, False : không
    var isCheckOwner = false
    private var isCheckGuestDate = false
    private val onClickSuccess = object : ConfirmDialogInterface {
        override fun onClickOk() {
            clearAllBackStack()
        }

        override fun onClickCancel() {

        }
    }

    companion object {
        fun newInstance(model: String): CreateCheckListFragment {
            val args = Bundle()
            args.putString(Constants.ARG_CONTRACT_DETAIL, model)
            val fragment = CreateCheckListFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_create_check_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getActivityComponent().inject(this)
        presenter.onAttach(this)
        activity?.let { KeyboardUtils.setupUI(view, activity = it) }
        initView()
    }

    private fun initView() {
        handleDataArgument()
        initOnClick()
    }

    fun showDialogDetailContract() {
        val dialogContract = DetailContractDialogFragment()
        dialogContract.setData(contractModel, contractModel.Contract)
        dialogContract.show(fragmentManager, DetailContractDialogFragment::class.java.simpleName)
    }

    private fun initViewTimeZone() {
        adapterTimeZone = CreateCheckListAdapter {
            if (adapterTimeZone.indexSelect != -1) {
                listTimeZone[adapterTimeZone.indexSelect].status = false
                adapterTimeZone.notifyItemChanged(adapterTimeZone.indexSelect)
            }
            listTimeZone[it].status = true
            adapterTimeZone.notifyItemChanged(it)
        }
        adapterTimeZone.reasonId = listFirstStatus[positionFirstStatus].id
        adapterTimeZone.submitList(listTimeZone)
        fragCreateCheckList_rvMain.apply {
            val layout = LinearLayoutManager(context)
            layout.orientation = LinearLayoutManager.VERTICAL
            layoutManager = layout
            setHasFixedSize(true)
            adapter = adapterTimeZone
            isNestedScrollingEnabled = false
        }
    }

    private fun initOnClick() {
        fragCreateCheckList_tvSubmit.setOnClickListener {
            AppUtils.showDialog(fragmentManager, content = getString(R.string.create_check_list_mess), actionCancel = true, confirmDialogInterface = null)
        }
        fragCreateCheckList_tvStatus.setOnClickListener { AppUtils.showDialogSingChoice(fragmentManager, getString(R.string.create_check_list_status), listFirstStatus, fragCreateCheckList_tvStatus, positionFirstStatus) }
        fragCreateCheckList_tvDate.setOnClickListener { AppUtils.showPickTime(context, fragCreateCheckList_tvDate, Constants.SET_MIN_DATE) }
        fragCreateCheckList_imgClear.setOnClickListener {
            fragCreateCheckList_tvDate.setText("")
            if (fragCreateCheckList_cbGuestDate.isChecked)
                fragCreateCheckList_cbGuestDate.isChecked = false
        }
        fragCreateCheckList_cbGuestDate.setOnCheckedChangeListener { _, isCheck ->
            isCheckGuestDate = isCheck
            fragCreateCheckList_llTimeZone.visibility = if (isCheck) View.VISIBLE else View.GONE
            if (isCheck)
                initParamGetTimeZone()
        }
        fragCreateCheckList_cbRush.setOnCheckedChangeListener { _, isCheck ->
            isCheckOwner = isCheck
            initParamGetOwner(isCheckOwner)
        }
        fragCreateCheckList_tvSubmit.setOnClickListener { initActionSubmit() }
    }

    private fun initActionSubmit() {
        if (fragCreateCheckList_tvPartner.text.isBlank() || fragCreateCheckList_tvSub.text.isBlank())
            AppUtils.showDialog(fragmentManager, confirmDialogInterface = null, content = getString(R.string.alert_create_check_list))
        else if (fragCreateCheckList_cbGuestDate.isChecked && adapterTimeZone.indexSelect == Constants.DONT_BOOK_DATE)
            AppUtils.showDialog(fragmentManager, content = getString(R.string.time_zone_error_mes), confirmDialogInterface = null)
        else {
            AppUtils.showDialog(fragmentManager, content = getString(R.string.create_check_list_mess), actionCancel = true, confirmDialogInterface = object : ConfirmDialogInterface {
                override fun onClickOk() {
                    presenter.let {
                        showLoading()
                        val map = HashMap<String, Any>()
                        map[Constants.PARAMS_OBJID] = contractModel.ObjID
                        it.postSupportListRemainCheck(map)
                    }
                }

                override fun onClickCancel() {

                }
            })
        }
    }

    private fun initParamGetTimeZone() {
        presenter.let {
            showLoading()
            val map = HashMap<String, Any>()
            map[Constants.PARAMS_SUPPORTER] = fragCreateCheckList_tvPartner.text.toString()
            map[Constants.PARAMS_SUB_ID] = fragCreateCheckList_tvSub.text.toString()
            map[Constants.PARAMS_APPOINTMENT_DATE] = AppUtils.toConvertDateFormat(context, fragCreateCheckList_tvDate.text.toString())
            map[Constants.PARAMS_DATE] = AppUtils.toConvertDateFormat(context, AppUtils.getCurrentDate(Constants.CURRENT_DATE))
            it.getPartnerTimezoneAbilityList(map)
        }
    }

    fun initParamGetOwner(isCheck: Boolean) {
        presenter.let {
            showLoading()
            val map = HashMap<String, Any>()
            map[Constants.PARAMS_OBJID] = contractModel.ObjID
            if (!isCheck) {
                map[Constants.PARAMS_INIT_STATUS] = listFirstStatus[positionFirstStatus].id
                map[Constants.PARAMS_LOCATON_ID_LOW] = Constants.PARAMS_NO_VALUE_INT
                map[Constants.PARAMS_LOCAL_TYPE_ID] = Constants.PARAMS_NO_VALUE_INT
                it.getOwnerTypeByInitStatus(map)
            } else it.getOwnerTypeByPopManage(map)
        }
    }

    private fun initParamSubTeam() {
        presenter.let {
            val map = HashMap<String, Any>()
            map[Constants.PARAMS_OBJID] = contractModel.ObjID.toBigDecimal()
            map[Constants.PARAMS_REASON_ID] = listFirstStatus[positionFirstStatus].id
            map[Constants.PARAMS_OWNER_TYPE] = ownerType
            it.getSubTeamID(map)
        }
    }

    private fun handleDataArgument() {
        arguments?.let {
            val json = it.getString(Constants.ARG_CONTRACT_DETAIL) ?: ""
            contractModel = Gson().fromJson(json, ContractDetailModel::class.java)
        }
        setTitle(TitleAndMenuModel(title = contractModel.Contract, status = true, image = R.drawable.ic_info))
        listFirstStatus = DataCore.getListFirstStatus(context)
        fragCreateCheckList_tvStatus.text = listFirstStatus[Constants.FIRST_ITEM].account
        fragCreateCheckList_tvDate.onChange { it }
        initParamGetOwner(isCheckOwner)
    }

    private fun handleDataOwner(data: Any) {
        if (data is String)
            ownerType = data.toInt()
        else {
            val listOwner: ArrayList<OwnerTypeModel> = Gson().fromJson(Gson().toJson(data), object : TypeToken<ArrayList<OwnerTypeModel>>() {}.type)
            if (listOwner.size != 0)
                ownerType = listOwner[Constants.FIRST_ITEM].OwnerType
        }
        initParamSubTeam()
    }

    private fun handleDataSubTeam(list: ArrayList<SubTeamModel>) {
        if (list.size != 0) {
            val model = list[Constants.FIRST_ITEM]
            model.let {
                fragCreateCheckList_tvSub.text = it.ResultSubID
                fragCreateCheckList_tvPartner.text = it.ResultDepID
                pDepID = it.ResultCode
            }
        }
        hideLoading()
        if (isCheckGuestDate)
            initParamGetTimeZone()
    }

    private fun handleDataTimeZone(list: ArrayList<PartnerTimeZoneModel>) {
        listTimeZone = list
        if (listTimeZone.size != 0) {
            initViewTimeZone()
        }
        hideLoading()
    }

    private fun handleRemainCheck(list: ArrayList<StatusCheckListModel>) {
        if (list.size != 0) {
            val item = list[Constants.FIRST_ITEM]
            if (item.StatusCL == Constants.CREATE_CHECK_LIST_SUCCESS)
                presenter.let {
                    val map = HashMap<String, Any>()
                    map[Constants.PARAMS_OBJID] = contractModel.ObjID.toBigDecimal()
                    it.postCheckRemainPTC(map)
                }
            else {
                hideLoading()
                AppUtils.showDialog(fragmentManager, content = getString(R.string.check_list_error_mes), confirmDialogInterface = null)
            }
        } else hideLoading()
    }

    private fun handleRemainPTC(list: ArrayList<StatusCheckListModel>) {
        if (list.size != 0) {
            val item = list[Constants.FIRST_ITEM]
            when (item.Statusinf) {
                Constants.CREATE_CHECK_LIST_FAIL -> {
                    hideLoading()
                    AppUtils.showDialog(fragmentManager, content = getString(R.string.mess_error_check_ptc), confirmDialogInterface = null)
                }
                else -> {
                    listParams[Constants.PARAMS_OBJID] = contractModel.ObjID.toBigDecimal()
                    listParams[Constants.PARAMS_CREATE_BY] = getSharePreferences().accountName
                    listParams[Constants.PARAMS_INIT_STATUS] = listFirstStatus[positionFirstStatus].id
                    listParams[Constants.PARAMS_DESCRIPTION] = fragCreateCheckList_tvNote.text.toString()
                    listParams[Constants.PARAMS_SUPPORTER] = fragCreateCheckList_tvPartner.text.toString()
                    listParams[Constants.PARAMS_SUB_SUPPORTER] = fragCreateCheckList_tvSub.text.toString()
                    listParams[Constants.PARAMS_DEP_ID] = pDepID
                    listParams[Constants.PARAMS_REQUEST_FROM] = Constants.DEFAULT_REQUEST_FROM_MOBI_QC
                    listParams[Constants.PARAMS_OWNER_TYPE] = ownerType
                    if (item.Statusinf == Constants.CREATE_CHECK_LIST_SUCCESS)
                        presenter.postCreateChecklist(listParams)
                    else {
                        hideLoading()
                        AppUtils.showDialog(fragmentManager, content = getString(R.string.mess_error_check_ptc), actionCancel = true, confirmDialogInterface = object : ConfirmDialogInterface {
                            override fun onClickOk() {
                                showLoading()
                                presenter.postCreateChecklist(listParams)
                            }

                            override fun onClickCancel() {

                            }
                        })
                    }
                }
            }
        } else hideLoading()
    }

    private fun handleCreateCheck(data: String) {
        if (data.isNotBlank()) {
            val result = if (data.contains("."))
                data.split(".")[Constants.FIRST_ITEM] else data
            when {
                result.toInt() != 0 -> {
                    if (!fragCreateCheckList_cbGuestDate.isChecked) {
                        hideLoading()
                        AppUtils.showDialog(fragmentManager, content = getString(R.string.create_check_list_success), confirmDialogInterface = onClickSuccess)
                    } else {
                        presenter.let {
                            val map = HashMap<String, Any>()
                            map[Constants.PARAMS_USER_NAME_NON_CAPWORD] = getSharePreferences().accountName
                            map[Constants.PARAMS_SUPID] = data.toBigDecimal()
                            map[Constants.PARAMS_SUPPORTER] = fragCreateCheckList_tvPartner.text.toString()
                            map[Constants.PARAMS_SUB_ID] = fragCreateCheckList_tvSub.text.toString()
                            map[Constants.PARAMS_APPOINTMENT_DATE] = AppUtils.toConvertDateFormat(context, fragCreateCheckList_tvDate.text.toString())
                            map[Constants.PARAMS_TIME_ZONE] = listTimeZone[adapterTimeZone.indexSelect].Timezone
                            it.postSupportListAssignInsert(map)
                        }
                    }
                }
                else -> {
                    hideLoading()
                    AppUtils.showDialog(fragmentManager, content = getString(R.string.create_check_list_fail), confirmDialogInterface = null)
                }
            }
        }
    }

    private fun handleDataListAssignInsert(data: String) {
        if (data.isNotBlank()) {
            val result = if (data.contains("."))
                data.split(".")[Constants.FIRST_ITEM] else data
            when (result.toInt()) {
                Constants.REQUEST_SUCCESS ->
                    AppUtils.showDialog(fragmentManager, content = getString(R.string.create_check_list_success), confirmDialogInterface = onClickSuccess)
                else ->
                    AppUtils.showDialog(fragmentManager, content = getString(R.string.create_check_list_fail), confirmDialogInterface = null)
            }
        }
        hideLoading()
    }

    override fun loadSubTeamID(response: ResponseLowCaseModel) {
        handleDataSubTeam(Gson().fromJson(Gson().toJson(response.data), object : TypeToken<ArrayList<SubTeamModel>>() {}.type))
    }

    override fun loadOwnerType(response: ResponseLowCaseModel) {
        handleDataOwner(response.data)
    }

    override fun loadPartnerTimezoneAbilityList(response: ResponseLowCaseModel) {
        handleDataTimeZone(Gson().fromJson(Gson().toJson(response.data), object : TypeToken<ArrayList<PartnerTimeZoneModel>>() {}.type))
    }

    override fun loadSupportListRemainCheck(response: ResponseLowCaseModel) {
        handleRemainCheck(Gson().fromJson(Gson().toJson(response.data), object : TypeToken<ArrayList<StatusCheckListModel>>() {}.type))
    }

    override fun loadCheckRemainPTC(response: ResponseLowCaseModel) {
        handleRemainPTC(Gson().fromJson(Gson().toJson(response.data), object : TypeToken<ArrayList<StatusCheckListModel>>() {}.type))
    }

    override fun loadCreateChecklist(response: ResponseLowCaseModel) {
        handleCreateCheck(response.data.toString())
    }

    override fun loadSupportListAssignInsert(response: ResponseLowCaseModel) {
        handleDataListAssignInsert(response.data.toString())
    }

    override fun handleError(error: String) {
        hideLoading()
        AppUtils.showDialog(fragmentManager, content = error, confirmDialogInterface = null)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onDetach()
    }

    private fun EditText.onChange(cb: (String) -> Unit) {
        this.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                fragCreateCheckList_imgClear.visibility = if (s?.length != 0) View.VISIBLE else View.GONE
                fragCreateCheckList_cbGuestDate.isEnabled = s?.length != 0
                if (s?.length != 0 && fragCreateCheckList_cbGuestDate.isChecked)
                    initParamGetTimeZone()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }
}