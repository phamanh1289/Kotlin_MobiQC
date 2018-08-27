package mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.others.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import kotlinx.android.synthetic.main.fragment_dialog_send_email.*
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.R
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.data.interfaces.ConfirmDialogInterface
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.utils.AppUtils

/**
 * * Created by Anh Pham on 08/14/2018.     **
 * * Copyright (c) 2018 by FPT Telecom      **
 */
class SendEmailDialog : DialogFragment() {

    var confirmDialogInterface: ConfirmDialogInterface? = null
    private var sendMail = ""
    private var ccMail = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return inflater.inflate(R.layout.fragment_dialog_send_email, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadDataDialog()
    }

    fun setDataDialog(sendMail: String, ccMail: String, confirmDialogInterface: ConfirmDialogInterface?) {
        this.sendMail = sendMail
        this.ccMail = ccMail
        this.confirmDialogInterface = confirmDialogInterface
    }

    private fun loadDataDialog() {
        fragDialogSendEmail_tvSendMail.setText("phamanh1289@gmail.com")
        fragDialogSendEmail_tvCcEmail.setText("anhpham.developer@gmail.com")
//        fragDialogSendEmail_tvSendMail.setText(sendMail)
//        fragDialogSendEmail_tvCcEmail.setText(getString(R.string.add_default_email, ccMail))
        fragDialogSendEmail_tvCancel.setOnClickListener {
            confirmDialogInterface?.onClickCancel()
        }
        fragDialogSendEmail_tvOK.setOnClickListener {
            confirmDialogInterface?.onClickOk()
        }
    }

    fun getListSendMail(): String {
        return fragDialogSendEmail_tvSendMail.text.toString().trim()
    }

    fun getListCcMail(): String {
        return fragDialogSendEmail_tvCcEmail.text.toString().trim()
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(AppUtils.getScreenWidth() - resources.getDimension(R.dimen._40dp).toInt(), ViewGroup.LayoutParams.WRAP_CONTENT)
    }
}