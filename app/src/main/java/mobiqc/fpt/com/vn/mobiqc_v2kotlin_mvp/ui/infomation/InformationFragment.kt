package mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.infomation

import android.content.Context.WIFI_SERVICE
import android.content.Intent
import android.net.wifi.WifiManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_infomation.*
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.BuildConfig
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.R
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.others.constant.Constants
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.base.BaseFragment
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.utils.KeyboardUtils


/**
 * * Created by Anh Pham on 08/02/2018.     **
 * * Copyright (c) 2018 by FPT Telecom      **
 */
class InformationFragment : BaseFragment() {

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
        fragInformation_tvIpWan.text = getSharePreferences().ipWan
    }

    private fun getIpLan(): String {
        val wm = context?.getSystemService(WIFI_SERVICE) as WifiManager
        return android.text.format.Formatter.formatIpAddress(wm.connectionInfo.ipAddress)
    }
}

