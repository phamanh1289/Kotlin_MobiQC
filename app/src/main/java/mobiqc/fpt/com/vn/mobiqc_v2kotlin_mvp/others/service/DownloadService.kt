package mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.others.service

import android.os.Environment
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.data.network.api.ApiService
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.splash_screen.SplashScreenContract
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.io.BufferedInputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

/**
 * * Created by Anh Pham on 08/08/2018.     **
 * * Copyright (c) 2018 by FPT Telecom      **
 */
class DownloadService(private val url: String, private val view: SplashScreenContract.SplashScreenView?) {

    private var totalFile = 0
    fun initDownload() {
        val retrofit = Retrofit.Builder()
                .baseUrl("http://wsmobiqc.fpt.vn/MobiQC.svc/")
                .build()
        val retrofitInterface = retrofit.create(ApiService::class.java)
        retrofitInterface.getFileNewVersion(url).enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>?, t: Throwable?) {
                view?.handleError(t.toString())
            }

            override fun onResponse(call: Call<ResponseBody>?, response: Response<ResponseBody>?) {
                response?.body()?.let {
                    downloadFile(it)
                }
            }
        })
    }

    @Throws(IOException::class)
    fun downloadFile(body: ResponseBody) {
        val dataArray = ByteArray(1024 * 4)
        val buffer = BufferedInputStream(body.byteStream(), 1024 * 8)
        val outPutStream = FileOutputStream(File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "MobiQc_Release.apk"))
        totalFile = buffer.read(dataArray)
        while (totalFile != -1) {
            outPutStream.write(dataArray, 0, totalFile)
            totalFile = buffer.read(dataArray)
        }
        outPutStream.flush()
        outPutStream.close()
        buffer.close()
        view?.loadNewFileVersion(totalFile == -1)
    }
}