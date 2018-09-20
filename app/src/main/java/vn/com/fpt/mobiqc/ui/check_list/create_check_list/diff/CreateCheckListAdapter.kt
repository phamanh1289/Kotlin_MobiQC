package vn.com.fpt.mobiqc.ui.check_list.create_check_list.diff

import android.support.v4.content.ContextCompat
import android.support.v7.recyclerview.extensions.ListAdapter
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_time_zone.view.*
import vn.com.fpt.mobiqc.R
import vn.com.fpt.mobiqc.data.network.model.PartnerTimeZoneModel
import vn.com.fpt.mobiqc.others.constant.Constants

/**
 * * Created by Anh Pham on 08/08/2018.     **
 * * Copyright (c) 2018 by FPT Telecom      **
 */
class CreateCheckListAdapter(val onClick: (Int) -> Unit) : ListAdapter<PartnerTimeZoneModel, CreateCheckListAdapter.CreateCheckListHolder>(CreateCheckListDiff()) {

    var indexSelect = -1
    var reasonId = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CreateCheckListAdapter.CreateCheckListHolder {
        return CreateCheckListHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_time_zone, parent, false))
    }

    override fun onBindViewHolder(holder: CreateCheckListAdapter.CreateCheckListHolder, position: Int) {
        holder.itemView.itemTimeZone_imgChecked.isSelected = false
        holder.bindData(getItem(position), onClick)
    }

    inner class CreateCheckListHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bindData(model: PartnerTimeZoneModel?, onClick: (Int) -> Unit) {
            model?.let { item ->
                itemView.itemTimeZone_tvTimeZone.text = item.Timezone
                itemView.itemTimeZone_tvContractNumber.text = item.TimeCount.toString()
                itemView.itemTimeZone_tvAbility.text = item.TimezoneAbility.toString()
                item.status.run {
                    itemView.itemTimeZone_tvTimeZone.isSelected = this
                    itemView.itemTimeZone_tvContractNumber.isSelected = this
                    itemView.itemTimeZone_tvAbility.isSelected = this
                    itemView.itemTimeZone_imgChecked.isSelected = this
                }
                val resultTimeZone = reasonId == Constants.TYPE_NO_CONNECT && (item.TimezoneCode18 == Constants.TIME_ZONE_19 || item.TimezoneCode18 == Constants.TIME_ZONE_21)
                when {
                    item.TimezoneCode == Constants.DONT_BOOK_DATE || resultTimeZone -> itemView.itemTimeZone_llRootView.setBackgroundColor(ContextCompat.getColor(itemView.context, R.color.grey_blur))
                    else -> itemView.setOnClickListener {
                        onClick(adapterPosition)
                        indexSelect = adapterPosition
                    }
                }
            }
        }
    }
}