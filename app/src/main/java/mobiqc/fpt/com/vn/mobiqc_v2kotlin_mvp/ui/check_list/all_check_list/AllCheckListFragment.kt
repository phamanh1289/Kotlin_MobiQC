package mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.check_list.all_check_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_all_check_list.*
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.R
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.data.network.model.ContractDetailModel
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.data.network.model.TitleAndMenuModel
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.others.constant.Constants
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.base.BaseFragment
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.check_list.deployment_check_list.DeploymentCheckListFragment
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.check_list.maintenance_check_list.MaintenanceCheckListFragment
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.contract.dialog_detail_contract.DetailContractDialogFragment
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.utils.KeyboardUtils

/**
 * * Created by Anh Pham on 08/09/2018.     **
 * * Copyright (c) 2018 by FPT Telecom      **
 */
class AllCheckListFragment : BaseFragment() {

    private lateinit var mViewPagerAdapter: ViewPagerCheckListAdapter
    private lateinit var contractModel: ContractDetailModel
    private var contractNumber = ""

    companion object {
        fun newInstance(model: String, number: String): AllCheckListFragment {
            val args = Bundle()
            args.putString(Constants.ARG_CONTRACT_DETAIL, model)
            args.putString(Constants.ARG_CONTRACT_NUMBER, number)
            val fragment = AllCheckListFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_all_check_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.let { KeyboardUtils.setupUI(view, activity = it) }
        initView()
    }

    private fun initView() {
        showLoading()
        handleArgument()
        setUpTabLayout()
    }

    private fun handleArgument() {
        arguments?.let {
            val json = it.getString(Constants.ARG_CONTRACT_DETAIL) ?: ""
            contractModel = Gson().fromJson(json, ContractDetailModel::class.java)
            contractNumber = it.getString(Constants.ARG_CONTRACT_NUMBER) ?: ""
        }
        setTitle(TitleAndMenuModel(title = contractNumber, status = true, image = R.drawable.ic_info))
    }

    private fun setUpTabLayout() {
        mViewPagerAdapter = ViewPagerCheckListAdapter(fragmentManager)
        mViewPagerAdapter.addTabFragment(DeploymentCheckListFragment.newInstance(contractModel.ObjID.toBigDecimal().toString()), getString(R.string.deployment_check_list))
        mViewPagerAdapter.addTabFragment(MaintenanceCheckListFragment.newInstance(contractModel.ObjID.toBigDecimal().toString()), getString(R.string.maintenance_check_list))
        fragAllCheckList_viewPager.adapter = mViewPagerAdapter
        fragAllCheckList_tabLayout.setupWithViewPager(fragAllCheckList_viewPager)
    }

    fun showDialogDetailContract() {
        val dialogContract = DetailContractDialogFragment()
        dialogContract.setData(contractModel, contractNumber)
        dialogContract.show(fragmentManager, DetailContractDialogFragment::class.java.simpleName)
    }
}