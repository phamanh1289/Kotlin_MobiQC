package vn.com.fpt.mobiqc.dagger.module

import android.app.Application
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import vn.com.fpt.mobiqc.dagger.connect.ApiConfig
import vn.com.fpt.mobiqc.dagger.connect.ApiConfigType
import vn.com.fpt.mobiqc.dagger.scope.AppScope
import vn.com.fpt.mobiqc.data.network.api.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit

/**
 * * Created by Anh Pham on 08/02/2018.     **
 * * Copyright (c) 2018 by FPT Telecom      **
 */
@Module
@AppScope
class NetworkModule(private val mType: ApiConfigType) {
    private val CACHE_SIZE_BUFFER = (50 * 1024 * 1024).toLong()
    private val httpClient = OkHttpClient.Builder()
            .connectTimeout(1, TimeUnit.MINUTES)
            .readTimeout(4, TimeUnit.MINUTES)
            .writeTimeout(4, TimeUnit.MINUTES)
            .addInterceptor(ConnectivityInterceptor())
            .retryOnConnectionFailure(true)
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .addInterceptor(ApiCustomInterceptor())

    @Provides
    fun provideOkHttpClient(app: Application): OkHttpClient {
        val cacheDir = File(app.cacheDir, "http")
        return OkHttpClient.Builder()
                .readTimeout(1, TimeUnit.MINUTES)
                .connectTimeout(1, TimeUnit.MINUTES)
                .writeTimeout(1, TimeUnit.MINUTES)
                .cache(okhttp3.Cache(cacheDir, CACHE_SIZE_BUFFER))
                .retryOnConnectionFailure(true)
                .build()
    }

    @Provides
    fun provideApiService(): ApiService {
        val retrofit = Retrofit.Builder()
                .baseUrl(ApiConfig.createConnectionDetail(mType).baseURL)
                .client(httpClient.build())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(GsonBuilder()
                        .setLenient()
                        .create()))
                .build()
        return retrofit.create(ApiService::class.java)
    }

    @Provides
    fun provideApiMobiNetService(): ApiMobiNetService {
        val retrofit = Retrofit.Builder()
                .baseUrl(ApiConfig.createConnectionDetail(mType).urlMobiNet)
                .client(httpClient.build())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(GsonBuilder()
                        .setLenient()
                        .create()))
                .build()
        return retrofit.create(ApiMobiNetService::class.java)
    }

    @Provides
    fun provideApiWsMobiNetService(): ApiWsMobiNetService {
        val retrofit = Retrofit.Builder()
                .baseUrl(ApiConfig.createConnectionDetail(mType).urlWsMobiNet)
                .client(httpClient.build())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(GsonBuilder()
                        .setLenient()
                        .create()))
                .build()
        return retrofit.create(ApiWsMobiNetService::class.java)
    }

    @Provides
    fun provideApiIstorageService(): ApiIstorageService {
        val retrofit = Retrofit.Builder()
                .baseUrl(ApiConfig.createConnectionDetail(mType).urlStorage)
                .client(httpClient.build())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(GsonBuilder()
                        .setLenient()
                        .create()))
                .build()
        return retrofit.create(ApiIstorageService::class.java)
    }

    @Provides
    fun provideApiUploadImageService(): ApiUploadImageService {
        val retrofit = Retrofit.Builder()
                .baseUrl(ApiConfig.createConnectionDetail(mType).urlUploadImage)
                .client(httpClient.build())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(GsonBuilder()
                        .setLenient()
                        .create()))
                .build()
        return retrofit.create(ApiUploadImageService::class.java)
    }
}