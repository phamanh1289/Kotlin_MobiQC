package vn.com.fpt.mobiqc.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import vn.com.fpt.mobiqc.ui.main.MainActivity
import vn.com.fpt.mobiqc.ui.splash_screen.SplashScreenActivity


/**
 * * Created by Anh Pham on 08/03/2018.     **
 * * Copyright (c) 2018 by FPT Telecom      **
 */
class StartActivityUtils {

    fun toMainActivity(context: Context) {
        val intent = Intent().setClass(context, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        context.startActivity(intent)
    }

    fun toSplashActivity(context: Context) {
        val intent = Intent().setClass(context, SplashScreenActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        context.startActivity(intent)
    }

    fun toSettingPermission(context: Context, packageName: String) {
        val intent = Intent()
        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        intent.data = Uri.fromParts("package", packageName, null)
        context.startActivity(intent)
    }
}