package mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.contract.detail_contract.diff

import android.support.v7.recyclerview.extensions.ListAdapter
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_phone_number.view.*
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.R
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.data.network.model.PhoneNumberModel
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.others.constant.Constants
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.utils.AppUtils

/**
 * * Created by Anh Pham on 08/08/2018.     **
 * * Copyright (c) 2018 by FPT Telecom      **
 */
class PhoneNumberAdapter(val onClick: (Int) -> Unit) : ListAdapter<PhoneNumberModel, PhoneNumberAdapter.PhoneNumberHolder>(PhoneNumberDiff()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhoneNumberHolder {
        return PhoneNumberHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_phone_number, parent, false))
    }

    override fun onBindViewHolder(holder: PhoneNumberHolder, position: Int) {
        holder.bindData(getItem(position), onClick)
    }

    inner class PhoneNumberHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bindData(model: PhoneNumberModel?, onClick: (Int) -> Unit) {
            model?.let { item ->
                itemView.itemPhoneNumber_tvNumber.text = item.Phone
                itemView.itemPhoneNumber_tvDateUpdate.text = AppUtils.toConvertTimeToString(itemView.context, item.Date)
                itemView.itemPhoneNumber_rbSms.isChecked = item.Sms == Constants.SMS_TRUE
                itemView.setOnClickListener { onClick(adapterPosition) }
            }
        }
    }
}