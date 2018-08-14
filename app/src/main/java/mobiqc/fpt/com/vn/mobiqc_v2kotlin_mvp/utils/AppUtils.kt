package mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.utils

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Environment
import android.support.v4.app.FragmentManager
import android.text.TextUtils
import android.widget.TextView
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.R
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.data.interfaces.ConfirmDialogInterface
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.data.interfaces.MenuCheckListDialogInterface
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.data.network.model.AccountGroup
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.data.network.model.PhoneNumberModel
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.data.network.model.SingleChoiceModel
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.others.constant.Constants
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.others.dialog.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * * Created by Anh Pham on 08/02/2018.     **
 * * Copyright (c) 2018 by FPT Telecom      **
 */
object AppUtils {
    @SuppressLint("SimpleDateFormat")
    val formatter = SimpleDateFormat(Constants.TIME_DATE_FORMAT)

    fun getNetwork(context: Context?): Boolean {
        val cm = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        val activeNetwork = cm?.activeNetworkInfo
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting
    }

    fun isValidEmail(email: String): Boolean {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun getScreenWidth(): Int {
        return Resources.getSystem().displayMetrics.widthPixels
    }

    fun getScreenHeight(): Int {
        return Resources.getSystem().displayMetrics.heightPixels
    }

    fun showDialog(fragmentManager: FragmentManager?, title: String = "", content: String = "", actionCancel: Boolean = false, confirmDialogInterface: ConfirmDialogInterface?) {
        fragmentManager?.let {
            val dialog = ConfirmDialog()
            dialog.setDataDialog(title = title, content = content, actionCancel = actionCancel, confirmDialogInterface = confirmDialogInterface)
            dialog.show(it, ConfirmDialog::class.java.simpleName)
        }
    }

    fun showMenuCheckListDialog(fragmentManager: FragmentManager?, confirmDialogInterface: MenuCheckListDialogInterface, index: Int) {
        fragmentManager?.let {
            val dialog = MenuCheckListDialog()
            dialog.setDataDialog(confirmDialogInterface = confirmDialogInterface, index = index)
            dialog.show(it, MenuCheckListDialog::class.java.simpleName)
        }
    }

    fun showAddressToMap(context: Context?, address: String) {
        val mapIntent = Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0?z=18&q=${address.replace(" ", "+")}"))
        mapIntent.setPackage("com.google.android.apps.maps")
        context?.startActivity(mapIntent)
    }

    fun showDialogDownLoadData(fragmentManager: FragmentManager?, dialog: ShowDownLoadDialog) {
        fragmentManager?.let {
            dialog.show(it, ConfirmDialog::class.java.simpleName)
        }
    }

    fun showDialogPhoneNumber(fragmentManager: FragmentManager?, name: String, list: ArrayList<PhoneNumberModel>) {
        fragmentManager?.let {
            val dialog = PhoneNumberDialog()
            dialog.setData(name = name, list = list)
            dialog.show(it, PhoneNumberDialog::class.java.simpleName)
        }
    }

    fun getFileDownload(): File {
        return File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), Constants.FILE_NAME_DOWNLOAD)
    }

    fun deleteFileExist() {
        if (getFileDownload().exists())
            getFileDownload().delete()
    }

    fun makeCallPhoneNumber(context: Context?, number: String) {
        context?.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("tel:$number")))
    }

    fun showPickTime(context: Context?, tvDate: TextView) {
        val calendar = Calendar.getInstance()
        context?.let {
            val datePickerDialog = DatePickerDialog(it,
                    { view, year, monthOfYear, dayOfMonth -> tvDate.text = it.resources.getString(R.string.date_time_format, toConvertMonth(dayOfMonth), toConvertMonth(monthOfYear + 1), year.toString()) }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
            datePickerDialog.datePicker.maxDate = System.currentTimeMillis() - 1000
            datePickerDialog.show()
        }
    }

    private fun toConvertMonth(month: Int): String {
        return if (month < 10) "0$month" else month.toString()
    }

    fun getDefaultDateSearch(toDate: TextView, fromDate: TextView, lateDate: Int) {
        toDate.text = getCurrentDate(0)
        fromDate.text = getCurrentDate(lateDate)
    }

    fun getCurrentDate(lateDate: Int): String {
        val c = Calendar.getInstance()
        if (lateDate != 0)
            c.add(Calendar.DATE, lateDate)
        return formatter.format(c.time)
    }

    fun toConvertTimeToString(context: Context?, time: String): String {
        return if (time.contains("T")) {
            val arr = time.split("T")
            "${toConvertDateFormat(context, arr[0])} ${arr[1].split(".")[0]}"
        } else time
    }

    fun toConvertDateFormat(context: Context?, date: String): String {
        val arr: List<String> = if (date.contains("/")) date.split("/") else date.split("-")
        context?.let {
            return it.getString(R.string.date_time_format, arr[2], arr[1], arr[0])
        }
        return ""
    }

    fun handleAssignDate(context: Context?, s1: String?, s2: String?, typeCheck: Boolean): String {
        return when {
            s1.isNullOrEmpty() -> if (typeCheck) toConvertTimeToString(context, s2!!) else s2!!
            s2.isNullOrEmpty() -> if (typeCheck) toConvertTimeToString(context, s1!!) else s1!!
            s1?.isNotBlank()!! -> s1
            else -> "N/A"
        }
    }

    fun getPopUpSingleChoice(context: Context?, listData: ArrayList<SingleChoiceModel>, onClick: (Int) -> Unit): SelectSinglePopup {
        val popUp = SelectSinglePopup(listData
                , onClick = onClick)
        popUp.onCreateView(context = context!!)
        return popUp
    }

    fun getPopUpSingleChoiceroup(context: Context?, listData: ArrayList<AccountGroup>, onClick: (Int) -> Unit): SelectSingleGroupPopup {
        val popUp = SelectSingleGroupPopup(listData
                , onClick = onClick)
        popUp.onCreateView(context = context!!)
        return popUp
    }

    fun handleCheckDate(context: Context?, start: String, end: String): String {
        context?.let {
            val startDate = formatter.parse(start)
            val endDate = formatter.parse(end)
            if (endDate.before(startDate))
                return it.getString(R.string.error_date)
            else {
                val longDiff = endDate.time - startDate.time
                if (TimeUnit.DAYS.convert(longDiff, TimeUnit.MILLISECONDS) > 7)
                    return it.getString(R.string.error_calculator_date)
            }
        }
        return ""
    }
}