package mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.check_list.maintenance_check_list

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.fragment_check_list_maintenance.*
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.R
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.data.interfaces.MenuCheckListDialogInterface
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.data.network.model.CheckListModel
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.data.network.model.ResponseModel
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.others.constant.Constants
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.base.BaseFragment
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.check_list.all_check_list.AllCheckListContract
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.check_list.all_check_list.AllCheckListPresenter
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.check_list.all_check_list.diff.AllCheckListAdapter
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.contract.detail_contract.DetailContractFragment
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.utils.AppUtils
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.utils.KeyboardUtils
import javax.inject.Inject

/**
 * * Created by Anh Pham on 08/09/2018.     **
 * * Copyright (c) 2018 by FPT Telecom      **
 */
class MaintenanceCheckListFragment : BaseFragment(), AllCheckListContract.AllCheckListView, MenuCheckListDialogInterface {
    @Inject
    lateinit var presenter: AllCheckListPresenter
    private var contractName = ""

    private lateinit var mAdapterAll: AllCheckListAdapter
    private var dataCheckList = ArrayList<CheckListModel>()

    companion object {
        fun newInstance(type: String): MaintenanceCheckListFragment {
            val args = Bundle()
            args.putString(Constants.ARG_CONTRACT, type)
            val fragment = MaintenanceCheckListFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_check_list_maintenance, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getActivityComponent().inject(this)
        presenter.onAttach(this)
        activity?.let { KeyboardUtils.setupUI(view, activity = it) }
        initView()
    }

    private fun initView() {
        handleArgument()
    }

    private fun handleArgument() {
        val bundle = arguments
        bundle?.let { item ->
            contractName = item.getString(Constants.ARG_CONTRACT)
            presenter.let {
                val map = HashMap<String, Any>()
                map[Constants.PARAMS_OBJID] = contractName
                map[Constants.PARAMS_TYPE] = Constants.MAINTENANCE
                it.getAllCheckList(map)
            }
        }
    }

    private fun handleDataCheckList(list: ArrayList<CheckListModel>) {
        dataCheckList = list
        mAdapterAll = AllCheckListAdapter { index ->
            AppUtils.showMenuCheckListDialog(fragmentManager, confirmDialogInterface = this, index = index)
        }
        mAdapterAll.submitList(dataCheckList)
        fragCheckListMaintenance_rvMain.apply {
            adapter = mAdapterAll
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
        }
        fragCheckListMaintenance_tvNoData.visibility = if (dataCheckList.size != 0) View.VISIBLE else View.GONE
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
        AppUtils.showDialog(fragmentManager, content = "Error", confirmDialogInterface = null)
    }

    override fun onClickDetail(index: Int) {
        val model = dataCheckList[index]
        addFragment(DetailContractFragment.newInstance(model.ID.toString(), Constants.STATUS_COMPLETED, model.ObjID, Constants.MAINTENANCE, model.Contract, model.Date), true, true)
    }
}