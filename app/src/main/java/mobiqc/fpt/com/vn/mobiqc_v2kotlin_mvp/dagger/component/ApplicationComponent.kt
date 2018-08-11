package mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.dagger.component

import dagger.Component
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.dagger.module.ApplicationModule
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.dagger.module.NetworkModule
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.dagger.scope.AppScope
import javax.inject.Singleton

/**
 * * Created by Anh Pham on 08/02/2018.     **
 * * Copyright (c) 2018 by FPT Telecom      **
 */

@AppScope
@Singleton
@Component(modules = [ApplicationModule::class, NetworkModule::class])
interface ApplicationComponent {
    fun getActivityComponent() : ActivityComponent
}