package mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.splash_screen

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.ActivityCompat
import android.telephony.TelephonyManager
import android.view.View
import kotlinx.android.synthetic.main.activity_splash_screen.*
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.BuildConfig
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.R
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.data.interfaces.ConfirmDialogInterface
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.data.network.model.ResponseErrorDataModel
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.data.network.model.ResponseModel
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.data.realm.error.ErrorRealmManager
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.others.constant.Constants
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.base.BaseActivity
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.login.LoginFragment
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.utils.AppUtils
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.utils.StartActivityUtils
import javax.inject.Inject


@Suppress("DEPRECATION")
class SplashScreenActivity : BaseActivity(), ConfirmDialogInterface, SplashScreenContract.SplashScreenView {

    @Inject
    lateinit var presenter: SplashScreenPresenter

    private var url = ""
    private val updateClick = object : ConfirmDialogInterface {
        override fun onClickOk() {
            showDialogLoadData(true)
            presenter.getNewFileVersion(url)
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
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                getPermission().requestEachCombined(
                        Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.USE_FINGERPRINT,
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
            }
        }, 1000)
    }

    @SuppressLint("HardwareIds")
    private fun getImeiDevice() {
        if (getSharePreferences().imeiDevice.isBlank())
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED)
                getSharePreferences().imeiDevice = (getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager).deviceId
    }

    private fun showDialogLoadData(check: Boolean) {
        getSharePreferences().onBackPress = check
        actSplashScreen_llLoadData.visibility = if (check) View.VISIBLE else View.GONE
        actSplashScreen_imgLogo.visibility = if (check) View.GONE else View.VISIBLE
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
                if (ErrorRealmManager().getCountError() == 0)
                    hideLoading()
                showDialogLoadData(ErrorRealmManager().getCountError() == 0)
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
            if (getSharePreferences().createDate.isBlank() || getSharePreferences().createDate != AppUtils.getCurrentDate(Constants.NONE_LATE_DATE)) {
                getSharePreferences().toClearSessionLogin()
                addFragment(LoginFragment(), false, true)
            } else StartActivityUtils().toMainActivity(this@SplashScreenActivity)
        } else
            AppUtils.showDialog(fragmentManager = supportFragmentManager, content = response.description, confirmDialogInterface = null)
    }

    override fun loadNewFileVersion(response: Boolean) {
        if (response == Constants.DOWNLOAD_SUCCESS) {
            showDialogLoadData(false)
            addFragment(LoginFragment(), false, true)
        }
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

    override fun onBackPressed() {
        if (!getSharePreferences().onBackPress)
            super.onBackPressed()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onDetach()
    }
}
