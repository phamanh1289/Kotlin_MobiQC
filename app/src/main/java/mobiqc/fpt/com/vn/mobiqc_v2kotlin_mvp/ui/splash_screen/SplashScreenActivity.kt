package mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.splash_screen

import android.Manifest
import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.support.v4.app.ActivityCompat
import android.support.v4.content.LocalBroadcastManager
import android.telephony.TelephonyManager
import android.util.Log
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.BuildConfig
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.R
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.data.interfaces.ConfirmDialogInterface
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.data.network.model.DownloadFileModel
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.data.network.model.ResponseErrorDataModel
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.data.network.model.ResponseModel
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.others.constant.Constants
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.others.service.DownloadService
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.base.BaseActivity
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.login.LoginFragment
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.utils.AppUtils
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.utils.StartActivityUtils
import java.io.BufferedInputStream
import java.io.File
import java.io.FileOutputStream
import java.net.HttpURLConnection
import java.net.URL
import javax.inject.Inject


@Suppress("DEPRECATION")
class SplashScreenActivity : BaseActivity(), ConfirmDialogInterface, SplashScreenContract.SplashScreenView {

    @Inject
    lateinit var presenter: SplashScreenPresenter

    var url: String? = null
    private val updateClick = object : ConfirmDialogInterface {
        override fun onClickOk() {
            url?.let {
                //                GetFileNewVersion().execute(it)
            }
        }

        override fun onClickCancel() {

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        getActivityComponent().inject(this)
        presenter.onAttach(this)
        initView()
    }

    private fun initView() {
        Handler().postDelayed({
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                getPermission().requestEachCombined(
                        Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
//                        Manifest.permission.USE_FINGERPRINT,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.WAKE_LOCK)
                        .subscribe {
                            if (it.granted) {
                                presenter.let { callApi ->
                                    showLoading()
                                    val map = HashMap<String, Any>()
                                    map["Version"] = BuildConfig.VERSION_CODE
//                                    map["Version"] = 20
                                    callApi.getAppVersion(map)
                                }
                            } else
                                AppUtils.showDialog(fragmentManager = supportFragmentManager, content = getString(R.string.message_permission), confirmDialogInterface = this, actionCancel = true)
                        }
//            }
        }, 1000)
    }

    @SuppressLint("HardwareIds")
    private fun getImeiDevice() {
        if (getSharePreferences().imeiDevice.isBlank())
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED)
                getSharePreferences().imeiDevice = (getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager).deviceId
    }

    override fun loadAppVersion(response: ResponseModel) {
        if (response.Code == Constants.REQUEST_UPDATE) {
            hideLoading()
            url = response.Link
            AppUtils.showDialog(fragmentManager = supportFragmentManager, content = response.Description, confirmDialogInterface = updateClick)
        } else {
            getImeiDevice()
            presenter.let {
                val map = HashMap<String, Any>()
//            map["imei"] = getSharePreferences().imeiDevice
                map["imei"] = "358548066496528"
                it.postCheckImei(map)
            }
        }
    }

    override fun loadCheckImei(response: ResponseModel) {
        if (response.Code == Constants.REQUEST_SUCCESS)
            presenter.let { it ->
                val map = HashMap<String, Any>()
                map["date"] = getSharePreferences().maxDateError
                it.getNewErrorData(map)
            }
        else {
            hideLoading()
            AppUtils.showDialog(fragmentManager = supportFragmentManager, content = response.Description, confirmDialogInterface = null)
        }
    }

    override fun loadNewErrorData(response: ResponseErrorDataModel, data: String?) {
        hideLoading()
        if (response.code == Constants.REQUEST_SUCCESS) {
            data?.let {
                getSharePreferences().maxDateError = data
            }
            if (getSharePreferences().createDate.isBlank() || getSharePreferences().createDate != AppUtils.getCurrentDate(Constants.NONE_LATE_DATE)) {
                getSharePreferences().toClearSessionLogin()
                addFragment(LoginFragment(), false, true)
            } else StartActivityUtils().toMainActivity(this@SplashScreenActivity)
        } else
            AppUtils.showDialog(fragmentManager = supportFragmentManager, content = response.description, confirmDialogInterface = null)
    }

    override fun handleError(error: String) {
        hideLoading()
        AppUtils.showDialog(fragmentManager = supportFragmentManager, content = error, confirmDialogInterface = null)
    }

    override fun onClickCancel() {
    }

    override fun onClickOk() {
        StartActivityUtils().toSettingPermission(this, packageName)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onDetach()
    }


    //Demo download file
    private fun startDownload() {
        val intent = Intent(this@SplashScreenActivity, DownloadService::class.java)
        startService(intent)
    }

    private fun registerReceiver() {
        val bManager = LocalBroadcastManager.getInstance(this)
        val intentFilter = IntentFilter()
        intentFilter.addAction(Constants.SERVICE_DOWNLOAD)
        bManager.registerReceiver(broadcastReceiver, intentFilter)
    }

    private val broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {

            if (intent.action == Constants.SERVICE_DOWNLOAD) {

                val download = intent.getSerializableExtra("download") as DownloadFileModel
//                mProgressBar.setProgress(download.progress)
                if (download.progress == 100) {

//                    mProgressText.setText("File Download Complete")

                } else {

//                    mProgressText.setText(String.format("Downloaded (%d/%d) MB", download.currentFileSize, download.totalFileSize))

                }
            }
        }
    }


    inner class GetFileNewVersion : AsyncTask<String, Int, Int>() {
        private var dialog: ProgressDialog? = null
        var PATH_OUTPUT = ""
        var FILE_NAME = "demoFile.apk"

        init {
            PATH_OUTPUT = Environment.getExternalStorageDirectory().absolutePath + "/Download/" + FILE_NAME
        }

        override fun onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute()
            dialog = ProgressDialog(this@SplashScreenActivity)
            dialog!!.setMessage("Đang tải bản mới. Vui lòng chờ...")
            dialog!!.isIndeterminate = false
            dialog!!.max = 100
            dialog!!.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL)
            dialog!!.setCancelable(false)
            dialog!!.show()
        }

        override fun doInBackground(vararg params: String): Int? {
            var count: Int
            var lenghtOfFile = 0
            try {
                val url = URL(params[0])
                val connection = url
                        .openConnection() as HttpURLConnection
                connection.requestMethod = "GET"
                connection.readTimeout = 10000
                connection.connectTimeout = 15000
                connection.doOutput = true
                connection.connect()
                val response = connection.responseCode
                if (response != -1) {
                    lenghtOfFile = connection.contentLength
                    Log.e("file size", lenghtOfFile.toString() + "")
                    val input = BufferedInputStream(url.openStream(),
                            8192)
                    val output = FileOutputStream(PATH_OUTPUT)
                    val data = ByteArray(1024)
                    var total: Long = 0
                    count = input.read(data)

                    while (count != -1) {
                        total += count.toLong()
                        val progress = (total * 100 / lenghtOfFile).toInt()
                        Log.e("progress %", progress.toString() + " %")
                        publishProgress(progress)
                        output.write(data, 0, count)
                    }
                    publishProgress(100)
                    output.flush()
                    output.close()
                    input.close()
                    return 100
                }
            } catch (e: Exception) {
                Log.e("Error update: ", e.message)
            }

            return 0
        }

        override fun onProgressUpdate(vararg values: Int?) {
            dialog!!.progress = Integer.parseInt("" + values[0])
        }

        override fun onPostExecute(result: Int?) {
            dialog!!.progress = result!!
            if (result != 0) {
                val file = File(PATH_OUTPUT)
                val intent = Intent(Intent.ACTION_VIEW)
                intent.setDataAndType(Uri.fromFile(file),
                        "application/vnd.android.package-archive")
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                this@SplashScreenActivity.startActivity(intent)
                this@SplashScreenActivity.finish()
                deleteUpdateFile()
            }
        }

        fun deleteUpdateFile() {
            PATH_OUTPUT = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).absolutePath + FILE_NAME
            val file = File(PATH_OUTPUT)
            if (file.exists())
                file.delete()
        }
    }

}
