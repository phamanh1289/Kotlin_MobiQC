package vn.com.fpt.mobiqc.ui.check_list.deployment_check_list

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.fragment_check_list_deployment.*
import vn.com.fpt.mobiqc.R
import vn.com.fpt.mobiqc.data.interfaces.MenuCheckListDialogInterface
import vn.com.fpt.mobiqc.data.network.model.CheckListModel
import vn.com.fpt.mobiqc.data.network.model.ResponseModel
import vn.com.fpt.mobiqc.others.constant.Constants
import vn.com.fpt.mobiqc.ui.base.BaseFragment
import vn.com.fpt.mobiqc.ui.check_list.all_check_list.diff.AllCheckListAdapter
import vn.com.fpt.mobiqc.ui.contract.detail_contract.DetailContractFragment
import vn.com.fpt.mobiqc.ui.error.update.UpdateErrorFragment
import vn.com.fpt.mobiqc.utils.AppUtils
import vn.com.fpt.mobiqc.utils.KeyboardUtils
import javax.inject.Inject

/**
 * * Created by Anh Pham on 08/09/2018.     **
 * * Copyright (c) 2018 by FPT Telecom      **
 */
class DeploymentCheckListFragment : BaseFragment(), DeploymentCheckListContract.DeploymentCheckListView, MenuCheckListDialogInterface {

    @Inject
    lateinit var presenter: DeploymentCheckListPresenter
    private var contractName = ""

    private lateinit var mAdapterAll: AllCheckListAdapter
    private var dataCheckList = ArrayList<CheckListModel>()

    companion object {
        fun newInstance(type: String): DeploymentCheckListFragment {
            val args = Bundle()
            args.putString(Constants.ARG_CONTRACT, type)
            val fragment = DeploymentCheckListFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_check_list_deployment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getActivityComponent().inject(this)
        presenter.onAttach(this)
        activity?.let { KeyboardUtils.setupUI(view, activity = it) }
        handleArgument()
    }

    private fun handleArgument() {
        val bundle = arguments
        bundle?.let { item ->
            contractName = item.getString(Constants.ARG_CONTRACT) ?: ""
            presenter.let {
                val map = HashMap<String, Any>()
                map[Constants.PARAMS_OBJID] = contractName
                map[Constants.PARAMS_TYPE] = Constants.DEPLOYMENT
                it.getAllCheckList(map)
            }
        }
    }

    private fun handleDataCheckList(list: ArrayList<CheckListModel>) {
        dataCheckList = list
        mAdapterAll = AllCheckListAdapter { index ->
            AppUtils.showMenuCheckListDialog(fragmentManager, confirmDialogInterface = this, index = index, typeOption = false)
        }
        mAdapterAll.submitList(dataCheckList)
        fragCheckListDeployment_rvMain.apply {
            adapter = mAdapterAll
            val layout = LinearLayoutManager(context)
            layout.orientation = LinearLayoutManager.VERTICAL
            layoutManager = layout
            setHasFixedSize(true)
        }
        fragCheckListDeployment_tvNoData.visibility = if (dataCheckList.size == 0) View.VISIBLE else View.GONE
        hideLoading()
    }

    override fun loadAllCheckList(response: ResponseModel) {
        handleDataCheckList(Gson().fromJson(response.Data.toString(), object : TypeToken<ArrayList<CheckListModel>>() {}.type))
    }

    override fun handleError(error: String) {
        hideLoading()
        AppUtils.showDialog(fragmentManager, content = error, confirmDialogInterface = null)
    }

    override fun onClickError(index: Int) {
        val model = dataCheckList[index]
        addFragment(UpdateErrorFragment.newInstance(model.ID.toBigDecimal().toString(), model.ObjID.toBigDecimal().toString(), model.Contract, Constants.DEPLOYMENT, model.UpdateBy,model.Date), true, true)
    }

    override fun onClickDetail(index: Int) {
        val model = dataCheckList[index]
        addFragment(DetailContractFragment.newInstance(model.ID.toString(), Constants.STATUS_COMPLETED, model.ObjID, Constants.DEPLOYMENT, model.Contract, model.Date), true, true)
    }

    override fun onClickUpdateDetail(index: Int) {

    }

    override fun onClickUpdateStatus(index: Int) {

    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onDetach()
    }
}