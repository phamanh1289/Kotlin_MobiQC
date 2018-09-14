package vn.com.fpt.mobiqc.others.adapter

import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_menu_main.view.*
import vn.com.fpt.mobiqc.R
import vn.com.fpt.mobiqc.data.network.model.ItemMenuModel
import vn.com.fpt.mobiqc.others.constant.Constants

/**
 * * Created by Anh Pham on 08/07/2018.     **
 * * Copyright (c) 2018 by FPT Telecom      **
 */
class ItemMenuAdapter(var mData: ArrayList<ItemMenuModel>?, val onClick: (Int) -> Unit) : RecyclerView.Adapter<ItemMenuAdapter.ItemMenuHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemMenuHolder {
        return ItemMenuHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_menu_main, parent, false))
    }

    override fun getItemCount(): Int {
        return mData?.size ?: 0
    }

    override fun onBindViewHolder(holder: ItemMenuHolder, position: Int) {
        mData?.let { holder.bind(it[position], onClick) }
    }

    inner class ItemMenuHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(item: ItemMenuModel, onListener: (Int) -> Unit) {
            itemView.itemMenuMain.text = item.name
            if (item.id.isBlank() || item.id == Constants.DANG_XUAT) {
                itemView.itemMenuMain.setTextColor(ContextCompat.getColor(itemView.context, R.color.colorPrimary))
                itemView.itemMenu_rootView.setBackgroundColor(ContextCompat.getColor(itemView.context, R.color.grey_blur))
            }
            itemView.setOnClickListener { onListener(adapterPosition) }
        }
    }
}