package mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.image.upload_image

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.support.v7.widget.GridLayoutManager
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.erikagtierrez.multiple_media_picker.Gallery
import kotlinx.android.synthetic.main.fragment_upload_image.*
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.R
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.data.network.model.ResponseModel
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.data.network.model.TitleAndMenuModel
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.data.network.model.UploadImageModel
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.others.constant.Constants
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.base.BaseFragment
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.image.upload_image.diff.UploadImageAdapter
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.utils.AppUtils
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.utils.KeyboardUtils
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONException
import org.json.JSONObject
import java.io.*
import java.net.HttpURLConnection
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject


/**
 * * Created by Anh Pham on 08/09/2018.     **
 * * Copyright (c) 2018 by FPT Telecom      **
 */
class UploadImageFragment : BaseFragment(), UploadImageContract.UploadImageView {

    @Inject
    lateinit var presenter: UploadImagePresenter

    private var listBitmap = ArrayList<UploadImageModel>()
    private lateinit var adapterImage: UploadImageAdapter
    var mIsUploadImage = false

    companion object {
        const val RESULT_CODE_IMAGE = 101
        const val TYPE_IMAGE_AND_VIDEO = 1
        const val TYPE_IMAGE = 2
        const val TYPE_VIDEO = 3
        const val MAX_IMAGE = 6
        const val MAX_COL = 2
        const val TITLE = "title"
        const val MODEL = "mode"
        const val MAX_SELECTION = "maxSelection"
        const val RESULT = "result"
        fun newInstance(code: String): UploadImageFragment {
            val args = Bundle()
            args.putString(Constants.ARG_IMAGE_CODE, code)
            val fragment = UploadImageFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_upload_image, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getActivityComponent().inject(this)
        presenter.onAttach(this)
        activity?.let { KeyboardUtils.setupUI(view, activity = it) }
        initView()
    }

    private fun initView() {
        setTitle(TitleAndMenuModel(title = getString(R.string.upload_image)))
        initRecyclerViewImage()
        initOnClick()
    }

    private fun initRecyclerViewImage() {
        adapterImage = UploadImageAdapter {
            listBitmap.removeAt(it)
            adapterImage.notifyItemRemoved(it)
        }
        adapterImage.submitList(listBitmap)
        fragUpLoadImage_rvMain.apply {
            adapter = adapterImage
            setHasFixedSize(true)
            val layout = GridLayoutManager(context, MAX_COL)
            layoutManager = layout
        }
    }

    private fun initOnClick() {
        fragUpLoadImage_imgChoiceImage.setOnClickListener {
            if (listBitmap.size != MAX_IMAGE)
                toChoicePicture()
            else
                AppUtils.showDialog(fragmentManager, content = getString(R.string.max_pick_image), confirmDialogInterface = null)
        }
        fragUpLoadImage_imgUpImage.setOnClickListener {
            if (listBitmap.size == 0)
                AppUtils.showDialog(fragmentManager, content = getString(R.string.mess_upload_image), confirmDialogInterface = null)
            else initDataToUpload()
        }
    }

    private fun toChoicePicture() {
        val intent = Intent(context, Gallery::class.java)
        intent.putExtra(TITLE, getString(R.string.mess_title_image))
        intent.putExtra(MODEL, TYPE_IMAGE)
        intent.putExtra(MAX_SELECTION, MAX_IMAGE - listBitmap.size)
        startActivityForResult(intent, RESULT_CODE_IMAGE)
    }

    override fun loadUploadImageToServer(response: ResponseModel) {
        hideLoading()
        AppUtils.showDialog(fragmentManager, content = response.message, confirmDialogInterface = null)
    }

    override fun loadCreateImage(response: ResponseModel) {

    }

    override fun loadAddImage(response: ResponseModel) {

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == RESULT_CODE_IMAGE) {
            data?.let { intent ->
                val arrImageChoice = intent.getStringArrayListExtra(RESULT)
                if (arrImageChoice.size != 0) {
                    for (i in 0 until arrImageChoice.size) {
                        listBitmap.add(UploadImageModel(filePath = arrImageChoice[i]))
                    }
                    adapterImage.notifyDataSetChanged()
                }
            }
        }
    }

    override fun handleError(error: String) {
        hideLoading()
        AppUtils.showDialog(fragmentManager, content = error, confirmDialogInterface = null)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onDetach()
    }

    //==================
    private fun initDataToUpload() {
        showLoading()
        val codeFolder = "MobiQC ${SimpleDateFormat("yyyyMM", Locale.getDefault()).format(Calendar.getInstance().time)}"
        val date = SimpleDateFormat("yyyyMMddHHmmssSSSS", Locale.getDefault()).format(Calendar.getInstance().time)
        val body: MultipartBody.Part?
        val imgFile = File(listBitmap[0].filePath)
        var pathTo = ""
        if (imgFile.exists())
            pathTo = imgFile.absolutePath.substring(0, imgFile.absolutePath.lastIndexOf(File.separator)) + "/MOBIQC_" + codeFolder + "_" + date + ".jpg"
        val requestFile = RequestBody.create(MediaType.parse("image/*"), imgFile)
//        body = MultipartBody.Part.createFormData("fileType", pathTo, requestFile)
        presenter.postUploadImage(getSharePreferences().userToken, requestFile)
    }

//    public interface ApiInterface {
//        @Multipart
//        @POST("/api/Accounts/editaccount")
//        Call<User> editUser (@Header("Authorization") String authorization, @Part("file\"; filename=\"pp.png\" ") RequestBody file , @Part("FirstName") RequestBody fname, @Part("Id") RequestBody id);
//    }
//
//    File file = new File(imageUri.getPath());
//    RequestBody fbody = RequestBody.create(MediaType.parse("image/*"), file);
//    RequestBody name = RequestBody.create(MediaType.parse("text/plain"), firstNameField.getText().toString());
//    RequestBody id = RequestBody.create(MediaType.parse("text/plain"), AZUtils.getUserId(this));
//    Call<User> call = client.editUser(AZUtils.getToken(this), fbody, name, id);
//    call.enqueue(new Callback<User>() {
//        @Override
//        public void onResponse(retrofit.Response<User> response, Retrofit retrofit) {
//            AZUtils.printObject(response.body());
//        }
//
//        @Override
//        public void onFailure(Throwable t) {
//            t.printStackTrace();
//        }
//    });



    inner class taskUpload : AsyncTask<Void, String, Int>() {
        override fun onPreExecute() {
            super.onPreExecute()
            showLoading()
        }

        override fun onProgressUpdate(vararg values: String) {
            super.onProgressUpdate(*values)
//            tvProgress.setText(values[0])
        }

        override fun doInBackground(vararg voids: Void): Int {
            val codeFolder = "MobiQC ${SimpleDateFormat("yyyyMM", Locale.getDefault()).format(Calendar.getInstance().time)}"
            var num = 0
            for (i in 0 until listBitmap.size) {
                val img = listBitmap[i]
                publishProgress("Đang tải lên hình thứ " + (i + 1) + "...")
                val result = sendFileToServer(img.filePath, "http://iqc.fpt.vn/api/upload", getSharePreferences().userToken, codeFolder, getSharePreferences().accountName)
                if (result !== "error") {
                    try {
                        val jsonResult = JSONObject(result)
                        if (jsonResult.getInt("ErrorCode") == 0) {
                            img.filePath  = jsonResult.getString("ImgPath").toString()
//                            img.setUpload(true)
                            num++
                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }
            }
            return num
        }

        override fun onPostExecute(integer: Int) {
            super.onPostExecute(integer)
            hideLoading()
            AppUtils.showDialog(fragmentManager, content = "Đã upload thành công $integer ảnh", confirmDialogInterface = null)
            if (integer > 0) {
                createAlbumImage()
            }
        }
    }

    fun createAlbumImage() {

    }

    private fun sendFileToServer(filename: String, targetUrl: String, accessToken: String, codeFolder: String, uploader: String): String {
        var filename = filename
        //Set timeout upload 1 image is 2 minutes
        mIsUploadImage = true
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({ mIsUploadImage = false }, (120 * 1000).toLong())

        var response = "error"
        Log.e("Image filename", filename)
        Log.e("url", targetUrl)
        var connection: HttpURLConnection? = null
        var out: BufferedOutputStream? = null
        var fileStream: InputStream? = null
        var total: Long = 0
        var checksum = ""

        //Rename file upload
        val fileUploadOld = File(filename)
        val date = SimpleDateFormat("yyyyMMddHHmmssSSSS").format(Calendar.getInstance().time)
        val absolutePath = fileUploadOld.absolutePath
        val pathTo = absolutePath.substring(0, absolutePath.lastIndexOf(File.separator)) + "/MOBIQC_" + codeFolder + "_" + date + ".jpg"
        val fileUpload = File(pathTo)
        if (!renameImageFile(context, fileUploadOld, fileUpload))
            return "error"
        //        fileUploadOld.renameTo(fileUpload);
        filename = fileUpload.path
        try {
            // Read from filePath and upload to server (url)
            val buf = ByteArray(1024 * 16)
            fileStream = FileInputStream(filename)

            val lengthOfFile = fileUpload.length()
            Log.i("UPLOAD", "Length of file: $lengthOfFile B")

            // int sizeOfPacket = 8192; // 8kb
            val sizeOfBlock = (1024 * 1024).toLong() //
            val totalChunk = ((lengthOfFile + (sizeOfBlock - 1)) / sizeOfBlock).toInt()
            var headerValue: String

            val data = filename.toByteArray(charset("UTF-8"))
            val base64 = Base64.encodeToString(data, Base64.NO_WRAP)
                    .replace("[\"\';\\-\\+\\.\\^:,?=!@#$%^&*()\\[\\]]".toRegex(), "")
            Log.i("UPLOAD", "Session id upload .......: $base64")
            var contentLength: Long
            var i = 0

            while (mIsUploadImage && i < totalChunk) {
                val totalTmp = total
                if (fileStream == null) {
                    fileStream = FileInputStream(filename)
                }
                try {
                    val from = i * sizeOfBlock
                    var to: Long
                    if (from + sizeOfBlock > lengthOfFile) {
                        to = lengthOfFile
                    } else {
                        to = sizeOfBlock * (i + 1)
                    }
                    to = to - 1
                    contentLength = to - from + 1
                    headerValue = "bytes $from-$to/$lengthOfFile"

                    val url = URL(targetUrl)
                    connection = url.openConnection() as HttpURLConnection
                    connection.connectTimeout = 15 * 1000

                    Log.i("UPLOAD",
                            "chunk uploading: " + "..........." + (i + 1) + "......")
                    Log.i("UPLOAD", "content Range upload: $headerValue")
                    Log.i("UPLOAD",
                            "............file name..................: " + fileUpload.name)

                    connection.doInput = true
                    connection.doOutput = true
                    connection.useCaches = false
                    connection.requestMethod = "POST"

                    connection.addRequestProperty("Authorization", "Bearer $accessToken")
                    connection.addRequestProperty("Content-Type", "application/json; charset=UTF-8")
                    connection.setRequestProperty("Connection", "Keep-Alive")
                    connection.setRequestProperty("Content-Disposition",
                            "attachment; filename=\"" + fileUpload.name + "\"")
                    connection.setRequestProperty("Session-ID", base64)
                    connection.setRequestProperty("Content-Range", headerValue)
                    connection.setRequestProperty("X-Chunk-Index", (i + 1).toString() + "")
                    connection.setRequestProperty("X-Chunks-Number", totalChunk.toString() + "")
                    connection.setRequestProperty("code-folder", codeFolder)
                    connection.setRequestProperty("uploader", uploader)
                    connection.setRequestProperty("Content-Length", contentLength.toString() + "")
                    if (i > 0) {
                        connection.setRequestProperty("X-Last-Checksum", checksum)
                    }
                    connection.setRequestProperty("iqc-contract-name", "test")
                    connection.setRequestProperty("iqc-mobi-account", "test")

                    out = BufferedOutputStream(connection.outputStream)

                    var read = 1
                    while (read > 0 && total < sizeOfBlock * (i + 1)) {
                        if (!isCancelled()) {
                            read = fileStream.read(buf)
                            if (read > 0) {
                                total += read.toLong()
                                out.write(buf, 0, read)
                                out.flush()
                            }
                        }
                    }

                    if (isCancelled()) {
                        out.close()
                        fileStream.close()
                        connection.disconnect()
                        return "error"
                    }
                    val serverResponseCode = connection.responseCode
                    val serverResponseMessage = connection.responseMessage
                    Log.i("UPLOAD", "Server Response Code $serverResponseCode")
                    Log.i("UPLOAD", "Server Response Message$serverResponseMessage")

                    // if upload chunk i success then upload next chunk
                    // else upload chunk i again
                    if (serverResponseCode == HttpURLConnection.HTTP_OK || serverResponseCode == HttpURLConnection.HTTP_CREATED) {
                        checksum = ""
                        i++
                        val map = connection.headerFields
                        for ((key, value) in map) {
                            if (!(key + "").isEmpty() && key + "" == "X-Checksum") {
                                for (item in value) {
                                    checksum = checksum + item
                                }
                                break
                            }
                        }
                        Log.i("UPLOAD", ".............X-Checksum.....: $checksum")
                    } else
                        return "error"

                    if (i < totalChunk - 1) {
                        out.close()
                        out = null
                        connection.disconnect()
                    } else {
                        //                        response = Common.getStringFromInputStream(connection.getInputStream());
                        val s = Scanner(connection.inputStream).useDelimiter("\\A")
                        response = if (s.hasNext()) s.next() else ""
                        Log.d("UPLOAD", "JSON String after upload image: $response")
                        out.close()
                        out = null
                        connection.disconnect()
                    }
                } catch (e: Exception) {
                    total = totalTmp
                    connection?.disconnect()

                    if (out != null) {
                        out.close()
                        out = null
                    }

                    fileStream.close()
                    fileStream = null

                    e.printStackTrace()
                }

            }
        } catch (ex: Exception) {
            // Exception handling
            response = "error"
            Log.e("UPLOAD", "Send file Exception: " + ex.message)
            ex.printStackTrace()
        } finally {
            connection?.disconnect()
            try {
                fileStream?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
        return response
    }

    private fun isCancelled(): Boolean {
        return false
    }

    fun renameImageFile(c: Context?, from: File, to: File): Boolean {
        var result = false
        c?.let {
            result = if (from.renameTo(to)) {
                removeMedia(it, from)
                addMedia(it, to)
                true
            } else {
                false
            }
        }
        return result
    }

    fun addMedia(c: Context, f: File) {
        val intent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
        intent.data = Uri.fromFile(f)
        c.sendBroadcast(intent)
    }

    private fun removeMedia(c: Context, f: File) {
        val resolver = c.contentResolver
        resolver.delete(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, MediaStore.Images.Media.DATA + "=?", arrayOf(f.absolutePath))
    }
}