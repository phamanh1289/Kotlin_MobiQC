package mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.reprot.chart_report.diff

import android.support.v4.content.ContextCompat
import android.support.v7.recyclerview.extensions.ListAdapter
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_chart_report.view.*
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.R
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.data.network.model.DetailReportModel
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.others.datacore.DataCore
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.reprot.data_report.diff.ReportDiff
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.utils.AppUtils

/**
 * * Created by Anh Pham on 08/08/2018.     **
 * * Copyright (c) 2018 by FPT Telecom      **
 */
class ChartRePortAdapter(val sum: Float) : ListAdapter<DetailReportModel, ChartRePortAdapter.ChartRePortHolder>(ReportDiff()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChartRePortHolder {
        return ChartRePortHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_chart_report, parent, false))
    }

    override fun onBindViewHolder(holder: ChartRePortHolder, position: Int) {
            holder.bindData(getItem(position))
    }

    inner class ChartRePortHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bindData(model: DetailReportModel?) {
            model?.let { item ->
                val listColor = DataCore.getListcolor(itemView.context)
                itemView.itemChartReport_tvType.text = item.title
                itemView.itemChartReport_tvContractNumber.text = item.value.toString()
                itemView.itemChartReport_tvPercent.text = itemView.context.getString(R.string.percent_value, AppUtils.getCurrentPercent(item.value.toFloat(), sum), "%")
                itemView.itemChartReport_vColor.setBackgroundColor(listColor[adapterPosition])
                itemView.itemChartReport_llRoot.setBackgroundColor(ContextCompat.getColor(itemView.context, if (adapterPosition % 2 != 0) R.color.grey_blur else R.color.white))
            }
        }
    }
}