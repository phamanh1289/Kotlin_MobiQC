package mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.others.dialog

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.PopupWindow
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.R
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.data.network.model.SingleChoiceModel
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.others.adapter.SelectionSingleAdapter

/**
 * * Created by Anh Pham on 08/07/2018.     **
 * * Copyright (c) 2018 by FPT Telecom      **
 */
class SelectSinglePopup(val data: ArrayList<SingleChoiceModel>, val onClick: (Int) -> Unit) : PopupWindow() {

    lateinit var singleAdapter: SelectionSingleAdapter

    fun onCreateView(context: Context) {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view: View = inflater.inflate(R.layout.dialog_text_view, null)
        contentView = view
        width = LinearLayout.LayoutParams.WRAP_CONTENT
        height = LinearLayout.LayoutParams.WRAP_CONTENT
        setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        isFocusable = true
        val recyclerView = view.findViewById(R.id.dialogEditText_RvList) as RecyclerView
        singleAdapter = SelectionSingleAdapter(data, onClick)
        recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = singleAdapter
            setHasFixedSize(true)
        }
    }

    fun getSelectIndex(): Int = if (singleAdapter.indexSelect == -1) 0 else singleAdapter.indexSelect

    override fun showAsDropDown(anchor: View?) {
        width = if (anchor?.width != 0) anchor?.width!! else LinearLayout.LayoutParams.WRAP_CONTENT
        super.showAsDropDown(anchor)
    }
}