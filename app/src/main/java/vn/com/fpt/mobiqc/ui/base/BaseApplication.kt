package vn.com.fpt.mobiqc.ui.base

import android.content.Context
import android.support.multidex.MultiDex
import android.support.multidex.MultiDexApplication
import io.realm.Realm
import io.realm.RealmConfiguration
import vn.com.fpt.mobiqc.R
import vn.com.fpt.mobiqc.dagger.component.ApplicationComponent
import vn.com.fpt.mobiqc.dagger.component.DaggerApplicationComponent
import vn.com.fpt.mobiqc.dagger.connect.ApiConfigType
import vn.com.fpt.mobiqc.dagger.module.ApplicationModule
import vn.com.fpt.mobiqc.dagger.module.NetworkModule
import uk.co.chrisjenx.calligraphy.CalligraphyConfig

/**
 * * Created by Anh Pham on 08/02/2018.     **
 * * Copyright (c) 2018 by FPT Telecom      **
 */
class BaseApplication : MultiDexApplication() {
    companion object {
        lateinit var instance: BaseApplication private set
        val dbName = "mobi_qc.realm"
    }

    lateinit var baseApp: ApplicationComponent

    override fun onCreate() {
        super.onCreate()
        instance = this
        setupInjector()
        Realm.init(this)
        Realm.setDefaultConfiguration(RealmConfiguration.Builder().name(dbName).build())
        CalligraphyConfig.initDefault(CalligraphyConfig.Builder()
                .setDefaultFontPath(getString(R.string.Helvetica))
                .setFontAttrId(R.attr.fontPath)
                .build())
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    private fun setupInjector() {
        baseApp = DaggerApplicationComponent.builder()
                .applicationModule(ApplicationModule(this))
                .networkModule(NetworkModule(ApiConfigType.DEVELOP))
                .build()
    }

    fun getApplicationComponent(): ApplicationComponent {
        return baseApp
    }
}