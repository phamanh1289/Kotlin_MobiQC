package mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.contract.info_contract

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.fragment_info_contract.*
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.R
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.data.network.model.CompletedContractModel
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.data.network.model.ProcessingContractModel
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.data.network.model.ResponseModel
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.data.network.model.TitleAndMenuModel
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.others.constant.Constants
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.base.BaseFragment
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.contract.detail_contract.DetailContractFragment
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.contract.info_contract.diff.CompletedContractAdapter
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.utils.AppUtils
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.utils.KeyboardUtils
import javax.inject.Inject

/**
 * * Created by Anh Pham on 08/08/2018.     **
 * * Copyright (c) 2018 by FPT Telecom      **
 */
class InfoContractFragment : BaseFragment(), InfoContract.InfoContractView {

    @Inject
    lateinit var presenter: InfoContractPresenter

    private var typeContract = 0
    private var typeCheckList = 0
    private var fromDate = ""
    private var toDate = ""
    private var mobiAcc = ""
    private val listParams = HashMap<String, Any>()
    private val dataContract = ArrayList<CompletedContractModel>()
    private val adapterComplete: CompletedContractAdapter = CompletedContractAdapter(onClick = {
        listParams["ObjID"] = dataContract[it].ObjID.toString()
        addParams()
        getSharePreferences().listParams = Gson().toJson(listParams)
        addFragment(DetailContractFragment.newInstance(typeContract, typeCheckList, dataContract[it].Contract, dataContract[it].Date), true, true)
    })

    companion object {
        fun newInstance(type: Int, acc: String, checkList: Int, from: String, to: String): InfoContractFragment {
            val args = Bundle()
            args.putInt(Constants.ARG_TYPE_CONTRACT, type)
            args.putString(Constants.ARG_MOBI_ACC, acc)
            args.putInt(Constants.ARG_TYPE_CHECKLIST, checkList)
            args.putString(Constants.ARG_DATE_FROM, from)
            args.putString(Constants.ARG_DATE_TO, to)
            val fragment = InfoContractFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_info_contract, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getActivityComponent().inject(this)
        presenter.onAttach(this)
        setTitle(TitleAndMenuModel(title = getString(R.string.info_contract), status = false))
        activity?.let { KeyboardUtils.setupUI(view, activity = it) }
        initView()
    }

    private fun initView() {
        val bundle = arguments
        bundle?.let {
            showLoading()
            fromDate = it.getString(Constants.ARG_DATE_FROM)
            toDate = it.getString(Constants.ARG_DATE_TO)
            mobiAcc = it.getString(Constants.ARG_MOBI_ACC)
            typeCheckList = it.getInt(Constants.ARG_TYPE_CHECKLIST)
            typeContract = it.getInt(Constants.ARG_TYPE_CONTRACT)
            typeContract.let { type ->
                when (type) {
                    Constants.STATUS_PROCESSING -> {
                        handleRequestProcessing(typeCheckList)
                    }
                    Constants.STATUS_COMPLETED -> handleCompleted()
                }
            }
        }
    }

    private fun handleCompleted() {
        listParams["UserName"] = mobiAcc
        listParams["FromDate"] = AppUtils.toConvertDateFormat(context, fromDate)
        listParams["ToDate"] = AppUtils.toConvertDateFormat(context, toDate)
        listParams["Kind"] = Constants.DEFAULT_KIND
        listParams["SearchType"] = typeCheckList
        presenter.getCompletedContract(listParams)
    }

    private fun handleRequestProcessing(typeCheckList: Int) {
        listParams["UserName"] = mobiAcc
        listParams["Kind"] = Constants.DEFAULT_KIND
        addParams()
        if (typeCheckList == Constants.DEPLOYMENT)
            presenter.getDeploymentContractGroup(listParams)
        else
            presenter.getMaintenanceContractGroup(listParams)
    }

    private fun addParams() {
        listParams["From"] = fromDate.split("/")[0]
        listParams["FromDay"] = fromDate.split("/")[0]
        listParams["FromMonth"] = fromDate.split("/")[1]
        listParams["ToDay"] = toDate.split("/")[0]
        listParams["To"] = toDate.split("/")[0]
        listParams["ToMonth"] = toDate.split("/")[1]
    }

    private fun handleDataProcessing(list: ArrayList<ProcessingContractModel>) {
        val model: ProcessingContractModel? = list[0]
        model?.let { it ->
            fragInfoContract_llDataProcessing.visibility = View.VISIBLE
            fragInfoContract_tvGroupName?.text = mobiAcc
            fragInfoContract_tvContractAssign?.text = it.Assign.toString()
            fragInfoContract_tvContractFinish?.text = it.Finish.toString()
            fragInfoContract_tvContractInStock?.text = it.InStock.toString()
            fragInfoContract_tvContractInStockHiFPT?.text = "0"
            fragInfoContract_tvContractComing?.text = it.UpcomingSchedule.toString()
            fragInfoContract_tvContractOverTime?.text = it.OverSchedule.toString()
            fragInfoContract_tvLabelOvertime48.text = if (typeCheckList == Constants.DEPLOYMENT) getString(R.string.contract_over_time_48h) else getString(R.string.contract_up_coming)
            fragInfoContract_tvLabelOvertime96.text = if (typeCheckList == Constants.DEPLOYMENT) getString(R.string.contract_over_time_96h) else getString(R.string.contract_over_time)
            fragInfoContract_tvLabelInStockHiFPT.visibility = if (typeCheckList == Constants.DEPLOYMENT) View.GONE else View.VISIBLE
            fragInfoContract_tvSubmit.setOnClickListener { _ ->
                fragInfoContract_llDataProcessing.visibility = View.GONE
                showLoading()
                if (typeCheckList == Constants.DEPLOYMENT) presenter.getDeploymentContractList(listParams) else presenter.getMaintenanceContractList(listParams)
            }
        }
        hideLoading()
    }

    private fun handleDataCompleted(list: ArrayList<CompletedContractModel>) {
        dataContract.addAll(list)
        if (dataContract.size == 0)
            fragInfoContract_tvNoData.visibility = View.VISIBLE
        else {
            adapterComplete.submitList(dataContract)
            fragInfoContract_rvMain?.let {
                it.apply {
                    visibility = View.VISIBLE
                    layoutManager = LinearLayoutManager(context)
                    setHasFixedSize(true)
                    adapter = adapterComplete
                }
            }
        }
        hideLoading()
    }

    override fun loadCompletedContract(response: ResponseModel) {
        handleDataCompleted(Gson().fromJson(response.Data.toString(), object : TypeToken<ArrayList<CompletedContractModel>>() {}.type))
    }

    override fun loadDeploymentContractGroup(response: ResponseModel) {
        handleDataProcessing(Gson().fromJson(response.Data.toString(), object : TypeToken<ArrayList<ProcessingContractModel>>() {}.type))
    }

    override fun loadDeploymentContractList(response: ResponseModel) {
        handleDataCompleted(Gson().fromJson(response.Data.toString(), object : TypeToken<ArrayList<CompletedContractModel>>() {}.type))
    }

    override fun loadMaintenanceContractList(response: ResponseModel) {
        handleDataCompleted(Gson().fromJson(response.Data.toString(), object : TypeToken<ArrayList<CompletedContractModel>>() {}.type))
    }

    override fun loadMaintenanceContractGroup(response: ResponseModel) {
        handleDataProcessing(Gson().fromJson(response.Data.toString(), object : TypeToken<ArrayList<ProcessingContractModel>>() {}.type))
    }

    override fun handleError(error: String) {
        hideLoading()
        AppUtils.showDialog(fragmentManager, content = error, confirmDialogInterface = null)
    }
}