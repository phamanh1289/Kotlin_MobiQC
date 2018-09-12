package vn.com.fpt.mobiqc.ui.contract.detail_contract.diff

import android.support.v7.util.DiffUtil
import vn.com.fpt.mobiqc.data.network.model.PhoneNumberModel

/**
 * * Created by Anh Pham on 08/08/2018.     **
 * * Copyright (c) 2018 by FPT Telecom      **
 */
class PhoneNumberDiff : DiffUtil.ItemCallback<PhoneNumberModel>() {
    override fun areItemsTheSame(oldItem: PhoneNumberModel, newItem: PhoneNumberModel): Boolean {
        return oldItem.Phone == newItem.Phone
    }

    override fun areContentsTheSame(oldItem: PhoneNumberModel, newItem: PhoneNumberModel): Boolean {
        return oldItem == newItem
    }

}