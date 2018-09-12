package vn.com.fpt.mobiqc.ui.check_list.create_check_list.diff

import android.support.v7.util.DiffUtil
import vn.com.fpt.mobiqc.data.network.model.PartnerTimeZoneModel

/**
 * * Created by Anh Pham on 08/08/2018.     **
 * * Copyright (c) 2018 by FPT Telecom      **
 */
class CreateCheckListDiff : DiffUtil.ItemCallback<PartnerTimeZoneModel>() {
    override fun areItemsTheSame(oldItem: PartnerTimeZoneModel, newItem: PartnerTimeZoneModel): Boolean {
        return oldItem.Timezone == newItem.Timezone
    }

    override fun areContentsTheSame(oldItem: PartnerTimeZoneModel, newItem: PartnerTimeZoneModel): Boolean {
        return oldItem == newItem
    }

}