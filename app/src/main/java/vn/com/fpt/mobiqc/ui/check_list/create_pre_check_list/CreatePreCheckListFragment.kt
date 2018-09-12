package vn.com.fpt.mobiqc.ui.check_list.create_pre_check_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.fragment_create_pre_check_list.*
import vn.com.fpt.mobiqc.R
import vn.com.fpt.mobiqc.data.interfaces.ConfirmDialogInterface
import vn.com.fpt.mobiqc.data.network.model.*
import vn.com.fpt.mobiqc.others.constant.Constants
import vn.com.fpt.mobiqc.others.datacore.DataCore
import vn.com.fpt.mobiqc.ui.base.BaseFragment
import vn.com.fpt.mobiqc.ui.blank.CreatePreCheckListContract
import vn.com.fpt.mobiqc.ui.contract.dialog_detail_contract.DetailContractDialogFragment
import vn.com.fpt.mobiqc.utils.AppUtils
import vn.com.fpt.mobiqc.utils.KeyboardUtils
import javax.inject.Inject

/**
 * * Created by Anh Pham on 08/09/2018.     **
 * * Copyright (c) 2018 by FPT Telecom      **
 */
class CreatePreCheckListFragment : BaseFragment(), CreatePreCheckListContract.CreatePreCheckListView {

    @Inject
    lateinit var presenter: CreatePreCheckListPresenter

    var listFirstStatus = ArrayList<SingleChoiceModel>()
    lateinit var contractModel: ContractDetailModel
    var positionFirstStatus = 0
    private val onClickSuccess = object : ConfirmDialogInterface {
        override fun onClickOk() {
            clearAllBackStack()
        }

        override fun onClickCancel() {

        }
    }

    companion object {
        fun newInstance(model: String): CreatePreCheckListFragment {
            val args = Bundle()
            args.putString(Constants.ARG_CONTRACT_DETAIL, model)
            val fragment = CreatePreCheckListFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_create_pre_check_list, container, false)
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
        initClickAction()
    }

    fun showDialogDetailContract() {
        val dialogContract = DetailContractDialogFragment()
        dialogContract.setData(contractModel, contractModel.Contract)
        dialogContract.show(fragmentManager, DetailContractDialogFragment::class.java.simpleName)
    }

    private fun handleDataArgument() {
        arguments?.let {
            val json = it.getString(Constants.ARG_CONTRACT_DETAIL) ?: ""
            contractModel = Gson().fromJson(json, ContractDetailModel::class.java)
        }
        setTitle(TitleAndMenuModel(title = contractModel.Contract, status = true, image = R.drawable.ic_info))
        listFirstStatus = DataCore.getPreFirstStatus(context)
        fragPreCheckList_tvFirstStatus.text = listFirstStatus[Constants.FIRST_ITEM].account
        fragPreCheckList_tvContractNumber.text = contractModel.Contract
    }

    private fun initClickAction() {
        fragPreCheckList_tvSubmit.setOnClickListener {
            AppUtils.showDialog(fragmentManager, content = getString(R.string.pre_check_list_mess), actionCancel = true, confirmDialogInterface = object : ConfirmDialogInterface {
                override fun onClickOk() {
                    presenter.let { pre ->
                        showLoading()
                        val map = HashMap<String, Any>()
                        map[Constants.PARAMS_OBJID] = contractModel.ObjID
                        pre.postSupportListRemainCheck(map)
                    }
                }

                override fun onClickCancel() {

                }
            })
        }
        fragPreCheckList_tvFirstStatus.setOnClickListener { AppUtils.showDialogSingChoice(fragmentManager, getString(R.string.create_check_list_status), listFirstStatus, fragPreCheckList_tvFirstStatus, positionFirstStatus) }
    }

    private fun handleRemainCheck(list: ArrayList<StatusCheckListModel>) {
        if (list.size != 0) {
            val item = list[Constants.FIRST_ITEM]
            if (item.StatusCL == Constants.CREATE_SUCCESS)
                presenter.let {
                    val paramPreCL = HashMap<String, Any>()
                    paramPreCL[Constants.PARAMS_OBJID] = contractModel.ObjID.toBigDecimal()
                    paramPreCL[Constants.PARAMS_LOCATION_NAME] = fragPreCheckList_etUserName.text.toString()
                    paramPreCL[Constants.PARAMS_LOCATION_PHONE] = fragPreCheckList_etPhone.text.toString()
                    paramPreCL[Constants.PARAMS_FIRST_STATUS] = listFirstStatus[positionFirstStatus].id
                    paramPreCL[Constants.PARAMS_DESCRIPTION] = fragPreCheckList_etNote.text.toString()
                    paramPreCL[Constants.PARAMS_USER_NAME] = getSharePreferences().accountName
                    val map = HashMap<String, Any>()
                    map[Constants.PARAMS_PRE_CL] = paramPreCL
                    it.postCreatePreChecklist(map)
                }
            else {
                hideLoading()
                AppUtils.showDialog(fragmentManager, content = getString(R.string.pre_check_list_error_mes), confirmDialogInterface = null)
            }
        } else hideLoading()
    }

    override fun loadCreatePreChecklist(response: ResponseModel) {
        if (response.Description.isNotBlank()) {
            AppUtils.showDialog(fragmentManager, content = response.Description, confirmDialogInterface = onClickSuccess)
            hideLoading()
        }
    }

    override fun loadSupportListRemainCheck(response: ResponseLowCaseModel) {
        handleRemainCheck(Gson().fromJson(Gson().toJson(response.data), object : TypeToken<ArrayList<StatusCheckListModel>>() {}.type))
    }

    override fun handleError(error: String) {
        hideLoading()
        AppUtils.showDialog(fragmentManager, content = error, confirmDialogInterface = null)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onDetach()
    }
}