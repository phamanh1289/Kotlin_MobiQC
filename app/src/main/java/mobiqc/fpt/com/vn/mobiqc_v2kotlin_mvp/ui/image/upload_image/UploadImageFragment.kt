package mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.image.upload_image

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.erikagtierrez.multiple_media_picker.Gallery
import kotlinx.android.synthetic.main.fragment_upload_image.*
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.R
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.data.interfaces.ConfirmDialogInterface
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.data.network.model.LinkImageUploadModel
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.data.network.model.ResponseModel
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.data.network.model.TitleAndMenuModel
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.data.network.model.UploadImageModel
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.others.constant.Constants
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.others.dialog.ShowDownLoadDialog
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.base.BaseFragment
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.image.upload_image.diff.UploadImageAdapter
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.utils.AppUtils
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.utils.KeyboardUtils
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
    private lateinit var mDialogDownload: ShowDownLoadDialog
    private lateinit var imageCode: String
    private var countAddImage = 0

    companion object {
        const val RESULT_CODE_IMAGE = 101
        const val IMAGE_CODE_EXSIT = 404
        const val TYPE_IMAGE_AND_VIDEO = 1
        const val TYPE_IMAGE = 2
        const val TYPE_VIDEO = 3
        const val MAX_IMAGE = 6
        const val MAX_COL = 2
        const val TITLE = "title"
        const val MODEL = "mode"
        const val MAX_SELECTION = "maxSelection"
        const val RESULT = "result"
        const val UPLOAD_FAIL = "error"
        const val DEFAULT_STATUS_CREATE_IAMGE = 1
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
        mDialogDownload = ShowDownLoadDialog()
        arguments?.let {
            imageCode = it.getString(Constants.ARG_IMAGE_CODE) ?: ""
        }
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
            else {
                AppUtils.showDialogDownLoadData(fragmentManager, mDialogDownload)
                presenter.postUploadImage(context = context, token = getSharePreferences().userToken, list = listBitmap)
            }
        }
    }

    private fun toChoicePicture() {
        val intent = Intent(context, Gallery::class.java)
        intent.putExtra(TITLE, getString(R.string.mess_title_image))
        intent.putExtra(MODEL, TYPE_IMAGE)
        intent.putExtra(MAX_SELECTION, MAX_IMAGE - listBitmap.size)
        startActivityForResult(intent, RESULT_CODE_IMAGE)
    }

    private fun initParamCreateAlbum() {
        val listPathServer = ArrayList<LinkImageUploadModel>()
        listBitmap.forEach {
            listPathServer.add(LinkImageUploadModel(it.pathServer)) }
        presenter.let {
            val link = HashMap<String, Any>()
            link[Constants.PARAMS_LINK] = listPathServer
            val map = HashMap<String, Any>()
            map[Constants.PARAMS_CODE] = imageCode
            map[Constants.PARAMS_CREATE_BY_LOW] = getSharePreferences().accountName
            map[Constants.PARAMS_STATUS_LOW] = DEFAULT_STATUS_CREATE_IAMGE
            map[Constants.PARAMS_IMAGES] = listPathServer
            it.postCreateImage(map)
        }
    }

    private fun initParamAddImage() {
        for (i in 0 until listBitmap.size) {
            presenter.let {
                val item = listBitmap[i]
                val link = HashMap<String, Any>()
                val map = HashMap<String, Any>()
                map[Constants.PARAMS_CODE] = imageCode
                link[Constants.PARAMS_LINK] = item.pathServer
                map[Constants.PARAMS_CREATE_BY_LOW] = getSharePreferences().accountName
                it.postAddImage(map)
            }
        }
    }

    override fun loadUploadImageToServer(response: Int) {
        if (response == listBitmap.size) {
            mDialogDownload.dismiss()
            showLoading()
            initParamCreateAlbum()
        } else
            mDialogDownload.setPercentUpload(response, listBitmap.size)
    }

    override fun loadCreateImage(response: ResponseModel) {
        when (response.ResponseResult.ErrorCode) {
            Constants.CREATE_SUCCESS -> {
                hideLoading()
                clearDataImage()
                AppUtils.showDialog(fragmentManager, content = getString(R.string.upload_image_success, listBitmap.size), confirmDialogInterface = object : ConfirmDialogInterface {
                    override fun onClickOk() {
                        clearAllBackStack()
                    }

                    override fun onClickCancel() {

                    }
                })
            }
            IMAGE_CODE_EXSIT -> initParamAddImage()
            else -> {
                hideLoading()
                clearDataImage()
                AppUtils.showDialog(fragmentManager, content = response.ResponseResult.Message, confirmDialogInterface = null)
            }
        }
    }

    private fun clearDataImage() {
        if (listBitmap.size != 0) {
            listBitmap.clear()
            adapterImage.notifyDataSetChanged()
        }
    }

    override fun loadAddImage(response: ResponseModel) {
        countAddImage++
        if (countAddImage != listBitmap.size)
            initParamAddImage()
        else {
            hideLoading()
            clearDataImage()
            AppUtils.showDialog(fragmentManager, content = getString(R.string.upload_image_success, listBitmap.size), confirmDialogInterface = object : ConfirmDialogInterface {
                override fun onClickOk() {
                    clearAllBackStack()
                }

                override fun onClickCancel() {

                }
            })
        }
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
}