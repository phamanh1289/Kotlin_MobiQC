package vn.com.fpt.mobiqc.ui.image.upload_image

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.erikagtierrez.multiple_media_picker.Gallery
import kotlinx.android.synthetic.main.fragment_upload_image.*
import vn.com.fpt.mobiqc.R
import vn.com.fpt.mobiqc.data.interfaces.ConfirmDialogInterface
import vn.com.fpt.mobiqc.data.network.model.LinkImageUploadModel
import vn.com.fpt.mobiqc.data.network.model.ResponseModel
import vn.com.fpt.mobiqc.data.network.model.TitleAndMenuModel
import vn.com.fpt.mobiqc.data.network.model.UploadImageModel
import vn.com.fpt.mobiqc.others.constant.Constants
import vn.com.fpt.mobiqc.others.dialog.ShowDownLoadDialog
import vn.com.fpt.mobiqc.ui.base.BaseFragment
import vn.com.fpt.mobiqc.ui.image.upload_image.diff.UploadImageAdapter
import vn.com.fpt.mobiqc.utils.AppUtils
import vn.com.fpt.mobiqc.utils.KeyboardUtils
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
        //        const val TYPE_IMAGE_AND_VIDEO = 1
        const val TYPE_IMAGE = 2
        //        const val TYPE_VIDEO = 3
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
                AppUtils.showDialog(fragmentManager, content = getString(R.string.mess_notify_upload_image, listBitmap.size),actionCancel = true, confirmDialogInterface = object : ConfirmDialogInterface {
                    override fun onClickOk() {
                        AppUtils.showDialogDownLoadData(fragmentManager, mDialogDownload)
                        presenter.postUploadImage(context = context, token = getSharePreferences().userToken, list = listBitmap)
                    }

                    override fun onClickCancel() {

                    }
                })
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
            listPathServer.add(LinkImageUploadModel(it.pathServer))
        }
        presenter.let {
            val link = HashMap<String, Any>()
            link[Constants.PARAMS_LINK] = listPathServer
            val map = HashMap<String, Any>()
            map[Constants.PARAMS_CODE] = imageCode
            map[Constants.PARAMS_CREATE_BY_LOW] = getSharePreferences().accountName
            map[Constants.PARAMS_STATUS_LOW] = DEFAULT_STATUS_CREATE_IAMGE
            map[Constants.PARAMS_IMAGES] = listPathServer
            it.postCreateImage(getSharePreferences().userToken, map)
        }
    }

    private fun initParamAddImage() {
        for (i in 0 until listBitmap.size) {
            presenter.let {
                val item = listBitmap[i]
                val map = HashMap<String, Any>()
                map[Constants.PARAMS_CODE] = imageCode
                map[Constants.PARAMS_LINK] = item.pathServer
                map[Constants.PARAMS_UPDATE_BY_LOW] = getSharePreferences().accountName
                it.postAddImage(getSharePreferences().userToken, map)
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
                AppUtils.showDialog(fragmentManager, content = getString(R.string.upload_image_success, listBitmap.size), confirmDialogInterface = object : ConfirmDialogInterface {
                    override fun onClickOk() {
                        clearDataImage()
                        clearAllBackStack()
                    }

                    override fun onClickCancel() {

                    }
                })
            }
            IMAGE_CODE_EXSIT -> initParamAddImage()
            else -> {
                hideLoading()
                AppUtils.showDialog(fragmentManager, content = response.ResponseResult.Message, confirmDialogInterface = null)
                clearDataImage()
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
            AppUtils.showDialog(fragmentManager, content = getString(R.string.upload_image_success, listBitmap.size), confirmDialogInterface = object : ConfirmDialogInterface {
                override fun onClickOk() {
                    clearDataImage()
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