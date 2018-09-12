package vn.com.fpt.mobiqc.dagger.module

import android.app.Application
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * * Created by Anh Pham on 08/02/2018.     **
 * * Copyright (c) 2018 by FPT Telecom      **
 */
@Module
class ApplicationModule(private val baseApplication: Application) {

    @Provides
    @Singleton
    fun provideApplicationContext(): Application {
        return baseApplication
    }
}