package vn.com.fpt.mobiqc.dagger.module

import android.app.Activity
import android.content.Context
import dagger.Module
import dagger.Provides
import vn.com.fpt.mobiqc.dagger.scope.ActivityScope

/**
 * * Created by Anh Pham on 08/02/2018.     **
 * * Copyright (c) 2018 by FPT Telecom      **
 */
@ActivityScope
@Module
class ActivityModule(private var activity: Activity) {

    @Provides
    fun provideContext(): Context {
        return activity
    }

    @Provides
    fun provideActivity(): Activity {
        return activity
    }
}