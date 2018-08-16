package mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.check_list.all_check_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_all_check_list.*
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.R
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.data.network.model.TitleAndMenuModel
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.others.constant.Constants
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.base.BaseFragment
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.check_list.deployment_check_list.DeploymentCheckListFragment
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.check_list.maintenance_check_list.MaintenanceCheckListFragment
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.utils.AppUtils
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.utils.KeyboardUtils

/**
 * * Created by Anh Pham on 08/09/2018.     **
 * * Copyright (c) 2018 by FPT Telecom      **
 */
class AllCheckListFragment : BaseFragment() {

    private lateinit var mViewPagerAdapter: ViewPagerCheckListAdapter
    private var contractName = ""
    private var contractNumber = ""
    private var typeCheckList = 0
    private var userUpdate = ""

    companion object {
        fun newInstance(type: String, number: String, typeCheckList: Int, userUpdate: String): AllCheckListFragment {
            val args = Bundle()
            args.putString(Constants.ARG_CONTRACT, type)
            args.putString(Constants.ARG_CONTRACT_NUMBER, number)
            args.putInt(Constants.ARG_TYPE_CHECKLIST, typeCheckList)
            args.putString(Constants.ARG_UPDATE_BY, userUpdate)
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

    fun showInfo() {
        AppUtils.showDialog(fragmentManager, content = "aaa", confirmDialogInterface = null)
    }

    private fun setUpTabLayout() {
        mViewPagerAdapter = ViewPagerCheckListAdapter(fragmentManager)
        mViewPagerAdapter.addTabFragment(DeploymentCheckListFragment.newInstance(contractName, contractNumber, typeCheckList,userUpdate), getString(R.string.deployment_check_list))
        mViewPagerAdapter.addTabFragment(MaintenanceCheckListFragment.newInstance(contractName, contractNumber, typeCheckList,userUpdate), getString(R.string.maintenance_check_list))
        fragAllCheckList_viewPager.adapter = mViewPagerAdapter
        fragAllCheckList_tabLayout.setupWithViewPager(fragAllCheckList_viewPager)
    }

    private fun handleArgument() {
        arguments?.let {
            contractName = it.getString(Constants.ARG_CONTRACT) ?: ""
            contractNumber = it.getString(Constants.ARG_CONTRACT_NUMBER) ?: ""
            typeCheckList = it.getInt(Constants.ARG_TYPE_CHECKLIST)
            userUpdate = it.getString(Constants.ARG_UPDATE_BY) ?: ""
        }
        setTitle(TitleAndMenuModel(title = contractNumber, status = true, image = R.drawable.ic_info))
    }

}