package mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.others.service

import android.app.IntentService
import android.content.Intent
import android.os.Environment
import android.support.v4.content.LocalBroadcastManager
import android.util.Log
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.data.network.api.ApiService
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.data.network.model.DownloadFileModel
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.others.constant.Constants
import okhttp3.ResponseBody
import retrofit2.Retrofit
import java.io.BufferedInputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

/**
 * * Created by Anh Pham on 08/08/2018.     **
 * * Copyright (c) 2018 by FPT Telecom      **
 */
class DownloadService : IntentService("MyIntentService") {

    private var totalSize: Int = 0

    override fun onHandleIntent(intent: Intent?) {
        Log.d("testService", "Intent Service started")
        initDownload()
    }

    private fun initDownload() {
        val retrofit = Retrofit.Builder()
                .baseUrl("https://download.learn2crack.com/")
                .build()

        val retrofitInterface = retrofit.create(ApiService::class.java)
        val request = retrofitInterface.getFileNewVersion("https://goo.gl/npGkbY")
        try {
            downloadFile(request.execute().body()!!)
        } catch (e: Exception) {
            e.toString()
        }
    }

    @Throws(IOException::class)
    private fun downloadFile(body: ResponseBody) {

        val data = ByteArray(1024 * 4)
        val fileSize = body.contentLength()
        val bis = BufferedInputStream(body.byteStream(), 1024 * 8)
        val outputFile = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "DemoApkMobiQC.zip")
        val output = FileOutputStream(outputFile)
        var total: Long = 0
        val startTime = System.currentTimeMillis()
        var timeCount = 1
        val count: Int = bis.read(data)
        val download = DownloadFileModel()
        while (count != -1) {
            total += count.toLong()
            totalSize = (fileSize / Math.pow(1024.0, 2.0)).toInt()
            val current = Math.round(total / Math.pow(1024.0, 2.0)).toDouble()
            val progress = (total * 100 / fileSize).toInt()
            val currentTime = System.currentTimeMillis() - startTime
            download.totalFileSize = totalSize
            if (currentTime > 1000 * timeCount) {
                download.currentFileSize = current.toInt()
                download.progress = progress
                timeCount++
            }
            output.write(data, 0, count)
        }
        sendIntent(download)
        output.flush()
        output.close()
        bis.close()
    }

    private fun sendIntent(download: DownloadFileModel) {
        val intent = Intent(Constants.SERVICE_DOWNLOAD)
        intent.putExtra("download", download)
        LocalBroadcastManager.getInstance(this@DownloadService).sendBroadcast(intent)
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)
    }
}