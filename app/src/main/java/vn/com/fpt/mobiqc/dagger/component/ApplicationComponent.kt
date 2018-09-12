package vn.com.fpt.mobiqc.dagger.component

import dagger.Component
import vn.com.fpt.mobiqc.dagger.module.ApplicationModule
import vn.com.fpt.mobiqc.dagger.module.NetworkModule
import vn.com.fpt.mobiqc.dagger.scope.AppScope
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