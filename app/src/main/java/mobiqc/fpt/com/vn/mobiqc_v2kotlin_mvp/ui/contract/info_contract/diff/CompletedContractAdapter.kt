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
                itemView.itemCompletedContract_tvAssignDate.text = handleAssignDate(item.AssignDate, item.AppointmentDate, true)
                itemView.itemCompletedContract_tvDate.text = AppUtils.toConvertTimeToString(itemView.context, item.Date)
                itemView.itemCompletedContract_tvLocation.text = handleAssignDate(item.Location, item.Support_Location, false)
                itemView.itemCompletedContract_llRootView.setBackgroundColor(if (adapterPosition % 2 != 0) itemView.context.resources.getColor(R.color.white) else itemView.context.resources.getColor(R.color.grey_blur))
                itemView.setOnClickListener { onClick(adapterPosition) }
            }
        }

        private fun handleAssignDate(s1: String?, s2: String?, typeCheck: Boolean): String {
            return when {
                s1.isNullOrEmpty() -> if (typeCheck) AppUtils.toConvertTimeToString(itemView.context, s2!!) else s2!!
                s2.isNullOrEmpty() -> if (typeCheck) AppUtils.toConvertTimeToString(itemView.context, s1!!) else s1!!
                else -> "N/A"
            }
        }
    }
}