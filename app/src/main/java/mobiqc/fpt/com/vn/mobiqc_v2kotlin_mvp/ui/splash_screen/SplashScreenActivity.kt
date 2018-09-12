package mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.splash_screen

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.ActivityCompat
import android.telephony.TelephonyManager
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.R
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.data.interfaces.ConfirmDialogInterface
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.data.network.model.ResponseErrorDataModel
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.data.network.model.ResponseModel
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.data.realm.error.ErrorRealmManager
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.data.realm.location.LocationRealmManager
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.others.constant.Constants
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.others.dialog.ShowDownLoadDialog
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.base.BaseActivity
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.login.LoginFragment
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.utils.AppUtils
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.utils.StartActivityUtils
import javax.inject.Inject


class SplashScreenActivity : BaseActivity(), ConfirmDialogInterface, SplashScreenContract.SplashScreenView {
    companion object {
        const val CHECK_PERMISSION = 1100
    }

    @Inject
    lateinit var presenter: SplashScreenPresenter

    private var url = ""
    private lateinit var mDialogDownload: ShowDownLoadDialog
    private val updateClick = object : ConfirmDialogInterface {
        override fun onClickOk() {
            if (AppUtils.getFileDownload().exists())
                installFileExist()
            else {
                initParamGetNewVersion()
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
        mDialogDownload = ShowDownLoadDialog()
        AppUtils.deleteFileExist()
        Handler().postDelayed({
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                getPermission().requestEachCombined(
                        Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.WAKE_LOCK)
                        .subscribe {
                            if (it.granted) {
                                presenter.let { pre ->
                                    showLoading()
                                    val map = HashMap<String, Any>()
//                                    map[Constants.PARAMS_VERSION] = BuildConfig.VERSION_CODE
                                    map[Constants.PARAMS_VERSION] = 20
                                    pre.getAppVersion(map)
                                }
                            } else
                                AppUtils.showDialog(fragmentManager = supportFragmentManager, content = getString(R.string.message_permission), confirmDialogInterface = this, actionCancel = true)
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
        val fileUri = android.support.v4.content.FileProvider.getUriForFile(this, applicationContext.packageName + ".provider", AppUtils.getFileDownload())
        val intent = Intent(Intent.ACTION_VIEW)
        intent.setDataAndType(fileUri, Constants.PATH_FILE_DOWNLOAD)
        intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        startActivity(intent)
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
//            map["imei"] = getSharePreferences().imeiDevice
                map["imei"] = "358548066496528"
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
            AppUtils.showDialog(supportFragmentManager, content = getString(R.string.mess_error_update), actionCancel = true, confirmDialogInterface = object : ConfirmDialogInterface {
                override fun onClickOk() {
                    initParamGetNewVersion()
                }

                override fun onClickCancel() {
                    finish()
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
}