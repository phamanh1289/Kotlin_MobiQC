package mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.infomation

import android.content.Context.WIFI_SERVICE
import android.content.Intent
import android.net.wifi.WifiManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_infomation.*
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.BuildConfig
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.R
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.others.constant.Constants
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.base.BaseFragment
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.utils.KeyboardUtils
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException


/**
 * * Created by Anh Pham on 08/02/2018.     **
 * * Copyright (c) 2018 by FPT Telecom      **
 */
class InformationFragment : BaseFragment() {

    private val compositeSubscription = CompositeDisposable()

    companion object {
        fun newInstance(title: String): InformationFragment {
            val args = Bundle()
            args.putString(Constants.ARG_TITLE, title)
            val fragment = InformationFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_infomation, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.let { KeyboardUtils.setupUI(view, activity = it) }
        initView()
    }

    private fun initView() {
        fragInformation_tvEmail.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.email_haint)))
            intent.type = Constants.TYPE_SEND_MAIL
            startActivity(Intent.createChooser(intent, Constants.TYPE_SEND_MAIL))
        }
        fragInformation_tvImei.text = getSharePreferences().imeiDevice
        fragInformation_tvAppName.text = getString(R.string.app_name)
        fragInformation_tvVersion.text = BuildConfig.VERSION_NAME
        fragInformation_tvAccountName.text = getSharePreferences().accountName
        fragInformation_tvIpLan.text = getIpLan()
        getExternalIpAsync()
    }

    private fun getIpLan(): String {
        val wm = context?.getSystemService(WIFI_SERVICE) as WifiManager
        return android.text.format.Formatter.formatIpAddress(wm.connectionInfo.ipAddress)
    }

    private fun getExternalIpAsync() {
        showLoading()
        compositeSubscription.add(
                Observable.just("")
                        .map { getIpWan() }
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe { ip: String ->
                            fragInformation_tvIpWan.text = ip
                            hideLoading()
                        }
        )
    }

    override fun onStop() {
        super.onStop()
        compositeSubscription.clear()
    }

    private fun getIpWan(): String {
        var ipWan = getString(R.string.mess_no_connection)
        val client = OkHttpClient()
        try {
            val url = getString(R.string.url_check_ip)
            val request = Request.Builder().url(url).build()
            val response = client.newCall(request).execute()
            val responseBody = response.body()
            if (responseBody != null) ipWan = responseBody.string()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return ipWan
    }
}

