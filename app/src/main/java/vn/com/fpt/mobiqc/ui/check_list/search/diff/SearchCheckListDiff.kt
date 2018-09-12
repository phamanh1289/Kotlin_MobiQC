package vn.com.fpt.mobiqc.ui.check_list.search.diff

import android.support.v7.util.DiffUtil
import vn.com.fpt.mobiqc.data.network.model.ContractDetailModel

/**
 * * Created by Anh Pham on 08/08/2018.     **
 * * Copyright (c) 2018 by FPT Telecom      **
 */
class SearchCheckListDiff : DiffUtil.ItemCallback<ContractDetailModel>() {
    override fun areItemsTheSame(oldItem: ContractDetailModel, newItem: ContractDetailModel): Boolean {
        return oldItem.ObjID == newItem.ObjID
    }

    override fun areContentsTheSame(oldItem: ContractDetailModel, newItem: ContractDetailModel): Boolean {
        return oldItem == newItem
    }

}