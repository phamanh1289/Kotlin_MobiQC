package mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.check_list.search.diff

import android.support.v7.recyclerview.extensions.ListAdapter
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_search_check_list.view.*
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.R
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.data.network.model.ContractDetailModel

/**
 * * Created by Anh Pham on 08/08/2018.     **
 * * Copyright (c) 2018 by FPT Telecom      **
 */
class SearchCheckListAdapter(val onClick: (Int) -> Unit) : ListAdapter<ContractDetailModel, SearchCheckListAdapter.SearchCheckListHolder>(SearchCheckListDiff()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchCheckListHolder {
        return SearchCheckListHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_search_check_list, parent, false))
    }

    override fun onBindViewHolder(holder: SearchCheckListHolder, position: Int) {
        holder.bindData(getItem(position), onClick)
    }

    inner class SearchCheckListHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bindData(model: ContractDetailModel?, onClick: (Int) -> Unit) {
            model?.let { item ->
                itemView.itemSearchCheckList_tvContract.text = itemView.context.getString(R.string.name_check_list, item.Contract, item.Name)
                itemView.itemSearchCheckList_tvName.text = item.FullName
                itemView.itemSearchCheckList_tvPhoneNumber.text = item.Phone
                itemView.itemSearchCheckList_tvAddress.text = item.Address
                itemView.setOnClickListener { onClick(adapterPosition) }
            }
        }
    }
}