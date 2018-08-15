package mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.contract.info_contract.diff

import android.support.v7.recyclerview.extensions.ListAdapter
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_completed_contract.view.*
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.R
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.data.network.model.CompletedContractModel
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.utils.AppUtils

/**
 * * Created by Anh Pham on 08/08/2018.     **
 * * Copyright (c) 2018 by FPT Telecom      **
 */
class CompletedContractAdapter(val onClick: (Int) -> Unit) : ListAdapter<CompletedContractModel, CompletedContractAdapter.CompletedContractHolder>(CompletedDiff()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CompletedContractHolder {
        return CompletedContractHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_completed_contract, parent, false))
    }

    override fun onBindViewHolder(holder: CompletedContractHolder, position: Int) {
        holder.bindData(getItem(position), onClick)
    }

    inner class CompletedContractHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bindData(model: CompletedContractModel?, onClick: (Int) -> Unit) {
            model?.let { item ->
                itemView.itemCompletedContract_tvContract.text = item.Contract
                itemView.itemCompletedContract_tvName.text = item.FullName
                itemView.itemCompletedContract_tvAssignDate.text = AppUtils.handleAssignDate(itemView.context, item.AssignDate, item.AppointmentDate, true)
                itemView.itemCompletedContract_tvDate.text = AppUtils.toConvertTimeToString(itemView.context, item.Date)
                itemView.itemCompletedContract_tvLocation.text = AppUtils.handleAssignDate(itemView.context, item.Location, item.Support_Location, false)
                itemView.setOnClickListener { onClick(adapterPosition) }
            }
        }
    }
}