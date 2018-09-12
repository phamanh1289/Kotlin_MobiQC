package vn.com.fpt.mobiqc.others.service

import android.annotation.SuppressLint
import android.os.AsyncTask
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import vn.com.fpt.mobiqc.dagger.module.ConnectivityInterceptor
import vn.com.fpt.mobiqc.data.network.api.ApiService
import vn.com.fpt.mobiqc.others.constant.Constants
import vn.com.fpt.mobiqc.others.custom_body.DownloadProgressInterceptor
import vn.com.fpt.mobiqc.ui.splash_screen.SplashScreenContract
import vn.com.fpt.mobiqc.utils.AppUtils
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import java.io.BufferedInputStream
import java.io.FileOutputStream
import java.util.concurrent.TimeUnit


/**
 * * Created by Anh Pham on 08/08/2018.     **
 * * Copyright (c) 2018 by FPT Telecom      **
 */
class DownloadService(private val url: String, private val view: SplashScreenContract.SplashScreenView?) {

    companion object {
        const val DEFAULT_BYTE_ARRAY = 1024 * 16
    }

    fun initDownload() {
        val retrofit = Retrofit.Builder()
                .client(OkHttpClient.Builder()
                        .addInterceptor(ConnectivityInterceptor())
                        .addInterceptor(DownloadProgressInterceptor(view))
                        .retryOnConnectionFailure(true)
                        .connectTimeout(1, TimeUnit.MINUTES)
                        .readTimeout(4, TimeUnit.MINUTES)
                        .writeTimeout(4, TimeUnit.MINUTES)
                        .build())
                .baseUrl("http://wsmobiqc.fpt.vn/MobiQC.svc/")
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
        retrofit.create(ApiService::class.java)
                .getFileNewVersion(url)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe( { it ->
                    DownLoadFileApk().execute(it)
                },{
                    view?.loadNewFileVersion(Constants.DOWNLOAD_FAIL.toFloat())
                })
//        val sink = Okio.buffer(Okio.sink(AppUtils.getFileDownload()))
//        sink.writeAll(responseBody?.source() as Source)
//        sink.close()
    }

    @SuppressLint("StaticFieldLeak")
    inner class DownLoadFileApk : AsyncTask<ResponseBody, Float, Float>() {

        override fun onProgressUpdate(vararg values: Float?) {
            super.onProgressUpdate(*values)
            values[Constants.FIRST_ITEM]?.let {
                view?.loadNewFileVersion(it)
            }
        }

        override fun doInBackground(vararg params: ResponseBody?): Float? {
            val dataArray = ByteArray(DEFAULT_BYTE_ARRAY)
            val buffer = BufferedInputStream(params[Constants.FIRST_ITEM]?.byteStream(), DEFAULT_BUFFER_SIZE)
            val outPutStream = FileOutputStream(AppUtils.getFileDownload())
            var currentSize = 0f
            val totalSize = params[Constants.FIRST_ITEM]?.contentLength()
            try {
                var totalFile = buffer.read(dataArray)
                while (totalFile != -1) {
                    outPutStream.write(dataArray, 0, totalFile)
                    currentSize += totalFile
                    totalSize?.let {
                        publishProgress(((currentSize / it) * 100))
                    }
                    totalFile = buffer.read(dataArray)
                }
            } catch (e: Exception) {
                publishProgress(Constants.DOWNLOAD_FAIL.toFloat())
            } finally {
                outPutStream.flush()
                outPutStream.close()
                buffer.close()
            }
            return currentSize
        }

        override fun onPostExecute(result: Float?) {
            super.onPostExecute(result)
            result?.let {
                view?.loadNewFileVersion(it)
            }
        }
    }
}