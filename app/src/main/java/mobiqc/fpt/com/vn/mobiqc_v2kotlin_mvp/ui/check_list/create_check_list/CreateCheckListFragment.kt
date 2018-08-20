package mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.blank

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
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.data.network.model.*
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.others.constant.Constants
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.others.datacore.DataCore
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.base.BaseFragment
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.check_list.create_check_list.CreateCheckListContract
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.check_list.create_check_list.CreateCheckListPresenter
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.check_list.create_check_list.diff.CreateCheckListAdapter
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
    var positionFirstStatus = 0
    private var ownerType = 0
    //True -> yêu cầu khẩn cấp, False : không
    var isCheckOwner = false

    private var listFirstStatus = ArrayList<SingleChoiceModel>()

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

    private fun initViewTimeZone() {
        adapterTimeZone = CreateCheckListAdapter {
            if (adapterTimeZone.indexSelect != -1) {
                listTimeZone[adapterTimeZone.indexSelect].status = false
                adapterTimeZone.notifyItemChanged(adapterTimeZone.indexSelect)
            }
            listTimeZone[it].status = true
            adapterTimeZone.notifyItemChanged(it)
        }
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
        fragCreateCheckList_tvDate.setOnClickListener { AppUtils.showPickTime(context, fragCreateCheckList_tvDate, false) }
        fragCreateCheckList_imgClear.setOnClickListener {
            fragCreateCheckList_tvDate.setText("")
            if (fragCreateCheckList_cbGuestDate.isChecked)
                fragCreateCheckList_cbGuestDate.isChecked = false
        }
        fragCreateCheckList_cbGuestDate.setOnCheckedChangeListener { _, isCheck ->
            fragCreateCheckList_llTimeZone.visibility = if (isCheck) View.VISIBLE else View.GONE
            if (isCheck)
                initParamGetTimeZone()
        }

        fragCreateCheckList_cbRush.setOnCheckedChangeListener { _, isCheck ->
            isCheckOwner = isCheck
            initParamGetOwner(isCheckOwner)
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
            }
        } else
            AppUtils.showDialog(fragmentManager, content = getString(R.string.alert_create_check_list), confirmDialogInterface = null)
        hideLoading()
    }

    private fun handleDataTimeZone(list: ArrayList<PartnerTimeZoneModel>) {
        listTimeZone = list
        if (listTimeZone.size != 0) {
            initViewTimeZone()
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
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }
}