package mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.contract.dialog_detail_contract

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import kotlinx.android.synthetic.main.fragment_dialog_contract_detail.*
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.R
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.data.interfaces.DialogContractDetailInterface
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.data.network.model.ContractDetailModel
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.utils.AppUtils

/**
 * * Created by Anh Pham on 08/02/2018.     **
 * * Copyright (c) 2018 by FPT Telecom      **
 */
class ContractDetailDialog : DialogFragment() {

    private lateinit var contractModel: ContractDetailModel
    private var contractNumber = ""
    private lateinit var dialogInterface: DialogContractDetailInterface

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return inflater.inflate(R.layout.fragment_dialog_contract_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadContractToView()
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
        }
        initOnClick()
    }

    private fun initOnClick() {
        fragDialogDetailContract_tvAddress.setOnClickListener { dialogInterface.onClickAddress() }
        fragDialogDetailContract_tvCoordinate.setOnClickListener { dialogInterface.onClickLocation() }
        fragDialogDetailContract_tvPhone.setOnClickListener { dialogInterface.onClickPhone() }
        fragDialogDetailContract_tvAllPhone.setOnClickListener { dialogInterface.onClickAllPhone() }
        fragDialogDetailContract_tvODCCableType.setOnClickListener { dialogInterface.onClickGroupPoint() }
    }

    fun setData(contractModel: ContractDetailModel, contractNumber: String, dialogInterface: DialogContractDetailInterface) {
        this.contractModel = contractModel
        this.contractNumber = contractNumber
        this.dialogInterface = dialogInterface
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(AppUtils.getScreenWidth() - resources.getDimension(R.dimen._40dp).toInt(), ViewGroup.LayoutParams.WRAP_CONTENT)
    }

}