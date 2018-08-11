package mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.dagger.module

import android.app.Activity
import android.content.Context
import dagger.Module
import dagger.Provides
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.dagger.scope.ActivityScope

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