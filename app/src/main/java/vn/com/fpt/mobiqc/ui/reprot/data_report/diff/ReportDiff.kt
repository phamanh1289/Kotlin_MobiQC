package vn.com.fpt.mobiqc.ui.reprot.data_report.diff

import android.support.v7.util.DiffUtil
import vn.com.fpt.mobiqc.data.network.model.DetailReportModel

/**
 * * Created by Anh Pham on 08/08/2018.     **
 * * Copyright (c) 2018 by FPT Telecom      **
 */
class ReportDiff : DiffUtil.ItemCallback<DetailReportModel>() {
    override fun areItemsTheSame(oldItem: DetailReportModel, newItem: DetailReportModel): Boolean {
        return oldItem.title == newItem.title
    }

    override fun areContentsTheSame(oldItem: DetailReportModel, newItem: DetailReportModel): Boolean {
        return oldItem == newItem
    }

}