package mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.image

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_upload_image.*
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.R
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.data.network.model.TitleAndMenuModel
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.data.network.model.UploadImageModel
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.others.constant.Constants
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.base.BaseFragment
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.image.diff.UploadImageAdapter
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.utils.AppUtils
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.utils.KeyboardUtils
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
        const val MAX_IMAGE = 6
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
                pickImage()
            else
                AppUtils.showDialog(fragmentManager, content = getString(R.string.max_pick_image), confirmDialogInterface = null)
        }
        fragUpLoadImage_imgUpImage.setOnClickListener { }
    }

    private fun pickImage() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(intent, RESULT_CODE_IMAGE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == RESULT_CODE_IMAGE) {
            data?.let { intent ->
                val bitmap = AppUtils.getBitmapFromData(context, intent)
                bitmap?.let { bit ->
                    listBitmap.add(UploadImageModel(bitmap = bit))
                    adapterImage.notifyItemInserted(listBitmap.size - 1)
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