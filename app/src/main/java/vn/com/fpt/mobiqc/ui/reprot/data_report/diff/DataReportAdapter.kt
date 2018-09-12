package vn.com.fpt.mobiqc.ui.reprot.data_report.diff

import android.support.v4.content.ContextCompat
import android.support.v7.recyclerview.extensions.ListAdapter
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_data_report.view.*
import vn.com.fpt.mobiqc.R
import vn.com.fpt.mobiqc.data.network.model.DetailReportModel

/**
 * * Created by Anh Pham on 08/08/2018.     **
 * * Copyright (c) 2018 by FPT Telecom      **
 */
class DataReportAdapter(val onClick: (Int) -> Unit) : ListAdapter<DetailReportModel, DataReportAdapter.DataReportHolder>(ReportDiff()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataReportHolder {
        return DataReportHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_data_report, parent, false))
    }

    override fun onBindViewHolder(holder: DataReportHolder, position: Int) {
        holder.bindData(getItem(position), onClick)
    }

    inner class DataReportHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bindData(model: DetailReportModel?, onClick: (Int) -> Unit) {
            model?.let { item ->
                itemView.itemDataReport_tvTitle.text = item.title
                itemView.itemDataReport_tvValue.text = item.value.toString()
                itemView.itemDataReport_llRoot.setBackgroundColor(ContextCompat.getColor(itemView.context, if (adapterPosition % 2 != 0) R.color.grey_blur else R.color.white))
                itemView.setOnClickListener { onClick(adapterPosition) }
            }
        }
    }
}