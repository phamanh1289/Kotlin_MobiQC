package mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.base

import android.app.Application
import android.content.Context
import io.realm.Realm
import io.realm.RealmConfiguration
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.R
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.dagger.component.ApplicationComponent
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.dagger.component.DaggerApplicationComponent
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.dagger.connect.ApiConfigType
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.dagger.module.ApplicationModule
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.dagger.module.NetworkModule
import uk.co.chrisjenx.calligraphy.CalligraphyConfig

/**
 * * Created by Anh Pham on 08/02/2018.     **
 * * Copyright (c) 2018 by FPT Telecom      **
 */
class BaseApplication : Application() {
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