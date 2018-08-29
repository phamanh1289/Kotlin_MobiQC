package mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.reprot.chart_report.diff

import android.support.v7.recyclerview.extensions.ListAdapter
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.R
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.data.network.model.DetailReportModel
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.reprot.data_report.diff.ReportDiff

/**
 * * Created by Anh Pham on 08/08/2018.     **
 * * Copyright (c) 2018 by FPT Telecom      **
 */
class ChartReprotAdapter(val onClick: (Int) -> Unit) : ListAdapter<DetailReportModel, ChartReprotAdapter.ChartReprotHolder>(ReportDiff()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChartReprotHolder {
        return ChartReprotHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_list_error, parent, false))
    }

    override fun onBindViewHolder(holder: ChartReprotHolder, position: Int) {
        holder.bindData(getItem(position), onClick)
    }

    inner class ChartReprotHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bindData(model: DetailReportModel?, onClick: (Int) -> Unit) {
            model?.let { item ->
//                itemView.itemListError_tvID.text = item.ID.toString()
                itemView.setOnClickListener { onClick(adapterPosition) }
            }
        }
    }
}