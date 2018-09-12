package vn.com.fpt.mobiqc.ui.check_list.all_check_list.diff

import android.support.v7.util.DiffUtil
import vn.com.fpt.mobiqc.data.network.model.CheckListModel

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