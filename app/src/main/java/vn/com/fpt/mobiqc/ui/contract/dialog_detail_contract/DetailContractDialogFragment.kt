package vn.com.fpt.mobiqc.ui.contract.dialog_detail_contract

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.fragment_dialog_contract_detail.*
import vn.com.fpt.mobiqc.R
import vn.com.fpt.mobiqc.dagger.component.ActivityComponent
import vn.com.fpt.mobiqc.data.network.model.ContractDetailModel
import vn.com.fpt.mobiqc.data.network.model.GroupPointModel
import vn.com.fpt.mobiqc.data.network.model.PhoneNumberModel
import vn.com.fpt.mobiqc.data.network.model.ResponseModel
import vn.com.fpt.mobiqc.others.constant.Constants
import vn.com.fpt.mobiqc.others.dialog.GroupPointDialog
import vn.com.fpt.mobiqc.ui.base.BaseActivity
import vn.com.fpt.mobiqc.ui.base.BaseFragment
import vn.com.fpt.mobiqc.ui.main.MainActivity
import vn.com.fpt.mobiqc.ui.maps.MapsFragment
import vn.com.fpt.mobiqc.utils.AppUtils
import vn.com.fpt.mobiqc.utils.SharedPrefUtils
import java.util.ArrayList
import javax.inject.Inject
import kotlin.collections.HashMap
import kotlin.collections.set

/**
 * * Created by Anh Pham on 08/02/2018.     **
 * * Copyright (c) 2018 by FPT Telecom      **
 */
class DetailContractDialogFragment : DialogFragment(), DetailContractDialogContract.DetailContractDialogView {

    @Inject
    lateinit var presenter: DetailContractDialogPresenter

    private lateinit var contractModel: ContractDetailModel
    private var contractNumber = ""
    private var typeGroupPoint = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return inflater.inflate(R.layout.fragment_dialog_contract_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getActivityComponent().inject(this)
        presenter.onAttach(this)
        handleRequest()
    }

    fun setData(contractModel: ContractDetailModel, contractNumber: String) {
        this.contractModel = contractModel
        this.contractNumber = contractNumber
    }

    private fun handleRequest() {
        presenter.let {
            showLoading()
            val map = HashMap<String, Any>()
            map[Constants.PARAMS_CONTRACT] = contractNumber
            it.getDepositsContract(map)
        }
    }

    private fun loadContractToView() {
        contractModel.let {
            fragDialogDetailContract_tvObjID.text = contractNumber
            fragDialogDetailContract_tvFullName.text = it.FullName
            fragDialogDetailContract_tvAddress.text = if (it.Address.isNullOrEmpty()) it.Support_Location else it.Address
            fragDialogDetailContract_tvCoordinate.text = AppUtils.getLocationUser(it.Coordinate)
            fragDialogDetailContract_tvPhone.text = it.Phone
            fragDialogDetailContract_tvODCCableType.text = it.ODCCableType
            fragDialogDetailContract_tvDeposit.text = getString(R.string.deposit_amount, AppUtils.setFormatMoney(it.Deposits.toLong()))
            fragDialogDetailContract_llRoot.visibility = View.VISIBLE
        }
        initOnClick()
    }

    private fun initOnClick() {
        fragDialogDetailContract_tvAddress.setOnClickListener { requestAddress() }
        if (fragDialogDetailContract_tvCoordinate.text.isNotBlank())
            fragDialogDetailContract_tvCoordinate.setOnClickListener {
                dismiss()
                requestLocation()
            }
        fragDialogDetailContract_tvPhone.setOnClickListener { AppUtils.makeCallPhoneNumber(context, contractModel.Phone) }
        fragDialogDetailContract_tvAllPhone.setOnClickListener { requestAllPhone() }
        fragDialogDetailContract_tvODCCableType.setOnClickListener {
            showLoading()
            requestODCCableType(Constants.TYPE_ADSL)
        }
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(AppUtils.getScreenWidth() - resources.getDimension(R.dimen._40dp).toInt(), ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onDetach()
    }

    private fun requestAllPhone() {
        presenter.let {
            showLoading()
            val map = HashMap<String, Any>()
            map[Constants.PARAMS_OBJID] = contractModel.ObjID
            it.getAllPhoneNumber(map)
        }
    }

    private fun requestLocation() {
        val lat = fragDialogDetailContract_tvCoordinate.text.split(",")[0]
        val lng = fragDialogDetailContract_tvCoordinate.text.split(",")[1]
        addFragment(MapsFragment.newInstance(contractNumber, contractModel.FullName, lat, lng, contractModel.Address), true, true)
    }

    private fun requestAddress() {
            AppUtils.showAddressToMap(context, contractModel.Address)
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
        loadContractToView()
    }

    private fun requestODCCableType(type: Int) {
        typeGroupPoint = type
        presenter.let {
            val map = HashMap<String, Any>()
            map[Constants.PARAMS_NAME_TD] = if (contractModel.GroupPoint.isNullOrBlank()) contractModel.GroupPoints else contractModel.GroupPoint
            map[Constants.PARAMS_TYPE] = typeGroupPoint
            it.getPortViewInfoCollection(map)
        }
    }

    private fun showPopUpAllPhone(list: ArrayList<PhoneNumberModel>) {
        AppUtils.showDialogPhoneNumber(fragmentManager, contractNumber, list)
        hideLoading()
    }

    private fun showPopUpGroupPoint(list: ArrayList<GroupPointModel>) {
        if (list.size != 0) {
            val dialog = GroupPointDialog()
            dialog.setData(list[0])
            dialog.show(fragmentManager, GroupPointDialog::class.java.simpleName)
        }
        hideLoading()
    }

    override fun loadDepositsContract(response: ResponseModel) {
        requestDataDeposit(response.Data.toString())
    }

    override fun loadCoordinateContract(response: ResponseModel) {
        requestDataCoordinate(response.Data.toString())
    }

    override fun loadPortViewInfoCollection(response: ResponseModel) {
        if ("[]" == response.Data.toString()) {
            if (typeGroupPoint == Constants.TYPE_PON) {
                hideLoading()
                AppUtils.showDialog(fragmentManager, content = "Không lấy được thông tin tập điểm", confirmDialogInterface = null)
            } else
                when (typeGroupPoint) {
                    Constants.TYPE_ADSL -> requestODCCableType(Constants.TYPE_FTTH)
                    Constants.TYPE_FTTH -> requestODCCableType(Constants.TYPE_PON)
                }
        } else
            showPopUpGroupPoint(Gson().fromJson(response.Data.toString(), object : TypeToken<ArrayList<GroupPointModel>>() {}.type))
    }

    override fun loadAllPhoneNumber(response: ResponseModel) {
        showPopUpAllPhone(Gson().fromJson(response.Data.toString(), object : TypeToken<ArrayList<PhoneNumberModel>>() {}.type))
    }

    override fun handleError(error: String) {
        hideLoading()
        AppUtils.showDialog(fragmentManager, content = error, confirmDialogInterface = null)
    }

    override fun showLoading() {
        if (activity is BaseActivity) {
            (activity as BaseActivity).showLoading()
        }
    }

    override fun hideLoading() {
        if (activity is BaseActivity) {
            (activity as BaseActivity).hideLoading()
        }
    }

    override fun getActivityComponent(): ActivityComponent {
        return (activity as BaseActivity).getActivityComponent()
    }

    override fun getSharePreferences(): SharedPrefUtils {
        return (activity as BaseActivity).getSharePreferences()
    }

    override fun getCurrentFragment(): BaseFragment {
        return BaseFragment()
    }

    override fun isNetworkConnected(): Boolean {
        return true
    }

    override fun addFragment(fragment: BaseFragment, isAddToBackStack: Boolean, isAnimation: Boolean) {
        if (activity is BaseActivity) {
            if (activity is MainActivity) {
                val activity = activity as MainActivity
                if (isAddToBackStack) activity.mCountBack++
                activity.handleShowMenu()
            }
            (activity as BaseActivity).addFragment(fragment, isAddToBackStack, isAnimation)
        }
    }

    override fun replaceFragment(fragment: BaseFragment, isAddToBackStack: Boolean, isAnimation: Boolean) {

    }
}