package mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.error.list.diff

import android.support.v7.util.DiffUtil
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.data.network.model.ErrorInfrastructureModel

/**
 * * Created by Anh Pham on 08/08/2018.     **
 * * Copyright (c) 2018 by FPT Telecom      **
 */
class ListErrorDiff : DiffUtil.ItemCallback<ErrorInfrastructureModel>() {
    override fun areItemsTheSame(oldItem: ErrorInfrastructureModel, newItem: ErrorInfrastructureModel): Boolean {
        return oldItem.ID == newItem.ID
    }

    override fun areContentsTheSame(oldItem: ErrorInfrastructureModel, newItem: ErrorInfrastructureModel): Boolean {
        return oldItem == newItem
    }

}