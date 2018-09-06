package mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.others.service

import android.annotation.SuppressLint
import android.content.Context
import android.os.AsyncTask
import com.google.gson.Gson
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.R
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.data.network.model.ResponseUploadImageModel
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.data.network.model.UploadImageModel
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.others.constant.Constants
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.image.upload_image.UploadImageContract
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.image.upload_image.UploadImageFragment
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.utils.AppUtils
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*

/**
 * * Created by Anh Pham on 06/09/2018.     **
 * * Copyright (c) 2018 by AppsCyclone      **
 */
class UploadService(val context: Context?,var listBitmap : ArrayList<UploadImageModel>, val token : String,val view : UploadImageContract.UploadImageView?){

    @SuppressLint("StaticFieldLeak")
    inner class upLoadImageToServer : AsyncTask<Void, Int, Int>() {

        override fun onProgressUpdate(vararg values: Int?) {
            super.onProgressUpdate(*values)
            values[Constants.FIRST_ITEM]?.let {
                view?.loadUploadImageToServer(it)
            }
        }

        override fun doInBackground(vararg voids: Void): Int {
            val codeFolder = "MobiQC ${SimpleDateFormat("yyyyMM", Locale.getDefault()).format(Calendar.getInstance().time)}"
            var imgUploadSuccess = 0
            for (i in 0 until listBitmap.size) {
                val result = sendFileToServer(listBitmap[i].filePath, codeFolder)
                if (result !== UploadImageFragment.UPLOAD_FAIL) {
                    val imageModel = Gson().fromJson(result, ResponseUploadImageModel::class.java)
                    imageModel?.let {
                        if (it.ErrorCode == Constants.CREATE_SUCCESS)
                            listBitmap[i].pathServer = it.ImgPath
                        imgUploadSuccess++
                        publishProgress(imgUploadSuccess)
                    }
                }
            }
            return imgUploadSuccess
        }

        override fun onPostExecute(integer: Int) {
            super.onPostExecute(integer)
            view?.loadUploadImageToServer(integer)
        }
    }

    private fun sendFileToServer(filename: String, codeFolder: String): String {
        val newFileUpload = renameFileUpload(filename, codeFolder)
        var dataResponse = ""
        var connection: HttpURLConnection? = null
        var out: BufferedOutputStream? = null
        var fileStream: InputStream? = null
        var total = 0.0
        try {
            val buf = ByteArray(1024 * 4)
            fileStream = FileInputStream(newFileUpload)
            val sizeOfBlock = (1024 * 1024).toLong()
            while (dataResponse.isBlank()) {
                try {
                    if (fileStream == null)
                        fileStream = FileInputStream(filename)
                    connection = URL("http://iqc.fpt.vn/api/upload").openConnection() as HttpURLConnection
                    connection.run {
                        connectTimeout = 15 * 1000
                        doInput = true
                        doOutput = true
                        useCaches = false
                        requestMethod = "POST"
                        addRequestProperty(Constants.PARAMS_AUTHORIZATION,token)
                        setRequestProperty(Constants.PARAMS_CONTENT_DISPOSITION, "attachment; filename=\"${newFileUpload.name}\"")
                        out = BufferedOutputStream(outputStream)
                        var read = 1
                        out?.let {
                            while (read > 0 && total < sizeOfBlock) {
                                read = fileStream.read(buf)
                                if (read > 0) {
                                    total += read.toLong()
                                    it.write(buf, 0, read)
                                    it.flush()
                                }
                            }
                        }
                    }
                    val serverResponseCode = connection.responseCode
                    if (serverResponseCode == HttpURLConnection.HTTP_OK || serverResponseCode == HttpURLConnection.HTTP_CREATED) {
                        val s = Scanner(connection.inputStream).useDelimiter("\\A")
                        if (s.hasNext()) dataResponse = s.next()
                    } else
                        dataResponse = UploadImageFragment.UPLOAD_FAIL
                } catch (e: Exception) {
                    out?.close()
                    connection?.disconnect()
                    fileStream?.close()
                    e.printStackTrace()
                }
            }
        } catch (ex: Exception) {
            dataResponse = UploadImageFragment.UPLOAD_FAIL
            ex.printStackTrace()
        } finally {
            out?.close()
            connection?.disconnect()
            fileStream?.close()
        }
        return dataResponse
    }

    private fun renameFileUpload(pathUpload: String, codeFolder: String): File {
        val currentFile = File(pathUpload)
        if (currentFile.parentFile.exists()) {
            val date = SimpleDateFormat("yyyyMMddHHmmssSSSS", Locale.getDefault()).format(Calendar.getInstance().time)
            //File.separator = "/"
            val newFile = File(context?.getString(R.string.path_new_file, pathUpload.substring(0, pathUpload.lastIndexOf(File.separator)), codeFolder, date))
            if (currentFile.renameTo(newFile)) {
                AppUtils.removeFile(context, currentFile)
                AppUtils.addFileToSd(context, newFile)
                return newFile
            }
        }
        return currentFile
    }
}