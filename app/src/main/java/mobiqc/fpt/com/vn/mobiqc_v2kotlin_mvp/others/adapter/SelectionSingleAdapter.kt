package mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.others.adapter

import android.support.v7.recyclerview.extensions.ListAdapter
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_selection_single.view.*
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.R
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.data.network.model.SingleChoiceModel

/**
 * * Created by Anh Pham on 08/07/2018.     **
 * * Copyright (c) 2018 by FPT Telecom      **
 */
class SelectionSingleAdapter(val onClick: (Int) -> Unit) : ListAdapter<SingleChoiceModel, SelectionSingleAdapter.SelectionSingleHolder>(SelectionSingleDiff()) {

    var indexSelect: Int = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectionSingleAdapter.SelectionSingleHolder {
        return SelectionSingleHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_selection_single, parent, false))
    }

    override fun onBindViewHolder(holder: SelectionSingleHolder, position: Int) {
        holder.bindData(getItem(position), onClick)
    }

    inner class SelectionSingleHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bindData(item: SingleChoiceModel, onClick: (Int) -> Unit) {
            itemView.itemSelection_tvTitle.text = item.account.trim()
            itemView.itemSelection_rbCheck.isChecked = item.status
            if (indexSelect == -1 && item.status)
                indexSelect = adapterPosition
            itemView.setOnClickListener {
                onClick(adapterPosition)
                indexSelect = adapterPosition
            }
        }
    }
}