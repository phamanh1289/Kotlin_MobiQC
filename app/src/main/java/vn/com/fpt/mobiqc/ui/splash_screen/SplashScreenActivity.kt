package vn.com.fpt.mobiqc.ui.splash_screen

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.support.annotation.RequiresApi
import android.support.v4.app.ActivityCompat
import android.telephony.TelephonyManager
import com.google.firebase.analytics.FirebaseAnalytics
import vn.com.fpt.mobiqc.BuildConfig
import vn.com.fpt.mobiqc.R
import vn.com.fpt.mobiqc.data.interfaces.ConfirmDialogInterface
import vn.com.fpt.mobiqc.data.network.model.ResponseErrorDataModel
import vn.com.fpt.mobiqc.data.network.model.ResponseModel
import vn.com.fpt.mobiqc.data.realm.error.ErrorRealmManager
import vn.com.fpt.mobiqc.data.realm.location.LocationRealmManager
import vn.com.fpt.mobiqc.others.constant.Constants
import vn.com.fpt.mobiqc.others.dialog.ShowDownLoadDialog
import vn.com.fpt.mobiqc.ui.base.BaseActivity
import vn.com.fpt.mobiqc.ui.login.LoginFragment
import vn.com.fpt.mobiqc.utils.AppUtils
import vn.com.fpt.mobiqc.utils.StartActivityUtils
import javax.inject.Inject


class SplashScreenActivity : BaseActivity(), ConfirmDialogInterface, SplashScreenContract.SplashScreenView {

    companion object {
        const val ENABLE_UNKNOWN_RESOURCE = 1010
    }

    @Inject
    lateinit var presenter: SplashScreenPresenter

    private var url = ""
    private lateinit var mDialogDownload: ShowDownLoadDialog
    private var mAnalytics: FirebaseAnalytics? = null
    private val updateClick = object : ConfirmDialogInterface {
        override fun onClickOk() {
            if (AppUtils.getFileDownload().exists())
                installFileExist()
            else
                initParamGetNewVersion()
        }

        override fun onClickCancel() {

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        getActivityComponent().inject(this)
        presenter.onAttach(this)
        mAnalytics = FirebaseAnalytics.getInstance(this)
        initView()
    }

    private fun initView() {
        mDialogDownload = ShowDownLoadDialog()
        AppUtils.deleteFileExist()
        Handler().postDelayed({
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                try {
                    getPermission()?.requestEachCombined(
                            Manifest.permission.READ_PHONE_STATE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.WAKE_LOCK)
                            ?.subscribe {
                                if (it.granted) {
                                    presenter.let { pre ->
                                        showLoading()
                                        val map = HashMap<String, Any>()
                                        map[Constants.PARAMS_VERSION] = BuildConfig.VERSION_CODE
//                                    map[Constants.PARAMS_VERSION] = 20
                                        pre.getAppVersion(map)
                                    }
                                } else
                                    AppUtils.showDialog(fragmentManager = supportFragmentManager, content = getString(R.string.message_permission), confirmDialogInterface = this, actionCancel = true)
                            }
                } catch (e: Exception) {
                    startActivity(Intent(this, SplashScreenActivity::class.java))
                    finish()
                }
            }
        }, 1000)
    }

    private fun initParamGetNewVersion() {
        AppUtils.showDialogDownLoadData(supportFragmentManager, mDialogDownload)
        presenter.getNewFileVersion(url)
    }

    @SuppressLint("HardwareIds")
    private fun getImeiDevice() {
        if (getSharePreferences().imeiDevice.isBlank())
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED)
                getSharePreferences().imeiDevice = (getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager).deviceId
    }

    private fun installFileExist() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && !packageManager.canRequestPackageInstalls()) {
            AppUtils.showDialog(fragmentManager = supportFragmentManager, content = getString(R.string.mess_install_android_o), actionCancel = true, confirmDialogInterface = object : ConfirmDialogInterface {
                @RequiresApi(Build.VERSION_CODES.O)
                override fun onClickOk() {
                    startActivityForResult(Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES, Uri.parse("package:$packageName")), ENABLE_UNKNOWN_RESOURCE)
                }

                override fun onClickCancel() {
                    finish()
                }
            })
        } else
            when {
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.N -> installAllowAndroidNougat()
                else -> installBelowAndroidNougat()
            }
    }

    private fun installBelowAndroidNougat() {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.setDataAndType(Uri.fromFile(AppUtils.getFileDownload()), Constants.PATH_FILE_DOWNLOAD)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        this@SplashScreenActivity.finish()
    }

    private fun installAllowAndroidNougat() {
        val fileUri = android.support.v4.content.FileProvider.getUriForFile(this, applicationContext.packageName + ".provider", AppUtils.getFileDownload())
        val intent: Intent? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && !packageManager.canRequestPackageInstalls())
            Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES)
        else
            Intent(Intent.ACTION_INSTALL_PACKAGE)
        intent?.let {
            it.data = fileUri
            it.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
            startActivity(it)
        }
        this@SplashScreenActivity.finish()
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
//            map[Constants.PARAMS_IMEI_LOW] = getSharePreferences().imeiDevice
                map[Constants.PARAMS_IMEI_LOW] = "358548066496528"
                it.postCheckImei(map)
            }
        }
    }

    override fun loadCheckImei(response: ResponseModel) {
        if (response.Code == Constants.REQUEST_SUCCESS)
            presenter.let { it ->
                if (ErrorRealmManager().getCountError() == 0)
                    getSharePreferences().maxDateError = ErrorRealmManager().getMaxDate(resources)
                val map = HashMap<String, Any>()
                map[Constants.PARAMS_DATE_LOW] = getSharePreferences().maxDateError
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
                getSharePreferences().maxDateError = it
            }
            if (!getSharePreferences().checkReLogin()) {
                getSharePreferences().toClearSessionLogin()
                LocationRealmManager().deleteAllLocation()
                addFragment(LoginFragment(), false, true)
            } else StartActivityUtils().toMainActivity(this@SplashScreenActivity)
        } else
            AppUtils.showDialog(fragmentManager = supportFragmentManager, content = response.description, confirmDialogInterface = null)
    }

    override fun loadNewFileVersion(percent: Float) {
        if (percent.toInt() != Constants.DOWNLOAD_FAIL) {
            mDialogDownload.setPercentDownload(percent)
            if (percent.toInt() == Constants.DOWNLOAD_SUCCESS)
                installFileExist()
        } else {
            mDialogDownload.dismiss()
            AppUtils.showDialog(supportFragmentManager, content = getString(R.string.mess_error_update), confirmDialogInterface = object : ConfirmDialogInterface {
                override fun onClickOk() {
                    finish()
                }

                override fun onClickCancel() {
                }
            })
        }
    }

    override fun handleError(error: String) {
        hideLoading()
        AppUtils.showDialog(fragmentManager = supportFragmentManager, content = error, confirmDialogInterface = null)
        if (mDialogDownload.isAdded)
            mDialogDownload.dismiss()
    }

    override fun onClickCancel() {
        finish()
    }

    override fun onClickOk() {
        StartActivityUtils().toSettingPermission(this, packageName)
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onDetach()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ENABLE_UNKNOWN_RESOURCE && resultCode == Activity.RESULT_OK) {
            installAllowAndroidNougat()
        } else AppUtils.showDialog(supportFragmentManager, content = getString(R.string.mess_out_app), confirmDialogInterface = object : ConfirmDialogInterface {
            override fun onClickOk() {
                finish()
            }

            override fun onClickCancel() {
            }
        })
    }
}