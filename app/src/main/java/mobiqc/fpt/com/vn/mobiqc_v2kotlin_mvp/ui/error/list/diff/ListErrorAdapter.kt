package mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.error.list.diff

import android.support.v7.recyclerview.extensions.ListAdapter
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_list_error.view.*
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.R
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.data.network.model.ErrorInfrastructureModel
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.others.constant.Constants

/**
 * * Created by Anh Pham on 08/08/2018.     **
 * * Copyright (c) 2018 by FPT Telecom      **
 */
class ListErrorAdapter(val onClick: (Int) -> Unit) : ListAdapter<ErrorInfrastructureModel, ListErrorAdapter.ListErrorHolder>(ListErrorDiff()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListErrorHolder {
        return ListErrorHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_list_error, parent, false))
    }

    override fun onBindViewHolder(holder: ListErrorHolder, position: Int) {
        holder.bindData(getItem(position), onClick)
    }

    inner class ListErrorHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bindData(model: ErrorInfrastructureModel?, onClick: (Int) -> Unit) {
            model?.let { item ->
                itemView.itemListError_tvID.text = item.ID.toString()
                itemView.itemListError_tvZone.text = item.Area
                itemView.itemListError_tvBranch.text = item.Branch
                itemView.itemListError_tvType.text = item.Type
                itemView.itemListError_tvElement.text = item.Element
                itemView.itemListError_tvDescription.text = item.Description
                itemView.itemListError_tvPartner.text = item.Partner
                itemView.itemListError_tvNote.text = item.Note
                itemView.itemListError_tvMail.text = itemView.context.getString(if (item.MailTo.isNullOrBlank()) R.string.mes_mail_no_send else R.string.mes_mail_send)
                itemView.itemListError_tvStatus.text = itemView.context.getString(if (item.Status == Constants.REQUEST_SUCCESS) R.string.status_done else R.string.status_no_done)
                itemView.setOnClickListener { onClick(adapterPosition) }
            }
        }
    }
}