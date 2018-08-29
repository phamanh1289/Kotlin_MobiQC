package mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.image.view_image

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.fragment_view_image.*
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.R
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.data.network.model.*
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.others.constant.Constants
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.base.BaseFragment
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.image.upload_image.diff.UploadImageAdapter
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.image.view_image_detail.ViewImageDetailFragment
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.utils.AppUtils
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.utils.KeyboardUtils
import javax.inject.Inject

/**
 * * Created by Anh Pham on 08/09/2018.     **
 * * Copyright (c) 2018 by FPT Telecom      **
 */
class ViewImageFragment : BaseFragment(), ViewImageContract.ViewImageView {
    @Inject
    lateinit var presenter: ViewImagePresenter
    private var idImage = 0
    private var codeImage = ""
    private var listUrlImage = ArrayList<UploadImageModel>()
    private lateinit var adapterImage: UploadImageAdapter

    companion object {
        fun newInstance(id: Int, code: String): ViewImageFragment {
            val args = Bundle()
            args.putInt(Constants.ARG_SUPID, id)
            args.putString(Constants.ARG_IMAGE_CODE, code)
            val fragment = ViewImageFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_view_image, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getActivityComponent().inject(this)
        presenter.onAttach(this)
        activity?.let { KeyboardUtils.setupUI(view, activity = it) }
        initView()
    }

    private fun initView() {
        handleArgument()
        initViewListImage()
        setTitle(TitleAndMenuModel(title = getString(R.string.view_image), status = false))
    }

    private fun handleArgument() {
        arguments?.let {
            idImage = it.getInt(Constants.ARG_SUPID)
            codeImage = it.getString(Constants.ARG_IMAGE_CODE) ?: ""
            showLoading()
            presenter.getAlbumCode(getSharePreferences().userToken, codeImage)
        }
    }

    private fun handleDataAlbumCode(data: ResponseResultModel) {
        if (data.ErrorCode == Constants.REQUEST_TOKEN_SUCCESS) {
            val listImage: ArrayList<ResultImageModel> = Gson().fromJson(Gson().toJson(data.Results), object : TypeToken<ArrayList<ResultImageModel>>() {}.type)
            for (i in 0 until listImage.size) {
                //Quantity image : 200 up to 480
                listUrlImage.add(UploadImageModel(filePath = AppUtils.getUrlImage(listImage[i].link).replace("200", "480")))
            }
            adapterImage.notifyDataSetChanged()
            fragViewImage_tvNoData.visibility = if (listUrlImage.size != 0) View.GONE else View.VISIBLE
        } else
            AppUtils.showDialog(fragmentManager, content = data.Message, confirmDialogInterface = null)
        hideLoading()
    }

    private fun initViewListImage() {
        adapterImage = UploadImageAdapter(Constants.TYPE_IMAGE_VIEW) {
            addFragment(ViewImageDetailFragment.newInstance(listUrlImage[it].filePath), true, true)
        }
        adapterImage.submitList(listUrlImage)
        fragViewImage_rvMain.apply {
            adapter = adapterImage
            val layout = LinearLayoutManager(context)
            layout.orientation = LinearLayoutManager.VERTICAL
            layoutManager = layout
            setHasFixedSize(true)
        }
    }

    override fun loadAlbumCode(response: ResponseModel) {
        handleDataAlbumCode(response.ResponseResult)
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