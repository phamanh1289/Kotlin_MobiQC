package mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.check_list.all_check_list.diff

import android.support.v7.recyclerview.extensions.ListAdapter
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_check_list.view.*
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.R
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.data.network.model.CheckListModel
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.utils.AppUtils

/**
 * * Created by Anh Pham on 08/08/2018.     **
 * * Copyright (c) 2018 by FPT Telecom      **
 */
class AllCheckListAdapter(val onClick: (Int) -> Unit) : ListAdapter<CheckListModel, AllCheckListAdapter.DeploymentCheckList>(AllCheckListDiff()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeploymentCheckList {
        return DeploymentCheckList(LayoutInflater.from(parent.context).inflate(R.layout.item_check_list, parent, false))
    }

    override fun onBindViewHolder(holder: DeploymentCheckList, position: Int) {
        holder.bindData(getItem(position), onClick)
    }

    inner class DeploymentCheckList(view: View) : RecyclerView.ViewHolder(view) {
        fun bindData(model: CheckListModel?, onClick: (Int) -> Unit) {
            model?.let { item ->
                itemView.itemCheckList_tvID.text = item.ID.toString()
                itemView.itemCheckList_tvCreateUpdate.text = AppUtils.toConvertTimeToString(itemView.context, item.Date)
                itemView.itemCheckList_tvLastUpdate.text = AppUtils.toConvertTimeToString(itemView.context, item.LastUpdate)
                itemView.itemCheckList_tvStatus.text = AppUtils.handleAssignDate(itemView.context, item.StatusCL, item.Status, false)
                itemView.itemCheckList_tvType.text = AppUtils.handleAssignDate(itemView.context, item.CusType, item.RequestFrom, false)
                itemView.itemCheckList_tvUserUpdate.text = item.UpdateBy
                itemView.setOnClickListener {
                    onClick(adapterPosition)
                }
            }
        }
    }
}