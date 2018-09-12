package vn.com.fpt.mobiqc.others.custom_body

import io.reactivex.subjects.PublishSubject
import vn.com.fpt.mobiqc.ui.splash_screen.SplashScreenContract
import vn.com.fpt.mobiqc.utils.AppUtils
import okhttp3.MediaType
import okhttp3.ResponseBody
import okio.*
import java.io.IOException


/**
 * * Created by Anh Pham on 10/09/2018.     **
 * * Copyright (c) 2018 by AppsCyclone      **
 */
open class DownloadProgressResponseBody(val responseBody: ResponseBody?, val view: SplashScreenContract.SplashScreenView?) : ResponseBody() {

    val updateProcess: PublishSubject<Float> = PublishSubject.create()

    override fun contentLength(): Long {
        return responseBody?.contentLength() ?: 0
    }

    override fun contentType(): MediaType? {
        return responseBody?.contentType()
    }

    override fun source(): BufferedSource {
        return Okio.buffer(source(responseBody?.source() as Source))
    }

    fun writeFileToSdCard() {
        val sink = Okio.buffer(Okio.sink(AppUtils.getFileDownload()))
        sink.writeAll(responseBody?.source() as Source)
        sink.close()
    }

    private fun source(source: Source): Source {
        return object : ForwardingSource(source) {
            var totalBytesRead: Long? = 0L
            var currentPercent = 0f
            @Throws(IOException::class)
            override fun read(sink: Buffer, byteCount: Long): Long {
                val bytesRead = super.read(sink, byteCount)
                totalBytesRead = responseBody?.contentLength() ?: 0L
                val percent: Float
                if (bytesRead != -1L) {
                    currentPercent += bytesRead
                    percent = ((currentPercent / totalBytesRead!!) * 100)
                    updateProcess.onNext(percent)
                }
                return bytesRead
            }
        }
    }
}