package mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.check_list.create_check_list.diff

import android.support.v7.util.DiffUtil
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.data.network.model.PartnerTimeZoneModel

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