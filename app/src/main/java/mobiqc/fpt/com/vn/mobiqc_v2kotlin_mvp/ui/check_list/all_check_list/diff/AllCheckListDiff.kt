package mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.check_list.all_check_list.diff

import android.support.v7.util.DiffUtil
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.data.network.model.CheckListModel

/**
 * * Created by Anh Pham on 08/08/2018.     **
 * * Copyright (c) 2018 by FPT Telecom      **
 */
class AllCheckListDiff : DiffUtil.ItemCallback<CheckListModel>() {
    override fun areItemsTheSame(oldItem: CheckListModel, newItem: CheckListModel): Boolean {
        return oldItem.ID == newItem.ID
    }

    override fun areContentsTheSame(oldItem: CheckListModel, newItem: CheckListModel): Boolean {
        return oldItem == newItem
    }

}