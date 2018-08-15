package mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.others.adapter

import android.support.v7.util.DiffUtil
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.data.network.model.SingleChoiceModel

/**
 * * Created by Anh Pham on 08/08/2018.     **
 * * Copyright (c) 2018 by FPT Telecom      **
 */
class SelectionSingleDiff : DiffUtil.ItemCallback<SingleChoiceModel>() {
    override fun areItemsTheSame(oldItem: SingleChoiceModel, newItem: SingleChoiceModel): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: SingleChoiceModel, newItem: SingleChoiceModel): Boolean {
        return oldItem == newItem
    }

}