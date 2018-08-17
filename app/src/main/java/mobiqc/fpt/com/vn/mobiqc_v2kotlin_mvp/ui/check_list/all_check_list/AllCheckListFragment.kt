package mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.check_list.all_check_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.fragment_all_check_list.*
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.R
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.data.interfaces.DialogContractDetailInterface
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.data.network.model.*
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.others.constant.Constants
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.others.dialog.GroupPointDialog
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.base.BaseFragment
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.check_list.deployment_check_list.DeploymentCheckListFragment
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.check_list.maintenance_check_list.MaintenanceCheckListFragment
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.contract.dialog_detail_contract.ContractDetailDialog
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.utils.AppUtils
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.utils.KeyboardUtils
import java.util.ArrayList
import javax.inject.Inject
import kotlin.collections.HashMap
import kotlin.collections.set

/**
 * * Created by Anh Pham on 08/09/2018.     **
 * * Copyright (c) 2018 by FPT Telecom      **
 */
class AllCheckListFragment : BaseFragment(), AllCheckListContract.AllCheckListView {

    @Inject
    lateinit var presenter: AllCheckListPresenter
    private lateinit var mViewPagerAdapter: ViewPagerCheckListAdapter
    private lateinit var contractModel: ContractDetailModel
    private var contractNumber = ""
    private var typeCheckList = 0
    private var typeGroupPoint = 0
    private lateinit var dialogInterface: DialogContractDetailInterface

    companion object {
        fun newInstance(type: String, number: String, typeCheckList: Int): AllCheckListFragment {
            val args = Bundle()
            args.putString(Constants.ARG_CONTRACT, type)
            args.putString(Constants.ARG_CONTRACT_NUMBER, number)
            args.putInt(Constants.ARG_TYPE_CHECKLIST, typeCheckList)
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
        getActivityComponent().inject(this)
        presenter.onAttach(this)
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
            val json = it.getString(Constants.ARG_CONTRACT) ?: ""
            contractModel = Gson().fromJson(json, ContractDetailModel::class.java)
            contractNumber = it.getString(Constants.ARG_CONTRACT_NUMBER) ?: ""
            typeCheckList = it.getInt(Constants.ARG_TYPE_CHECKLIST)
        }
        setTitle(TitleAndMenuModel(title = contractNumber, status = true, image = R.drawable.ic_info))
    }

    private fun setUpTabLayout() {
        mViewPagerAdapter = ViewPagerCheckListAdapter(fragmentManager)
        mViewPagerAdapter.addTabFragment(DeploymentCheckListFragment.newInstance(contractModel.ObjID.toBigDecimal().toString(), typeCheckList), getString(R.string.deployment_check_list))
        mViewPagerAdapter.addTabFragment(MaintenanceCheckListFragment.newInstance(contractModel.ObjID.toBigDecimal().toString(), typeCheckList), getString(R.string.maintenance_check_list))
        fragAllCheckList_viewPager.adapter = mViewPagerAdapter
        fragAllCheckList_tabLayout.setupWithViewPager(fragAllCheckList_viewPager)
    }

    fun requestDetailContract() {
        presenter.let {
            showLoading()
            val map = HashMap<String, Any>()
            map[Constants.PARAMS_CONTRACT] = contractNumber
            it.getDepositsContract(map)
        }
    }

    private fun showDialogDetailContract() {
        val dialogContract = ContractDetailDialog()
        initInterface()
        dialogContract.setData(contractModel, contractNumber, dialogInterface)
        dialogContract.show(fragmentManager, ContractDetailDialog::class.java.simpleName)
    }

    private fun initInterface() {
        dialogInterface = object : DialogContractDetailInterface {
            override fun onClickAddress() {
                requestAddress()
            }

            override fun onClickPhone() {
                AppUtils.makeCallPhoneNumber(context, contractModel.Phone)
            }

            override fun onClickAllPhone() {
                requestAllPhone()
            }

            override fun onClickGroupPoint() {
                requestODCCableType(Constants.TYPE_ADSL)
            }

            override fun onClickLocation() {
                requestLocation()
            }
        }
    }

    private fun requestLocation() {

    }

    private fun requestAddress() {
        AppUtils.showAddressToMap(context, contractModel.Address)
    }

    private fun requestAllPhone() {
        presenter.let {
            showLoading()
            val map = HashMap<String, Any>()
            map[Constants.PARAMS_OBJID] = contractModel.ObjID
            it.getAllPhoneNumber(map)
        }
    }

    private fun requestODCCableType(type: Int) {
        typeGroupPoint = type
        presenter.let {
            val map = HashMap<String, Any>()
            map[Constants.PARAMS_NAME_TD] = contractModel.GroupPoint
            map[Constants.PARAMS_TYPE] = typeGroupPoint
            it.getPortViewInfoCollection(map)
        }
    }

    private fun showPopUpGroupPoint(list: ArrayList<GroupPointModel>) {
        if (list.size != 0) {
            val dialog = GroupPointDialog()
            dialog.setData(list[0])
            dialog.show(fragmentManager, GroupPointDialog::class.java.simpleName)
        }
        hideLoading()
    }

    private fun showPopUpAllPhone(list: ArrayList<PhoneNumberModel>) {
        AppUtils.showDialogPhoneNumber(fragmentManager, contractNumber, list)
        hideLoading()
    }

    private fun requestDataDeposit(data: String) {
        contractModel.Deposits = data.toDouble()
        presenter.let {
            val map = HashMap<String, Any>()
            map[Constants.PARAMS_USER_NAME_LOW] = getSharePreferences().accountName
            map[Constants.PARAMS_CONTRACT] = contractNumber
            it.getCoordinateContract(map)
        }
    }

    private fun requestDataCoordinate(data: String) {
        contractModel.Coordinate = data
        hideLoading()
        showDialogDetailContract()
    }

    override fun loadDepositsContract(response: ResponseModel) {
        requestDataDeposit(response.Data.toString())
    }

    override fun loadCoordinateContract(response: ResponseModel) {
        requestDataCoordinate(response.Data.toString())
    }

    override fun loadPortViewInfoCollection(response: ResponseModel) {
        if (response.Data.toString().replace("[", "").replace("]", "").isBlank())
            when (typeGroupPoint) {
                Constants.TYPE_ADSL -> requestODCCableType(Constants.TYPE_FTTH)
                Constants.TYPE_FTTH -> requestODCCableType(Constants.TYPE_PON)
            }
        else
            showPopUpGroupPoint(Gson().fromJson(response.Data.toString(), object : TypeToken<ArrayList<GroupPointModel>>() {}.type))
    }

    override fun loadAllPhoneNumber(response: ResponseModel) {
        showPopUpAllPhone(Gson().fromJson(response.Data.toString(), object : TypeToken<ArrayList<PhoneNumberModel>>() {}.type))
    }

    override fun handleError(error: String) {
        hideLoading()
        AppUtils.showDialog(fragmentManager, content = error, confirmDialogInterface = null)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.onDetach()
    }
}