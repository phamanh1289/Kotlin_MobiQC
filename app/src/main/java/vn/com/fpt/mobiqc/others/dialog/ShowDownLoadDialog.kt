package vn.com.fpt.mobiqc.others.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import kotlinx.android.synthetic.main.fragment_dialog_show_download.*
import vn.com.fpt.mobiqc.R
import vn.com.fpt.mobiqc.utils.AppUtils

/**
 * * Created by Anh Pham on 08/02/2018.     **
 * * Copyright (c) 2018 by FPT Telecom      **
 */
class ShowDownLoadDialog : DialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.setCanceledOnTouchOutside(false)
        isCancelable = false
        return inflater.inflate(R.layout.fragment_dialog_show_download, container, false)
    }

    fun setPercentDownload(per: Float) {
        val current = per.toInt()
        when (current) {
            100 -> dismiss()
            else -> {
                fragDialogShowDownload_tvDetail.text = getString(R.string.wait_load_data, per.toInt(), "%")
                fragDialogShowDownload_tvPercent.progress = per.toInt()
            }
        }
    }

    fun setPercentUpload(per: Int, total: Int) {
        fragDialogShowDownload_tvDetail.text = getString(R.string.wait_up_data, per, total)
        fragDialogShowDownload_tvPercent.progress = (100 * per / total)
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(AppUtils.getScreenWidth() - resources.getDimension(R.dimen._40dp).toInt(), ViewGroup.LayoutParams.WRAP_CONTENT)
    }
}