package mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.contract.detail_contract

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.fragment_detail_contract.*
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.R
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.data.network.model.ContractDetailModel
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.data.network.model.PhoneNumberModel
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.data.network.model.ResponseModel
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.data.network.model.TitleAndMenuModel
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.others.constant.Constants
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.base.BaseFragment
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.utils.AppUtils
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.utils.KeyboardUtils
import java.text.NumberFormat
import java.util.*
import javax.inject.Inject
import kotlin.collections.HashMap

/**
 * * Created by Anh Pham on 08/08/2018.     **
 * * Copyright (c) 2018 by FPT Telecom      **
 */
class DetailContractFragment : BaseFragment(), DetailContract.DetailContractView {

    @Inject
    lateinit var presenter: DetailContractPresenter

    private var listParams = HashMap<String, Any>()
    private var listPhoneNumber = ArrayList<PhoneNumberModel>()
    private lateinit var contractNumber: String
    private var createDate = ""
    private var typeContract: Int = 0
    private var typeCheckList: Int = 0
    private lateinit var contractModel: ContractDetailModel

    companion object {
        fun newInstance(type: Int, checkList: Int, contractName: String, contractDate: String): DetailContractFragment {
            val args = Bundle()
            args.putInt(Constants.ARG_TYPE_CONTRACT, type)
            args.putInt(Constants.ARG_TYPE_CHECKLIST, checkList)
            args.putString(Constants.ARG_CONTRACT, contractName)
            args.putString(Constants.ARG_OBJ_CREATEDATE, contractDate)
            val fragment = DetailContractFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_detail_contract, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getActivityComponent().inject(this)
        presenter.onAttach(this)
        activity?.let { KeyboardUtils.setupUI(view, activity = it) }
        initView()
    }

    private fun initView() {
        val bundle = arguments
        bundle?.let {
            listParams = Gson().fromJson(getSharePreferences().listParams, object : TypeToken<HashMap<String, Any>>() {}.type)
            createDate = it.getString(Constants.ARG_OBJ_CREATEDATE) ?: ""
            contractNumber = it.getString(Constants.ARG_CONTRACT) ?: ""
            typeContract = it.getInt(Constants.ARG_TYPE_CONTRACT)
            typeCheckList = it.getInt(Constants.ARG_TYPE_CHECKLIST)
            handleRequestData()
        }
        setTitle(TitleAndMenuModel(title = contractNumber, status = true, image = R.drawable.ic_warning))
    }

    //Start : Call api get data
    private fun handleRequestData() {
        when (typeContract) {
            Constants.STATUS_PROCESSING -> {
                if (typeCheckList == Constants.DEPLOYMENT) presenter.getDeploymentContractDetail(listParams) else presenter.getMaintenanceContractDetail(listParams)
            }
            Constants.STATUS_COMPLETED -> {
                listParams["SearchType"] = typeCheckList
                presenter.getFinishContractDetail(listParams)
            }
            else -> presenter.getCheckListDetail(listParams)
        }
    }

    private fun requestDataDeposit(data: String) {
        contractModel.Deposits = data.toDouble()
        presenter.let {
            val map = HashMap<String, Any>()
            map["username"] = getSharePreferences().accountName
            map["contract"] = contractNumber
            it.getCoordinateContract(map)
        }
    }

    private fun requestDataCoordinate(data: String) {
        contractModel.Coordinate = data
        contractModel.CreateDate = createDate
        loadContractToView(contractModel)
        hideLoading()
    }

    private fun handleDataDeployDetail(data: ArrayList<Any>?) {
        data?.let {
            for (i in 0 until it.size) {
                when (i) {
                    0 ->
                        contractModel = Gson().fromJson(Gson().toJson(it[i]), ContractDetailModel::class.java)
                    else -> listPhoneNumber.add(Gson().fromJson(Gson().toJson(it[i]), PhoneNumberModel::class.java))
                }
            }
        }
        presenter.let {
            val map = HashMap<String, Any>()
            map["contract"] = contractNumber
            it.getDepositsContract(map)
        }
    }
    //End : Call api get data

    //Start : show UI
    private fun showDataDeployment(it: ContractDetailModel) {
        showUI(true)
        fragDetailContract_tvNewLocalType.text = it.NewLocalType
        fragDetailContract_tvLoopRemind.text = it.LoopRemind
        fragDetailContract_tvIndoor.text = getString(R.string.cable_met, it.Indoor)
        fragDetailContract_tvOutdoor.text = getString(R.string.cable_met, it.Outdoor)
        fragDetailContract_tvLanQuantity.text = getString(R.string.cable_met, it.LanQuantity)
        fragDetailContract_tvOutDoorReUsed.text = getString(R.string.cable_met, it.OutDoorReUsed)
        fragDetailContract_tvModemType.text = getString(R.string.type_amount, it.ModemType, it.ModemAmount)
        fragDetailContract_tvSTBType.text = getString(R.string.type_amount, it.STBType, it.STBAmount)
        fragDetailContract_tvIsUpgrade.text = it.IsUpgrade
        fragDetailContract_tvHDBoxAmount.text = it.HDBoxAmount
        fragDetailContract_tvImage.text = it.Image
        fragDetailContract_tvDescriptionCS.text = it.DescriptionCS
    }

    private fun showDataMaintenance(it: ContractDetailModel) {
        showUI(false)
        fragDetailContract_llDeposits.setBackgroundColor(resources.getColor(R.color.grey_blur))
        fragDetailContract_llDescription.setBackgroundColor(resources.getColor(R.color.grey_blur))
        fragDetailContract_llFinishDate.setBackgroundColor(resources.getColor(R.color.white))
        fragDetailContract_tvCableBT.text = getString(R.string.cable_met, (it.Indoor + it.Outdoor))
        fragDetailContract_tvCableTK.text = getString(R.string.cable_met, (it.IDCLength + it.ODCLength))
        fragDetailContract_tvInit_StatusD.text = it.Init_StatusD
    }

    private fun showUI(type: Boolean) {
        when (type) {
            true -> {
                fragDetailContract_llNewLocalType.visibility = View.VISIBLE
                fragDetailContract_llLoopRemind.visibility = View.VISIBLE
                fragDetailContract_llIndoor.visibility = View.VISIBLE
                fragDetailContract_llOutdoor.visibility = View.VISIBLE
                fragDetailContract_llLanQuantity.visibility = View.VISIBLE
                fragDetailContract_llOutDoorReUsed.visibility = View.VISIBLE
                fragDetailContract_llModemType.visibility = View.VISIBLE
                fragDetailContract_llSTBType.visibility = View.VISIBLE
                fragDetailContract_llIsUpgrade.visibility = View.VISIBLE
                fragDetailContract_llHDBoxAmount.visibility = View.VISIBLE
                fragDetailContract_llImage.visibility = View.VISIBLE
                fragDetailContract_llDescriptionCS.visibility = View.VISIBLE
            }
            else -> {
                fragDetailContract_llCableBT.visibility = View.VISIBLE
                fragDetailContract_llCableTK.visibility = View.VISIBLE
                fragDetailContract_llInit_StatusD.visibility = View.VISIBLE
                fragDetailContract_llInit_StatusD.visibility = View.VISIBLE
            }
        }
    }

    private fun loadContractToView(model: ContractDetailModel?) {
        model?.let {
            fragDetailContract_tvObjID.text = it.ObjID.toBigDecimalOrNull().toString()
            fragDetailContract_tvName.text = it.Name
            fragDetailContract_tvLocalType.text = it.LocalType
            fragDetailContract_tvFullName.text = it.FullName
            fragDetailContract_tvAddress.text = if (it.Address.isNullOrEmpty()) it.Support_Location else it.Address
            fragDetailContract_tvCoordinate.text = it.Coordinate
            fragDetailContract_tvPhone.text = it.Phone
            fragDetailContract_tvCreateDate.text = it.CreateDate
            fragDetailContract_tvODCCableType.text = it.ODCCableType
            fragDetailContract_tvDeposits.text = getString(R.string.deposit_amount, NumberFormat.getNumberInstance(Locale.US).format(it.Deposits.toLong()))
            fragDetailContract_tvDescription.text = it.Description
            when (typeCheckList) {
                Constants.DEPLOYMENT -> showDataDeployment(it)
                Constants.MAINTENANCE -> showDataMaintenance(it)
            }
            fragDetailContract_llFinishDate.visibility = if (typeContract == Constants.STATUS_COMPLETED) View.VISIBLE else View.GONE
            if (typeContract == Constants.STATUS_COMPLETED)
                fragDetailContract_tvFinishDate.text = AppUtils.toConvertTimeToString(context, it.FinishDate)
            fragDetailContract_scMain.visibility = View.VISIBLE
            onClickAction()
        }
    }
    //End : show UI

    //Start : onclick
    private fun onClickAction() {
        typeContract = 1
        fragDetailContract_tvObjID.setOnClickListener { onClickContractNumber() }
        fragDetailContract_tvAddress.setOnClickListener { onClickAddress() }
        fragDetailContract_tvPhone.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("tel:" + fragDetailContract_tvPhone.text)))
        }
        fragDetailContract_tvAllPhone.setOnClickListener { onClickAllPhone() }
        fragDetailContract_tvODCCableType.setOnClickListener { onClickODCCableType() }
        fragDetailContract_tvCoordinate.setOnClickListener { onClickLocation() }
//        actMain_ivNotification.setOnClickListener { onClickUpdateError() }
    }

    private fun onClickContractNumber() {
        hideLoading()

    }

    private fun onClickAddress() {
        hideLoading()

    }

    private fun onClickLocation() {
        hideLoading()

    }

    private fun onClickAllPhone() {
        hideLoading()

    }

    private fun onClickODCCableType() {
        presenter.let {
            val map = HashMap<String,Any>()
            map["NameTD"] = contractModel.GroupPoint
            map["Type"] = typeContract
            presenter.getPortViewInfoCollection(map)
        }
    }

    private fun onClickUpdateError() {
        hideLoading()

    }


    //End : onclick

    //Start handle data from server
    override fun loadDeploymentContractDetail(response: ResponseModel) {
        handleDataDeployDetail(Gson().fromJson(response.Data.toString(), object : TypeToken<ArrayList<Any>>() {}.type))
    }

    override fun loadMaintenanceContractDetail(response: ResponseModel) {
        handleDataDeployDetail(Gson().fromJson(response.Data.toString(), object : TypeToken<ArrayList<Any>>() {}.type))
    }

    override fun loadDepositsContract(response: ResponseModel) {
        requestDataDeposit(response.Data.toString())
    }

    override fun loadCoordinateContract(response: ResponseModel) {
        requestDataCoordinate(response.Data.toString())
    }

    override fun loadFinishContractDetail(response: ResponseModel) {
        handleDataDeployDetail(Gson().fromJson(response.Data.toString(), object : TypeToken<ArrayList<Any>>() {}.type))
    }

    override fun loadCheckListDetail(response: ResponseModel) {
        hideLoading()
    }

    override fun loadPortViewInfoCollection(response: ResponseModel) {
        hideLoading()
    }

    override fun handleError(error: String) {
        typeContract++
        hideLoading()
        AppUtils.showDialog(fragmentManager, content = error, confirmDialogInterface = null)
    }
    //End handle data from server
}