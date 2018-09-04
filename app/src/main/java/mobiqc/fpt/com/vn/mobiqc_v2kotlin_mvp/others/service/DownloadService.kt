package mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.others.service

import android.os.AsyncTask
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.data.network.api.ApiService
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.others.constant.Constants
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.splash_screen.SplashScreenContract
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.utils.AppUtils
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
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
                        .connectTimeout(1, TimeUnit.MINUTES)
                        .readTimeout(4, TimeUnit.MINUTES)
                        .writeTimeout(4, TimeUnit.MINUTES)
                        .build())
                .baseUrl("http://wsmobiqc.fpt.vn/MobiQC.svc/")
                .build()
        val retrofitInterface = retrofit.create(ApiService::class.java)
        retrofitInterface.getFileNewVersion(url).enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>?, t: Throwable?) {
                view?.loadNewFileVersion(Constants.DOWNLOAD_FAIL.toFloat())
            }

            override fun onResponse(call: Call<ResponseBody>?, response: Response<ResponseBody>?) {
                response?.body()?.let {
                    UpdateProcess().execute(it)
                }
            }
        })
    }

    inner class UpdateProcess : AsyncTask<ResponseBody, Float, Float>() {

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
                    currentSize += totalFile
                    outPutStream.write(dataArray, 0, totalFile)
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