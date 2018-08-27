package mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.others.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import kotlinx.android.synthetic.main.fragment_dialog_menu_check_list.*
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.R
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.data.interfaces.MenuCheckListDialogInterface
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.utils.AppUtils

/**
 * * Created by Anh Pham on 08/13/2018.     **
 * * Copyright (c) 2018 by FPT Telecom      **
 */
class MenuCheckListDialog : DialogFragment() {

    var confirmDialogInterface: MenuCheckListDialogInterface? = null
    private var index = 0
    //True : show 4 option. False : show 2 option
    private var typeDialog = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return inflater.inflate(R.layout.fragment_dialog_menu_check_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handleUIDialog()
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(AppUtils.getScreenWidth() - resources.getDimension(R.dimen._40dp).toInt(), ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    fun setDataDialog(confirmDialogInterface: MenuCheckListDialogInterface?, index: Int, typeDialog: Boolean) {
        this.index = index
        this.confirmDialogInterface = confirmDialogInterface
        this.typeDialog = typeDialog
    }

    private fun handleUIDialog() {
        if (typeDialog) {
            fragDialogMenuCheckList_cvErrorDetail.visibility = View.VISIBLE
            fragDialogMenuCheckList_cvStatusDone.visibility = View.VISIBLE
            fragDialogMenuCheckList_tvDetail.text = getString(R.string.view_image)
            fragDialogMenuCheckList_tvError.text = getString(R.string.send_email_notify)
        }
        fragDialogMenuCheckList_tvDetail.setOnClickListener {
            confirmDialogInterface?.onClickDetail(index)
            dismiss()
        }
        fragDialogMenuCheckList_tvError.setOnClickListener {
            confirmDialogInterface?.onClickError(index)
            dismiss()
        }
        fragDialogMenuCheckList_tvErrorDetail.setOnClickListener {
            confirmDialogInterface?.onClickUpdateDetail(index)
            dismiss()
        }
        fragDialogMenuCheckList_tvStatusDone.setOnClickListener {
            confirmDialogInterface?.onClickUpdateStatus(index)
            dismiss()
        }
    }
}