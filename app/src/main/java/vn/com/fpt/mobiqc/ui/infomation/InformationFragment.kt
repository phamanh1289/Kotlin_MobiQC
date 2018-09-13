package vn.com.fpt.mobiqc.ui.infomation

import android.content.Context.WIFI_SERVICE
import android.content.Intent
import android.net.wifi.WifiManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_infomation.*
import vn.com.fpt.mobiqc.BuildConfig
import vn.com.fpt.mobiqc.R
import vn.com.fpt.mobiqc.data.network.model.TitleAndMenuModel
import vn.com.fpt.mobiqc.others.constant.Constants
import vn.com.fpt.mobiqc.ui.base.BaseFragment
import vn.com.fpt.mobiqc.utils.KeyboardUtils


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
        var title = ""
        arguments?.let {
            title = it.getString(Constants.ARG_TITLE)
        }
        setTitle(TitleAndMenuModel(title = title))
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

