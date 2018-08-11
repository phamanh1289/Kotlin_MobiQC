package mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.contract.info_contract.diff

import android.support.v7.util.DiffUtil
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.data.network.model.CompletedContractModel

/**
 * * Created by Anh Pham on 08/08/2018.     **
 * * Copyright (c) 2018 by FPT Telecom      **
 */
class CompletedDiff : DiffUtil.ItemCallback<CompletedContractModel>() {
    override fun areItemsTheSame(oldItem: CompletedContractModel, newItem: CompletedContractModel): Boolean {
        return oldItem.ObjID == newItem.ObjID
    }

    override fun areContentsTheSame(oldItem: CompletedContractModel, newItem: CompletedContractModel): Boolean {
        return oldItem == newItem
    }

}