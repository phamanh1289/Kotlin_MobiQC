package mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.utils

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.app.FragmentManager
import android.util.Patterns
import android.widget.TextView
import com.google.android.gms.maps.model.LatLng
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.R
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.data.interfaces.ConfirmDialogInterface
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.data.interfaces.MenuCheckListDialogInterface
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.data.network.model.AccountGroup
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.data.network.model.DetailReportModel
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.data.network.model.PhoneNumberModel
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.data.network.model.SingleChoiceModel
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.others.constant.Constants
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.others.dialog.*
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.check_list.create_check_list.CreateCheckListFragment
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.check_list.create_pre_check_list.CreatePreCheckListFragment
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.contract.check_contract.CheckContractFragment
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.contract.search_contract.SearchFragment
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.error.create.CreateErrorFragment
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.error.update.UpdateErrorFragment
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.io.IOException
import java.math.BigDecimal
import java.text.DecimalFormat
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList


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

    fun isValidEmail(fragmentManager: FragmentManager?, listEmail: String, context: Context?): Boolean {
        var result = true
        context?.let {
            val elementEmail = ArrayList<String>()
            if (listEmail.contains(";")) {
                elementEmail.addAll(listEmail.split(";"))
            } else elementEmail.add(listEmail)
            if (elementEmail.size != 0) {
                for (i in 0 until elementEmail.size) {
                    if (elementEmail[i].isNotBlank())
                        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(elementEmail[i]).matches() || !elementEmail[i].contains(it.getString(R.string.default_type_email))) {
                            showDialog(fragmentManager, content = it.getString(R.string.mess_validate_email_detail, elementEmail[i]), confirmDialogInterface = null)
                            result = false
                            break
                        }
                }
            } else {
                result = false
                showDialog(fragmentManager, content = it.getString(R.string.mess_validate_email), confirmDialogInterface = null)
            }
        }
        return result
    }

    fun getScreenWidth(): Int {
        return Resources.getSystem().displayMetrics.widthPixels
    }

    fun isURL(url: String): Boolean {
        return Patterns.WEB_URL.matcher(url).matches()
    }

    fun getScreenHeight(): Int {
        return Resources.getSystem().displayMetrics.heightPixels
    }

    fun toAddHtmlToImage(link: String): String {
        return "<img src='http://iqc.fpt.vn$link'>"
    }

    fun getUrlImage(link: String): String {
        return if (link.isNotBlank())
            "http://iqc.fpt.vn/$link"
        else ""
    }

    fun showDialog(fragmentManager: FragmentManager?, title: String = "", content: String = "", actionCancel: Boolean = false, confirmDialogInterface: ConfirmDialogInterface?) {
        fragmentManager?.let {
            val dialog = ConfirmDialog()
            dialog.setDataDialog(title = title, content = content, actionCancel = actionCancel, confirmDialogInterface = confirmDialogInterface)
            dialog.show(it, ConfirmDialog::class.java.simpleName)
        }
    }

    fun showMenuCheckListDialog(fragmentManager: FragmentManager?, confirmDialogInterface: MenuCheckListDialogInterface, index: Int, typeOption: Boolean) {
        fragmentManager?.let {
            val dialog = MenuCheckListDialog()
            dialog.setDataDialog(confirmDialogInterface = confirmDialogInterface, index = index, typeDialog = typeOption)
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
            dialog.show(it, ShowDownLoadDialog::class.java.simpleName)
        }
    }

    fun showDialogPhoneNumber(fragmentManager: FragmentManager?, name: String, list: ArrayList<PhoneNumberModel>) {
        fragmentManager?.let {
            val dialog = PhoneNumberDialog()
            dialog.setData(name = name, list = list)
            dialog.show(it, PhoneNumberDialog::class.java.simpleName)
        }
    }

    @SuppressLint("SimpleDateFormat")
    fun generateImageCode(code: String): String {
        val format = SimpleDateFormat("yyMMddHHmmss")
        return "$code${format.format(Calendar.getInstance().time)}"
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

    fun showPickTime(context: Context?, tvDate: TextView, typeDate: Boolean) {
        val calendar = Calendar.getInstance()
        context?.let {
            val datePickerDialog = DatePickerDialog(it, { _, year, monthOfYear, dayOfMonth ->
                tvDate.text = it.resources.getString(R.string.date_time_format, toConvertMonth(dayOfMonth), toConvertMonth(monthOfYear + 1), year.toString())
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
            val arrDate = tvDate.text.toString()
            if (arrDate.isNotBlank()) {
                val list = if (arrDate.contains("-")) arrDate.split("-") else arrDate.split("/")
                datePickerDialog.updateDate(list[2].toInt(), list[1].toInt() - 1, list[0].toInt())
            }
            if (typeDate)
                datePickerDialog.datePicker.maxDate = System.currentTimeMillis() - 1000
            else
                datePickerDialog.datePicker.minDate = System.currentTimeMillis() - 1000
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
            s1.isNullOrEmpty() && s2.isNullOrEmpty() -> "N/A"
            s1.isNullOrEmpty() -> if (typeCheck) toConvertTimeToString(context, s2!!) else s2!!
            s2.isNullOrEmpty() -> if (typeCheck) toConvertTimeToString(context, s1!!) else s1!!
            s1?.isNotBlank()!! -> s1
            else -> s2!!
        }
    }

    fun setFormatMoney(value: Long): String {
        return NumberFormat.getNumberInstance(Locale.US).format(value)
    }

    fun autoInsertDot(text: String): String {
        var originalString = text
        val longValue: Long?
        if (originalString.contains(",")) {
            originalString = originalString.replace(",".toRegex(), "")
        }
        longValue = java.lang.Long.parseLong(originalString)
        val formatter = NumberFormat.getInstance(Locale.US) as DecimalFormat
        formatter.applyPattern("#,###,###,###")
        return formatter.format(longValue)
    }

    fun showDialogSingChoiceGroup(fragmentManager: FragmentManager?, title: String, listData: ArrayList<AccountGroup>, view: TextView, itemSelected: Int) {
        val listSingle = ArrayList<SingleChoiceModel>()
        listData.forEach {
            listSingle.add(SingleChoiceModel(account = it.group, status = it.status))
        }
        showDialogSingChoice(fragmentManager, title, listSingle, view, itemSelected)
    }

    fun showDialogSingChoice(fragmentManager: FragmentManager?, title: String, listData: ArrayList<SingleChoiceModel>, view: TextView, itemSelected: Int) {
        val dialog = SingChoiceDialog()
        dialog.setDataDialog(title = title, list = listData) { position ->
            listData[itemSelected].status = false
            listData[position].status = true
            val fragment = fragmentManager?.findFragmentById(android.R.id.tabcontent)
            when (fragment) {
                is CheckContractFragment -> {
                    when (view.id) {
                        R.id.fragCheckContract_tvMobiGroup -> {
                            fragment.dataMobiGroup[itemSelected].status = false
                            fragment.dataMobiGroup[position].status = true
                            fragment.positionMobiGroup = position
                            fragment.getDataAcc(position)
                        }
                        R.id.fragCheckContract_tvMobiAcc -> fragment.positionMobiAcc = position
                        R.id.fragCheckContract_tvMobiType -> fragment.positionMobiType = position
                    }
                }
                is UpdateErrorFragment -> {
                    fragment.setDefaultValueIndex(view.id, position)
                    fragment.setDefaultData(view.id)
                }
                is SearchFragment -> fragment.setDefaultValueIndex(view.id, position)
                is CreatePreCheckListFragment -> fragment.positionFirstStatus = position
                is CreateCheckListFragment -> {
                    fragment.positionFirstStatus = position
                    fragment.initParamGetOwner(fragment.isCheckOwner)
                }
                is CreateErrorFragment ->
                    fragment.setDefaultValueIndex(view.id, position)
            }
            view.text = listData[position].account
            dialog.submitData(listData)
            dialog.dismiss()
        }
        dialog.show(fragmentManager, SingChoiceDialog::class.java.simpleName)
    }

    fun removeAddPref(text: String): String {
        return when {
            text.contains(".") -> text.replace(".", "")
            text.contains(",") -> text.replace(",", "")
            text.isBlank() -> "0"
            else -> text
        }
    }

    fun getLocationUser(text: String): String {
        var result = text
        if (result.contains("("))
            result = text.replace("(", "")
        if (result.contains(")"))
            result = result.replace(")", "")
        return result
    }

    fun getTwoDecimal(num: Double): Double {
        return Math.round(num * 100).toDouble() / 100
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

    fun compareDate(start: String, end: String): Boolean {
        val startDate = if (start.contains("-")) start.replace("-", "/") else start
        val endDate = if (start.contains("-")) end.replace("-", "/") else end
        return formatter.parse(startDate).before(formatter.parse(endDate))
    }

    fun changeFormatDistance(distance: Double): String {
        return when {
            distance == 0.0 -> "0 m"
            distance < 1 -> "${distance * 1000} mm"
            distance > 1000 -> "${String.format("%.2f", distance / 1000)} km"
            else -> "${String.format("%.2f", distance)} m"
        }
    }

    fun getLatLng(latLng: String): LatLng {
        val result = getLocationUser(latLng)
        val listArr = result.split(",")
        return if (listArr.isNotEmpty()) LatLng(listArr[0].toDouble(), listArr[1].toDouble()) else LatLng(0.toDouble(), 0.toDouble())
    }

    fun convertStringToMacAddress(text: String): String {
        return when {
            text.contains(":") -> text
            text.contains("-") -> text.replace("-", ":")
            else -> {
                var result = text
                var countLoop = text.length / 2
                var firstIndex = 2
                while (countLoop > 1) {
                    result = StringBuffer(result).insert(firstIndex, ":").toString()
                    firstIndex += 3
                    countLoop--
                }
                result
            }
        }
    }

    fun getExternalIp(): String {
        var ipWan = ""
        val client = OkHttpClient()
        try {
            val url = Constants.URL_CHECK_IP
            val request = Request.Builder().url(url).build()
            val response = client.newCall(request).execute()
            val responseBody = response.body()
            if (responseBody != null) ipWan = responseBody.string()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return ipWan
    }

    fun getSumListData(list: ArrayList<DetailReportModel>): Float {
        var sum = 0f
        list.forEach { sum += it.value }
        return sum
    }

    fun getCurrentPercent(value: Float, sum: Float): String {
        return ((value / sum) * 100.0).toBigDecimal().setScale(1, BigDecimal.ROUND_HALF_EVEN).toString()
    }

    fun addFileToSd(context: Context?, file: File) {
        val intent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
        intent.data = Uri.fromFile(file)
        context?.sendBroadcast(intent)
    }

    fun removeFile(context: Context?, file: File) {
        val resolver = context?.contentResolver
        resolver?.delete(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, MediaStore.Images.Media.DATA + "=?", arrayOf(file.absolutePath))
    }

}