package vn.com.fpt.mobiqc.others.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import kotlinx.android.synthetic.main.fragment_dialog_group_point.*
import vn.com.fpt.mobiqc.R
import vn.com.fpt.mobiqc.data.network.model.GroupPointModel
import vn.com.fpt.mobiqc.utils.AppUtils

/**
 * * Created by Anh Pham on 08/02/2018.     **
 * * Copyright (c) 2018 by FPT Telecom      **
 */
class GroupPointDialog : DialogFragment() {

    private lateinit var name: String
    private lateinit var street: String
    private var quantity: Int = 0
    private var pop: Int = 0
    private var used: Int = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return inflater.inflate(R.layout.fragment_dialog_group_point, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadDataToUI()
    }

    fun setData(item: GroupPointModel) {
        this.name = item.TDName
        this.street = item.Street
        this.quantity = item.Capacity
        this.used = item.Portuse
        this.pop = item.LengthPop
    }

    private fun loadDataToUI() {
        fragDialogGroupPoint_tvGroupQuantity.text = quantity.toString()
        fragDialogGroupPoint_tvGroupName.text = name
        fragDialogGroupPoint_tvGroupPop.text = pop.toString()
        fragDialogGroupPoint_tvGroupAddress.text = street
        fragDialogGroupPoint_tvGroupUse.text = used.toString()
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(AppUtils.getScreenWidth() - resources.getDimension(R.dimen._40dp).toInt(), ViewGroup.LayoutParams.WRAP_CONTENT)
    }
}