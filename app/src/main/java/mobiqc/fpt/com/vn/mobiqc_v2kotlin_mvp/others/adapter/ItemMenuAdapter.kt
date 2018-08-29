package mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.others.adapter

import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_menu_main.view.*
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.R
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.data.network.model.ItemMenuModel

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
            if (item.id.isBlank())
                itemView.itemMenuMain.setTextColor(ContextCompat.getColor(itemView.context, R.color.colorPrimary))
            itemView.setOnClickListener { onListener(adapterPosition) }
        }
    }
}