package mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.image.upload_image

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
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
import java.io.File
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

    companion object {
        const val RESULT_CODE_IMAGE = 101
        const val TYPE_IMAGE_AND_VIDEO = 1
        const val TYPE_IMAGE = 2
        const val TYPE_VIDEO = 3
        const val MAX_IMAGE = 6
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
            val layout = GridLayoutManager(context, 2)
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


    private fun sendFileToServer() {

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


    private fun initDataToUpload() {
        showLoading()
        val codeFolder = "MobiQC ${SimpleDateFormat("yyyyMM", Locale.getDefault()).format(Calendar.getInstance().time)}"
        val date = SimpleDateFormat("yyyyMMddHHmmssSSSS").format(Calendar.getInstance().time)
        var body: MultipartBody.Part? = null
        for (i in 0 until listBitmap.size) {
            val imgFile = File(listBitmap[i].filePath)
            var bitmap: Bitmap? = null
            var pathTo = ""
            if (imgFile.exists()) {
                pathTo = imgFile.absolutePath.substring(0, imgFile.absolutePath.lastIndexOf(File.separator)) + "/MOBIQC_" + codeFolder + "_" + date + ".jpg"
                bitmap = BitmapFactory.decodeFile(imgFile.absolutePath)
            }
//            val buffer = ByteArrayOutputStream(it.width * it.height)
//            it.compress(Bitmap.CompressFormat.JPEG, 100, buffer)
            val requestFile = RequestBody.create(
                    MediaType.parse("multipart/form-data"),
                    imgFile
            )
            val timeStamp = System.currentTimeMillis().toString()
            val item = MultipartBody.Part.createFormData("uploaded_file", "$timeStamp.jpg", requestFile)
            body = item


//            bitmap?.let {
//                val buffer = ByteArrayOutputStream(it.width * it.height)
//                it.compress(Bitmap.CompressFormat.JPEG, 100, buffer)
//                val requestFile = RequestBody.create(
//                        MediaType.parse("multipart/form-data"),
//                        imgFile
//                )
//                val timeStamp = System.currentTimeMillis().toString()
//                val item = MultipartBody.Part.createFormData("uploaded_file", "$timeStamp.jpg", requestFile)
//                body = item
//            }
        }
        presenter.postUploadImage("Bearer 674cb342-79a2-4e94-8c61-1901bcb25371", body!!)
    }
}