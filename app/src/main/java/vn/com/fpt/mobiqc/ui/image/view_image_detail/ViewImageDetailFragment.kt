package vn.com.fpt.mobiqc.ui.image.view_image_detail

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.request.animation.GlideAnimation
import com.bumptech.glide.request.target.SimpleTarget
import it.sephiroth.android.library.imagezoom.ImageViewTouchBase
import kotlinx.android.synthetic.main.fragment_view_image_detail.*
import vn.com.fpt.mobiqc.R
import vn.com.fpt.mobiqc.others.constant.Constants
import vn.com.fpt.mobiqc.ui.base.BaseFragment
import vn.com.fpt.mobiqc.utils.AppUtils
import vn.com.fpt.mobiqc.utils.KeyboardUtils

/**
 * * Created by Anh Pham on 08/09/2018.     **
 * * Copyright (c) 2018 by FPT Telecom      **
 */
class ViewImageDetailFragment : BaseFragment() {

    private var imageModel = ""
    private var typeImage = 0

    companion object {
        const val MIN_ZOOM = 2f
        const val MAX_ZOOM = 4f
        fun newInstance(typeImage: Int, urlImage: String): ViewImageDetailFragment {
            val args = Bundle()
            args.putInt(Constants.ARG_IMAGE_TYPE, typeImage)
            args.putString(Constants.ARG_IMAGE_CODE, urlImage)
            val fragment = ViewImageDetailFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_view_image_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.let { KeyboardUtils.setupUI(view, activity = it) }
        handleArgument()
    }

    private fun handleArgument() {
        arguments?.let {
            imageModel = it.getString(Constants.ARG_IMAGE_CODE) ?: ""
            typeImage = it.getInt(Constants.ARG_IMAGE_TYPE)
        }
        fragViewImageDetail_imgView.displayType = ImageViewTouchBase.DisplayType.FIT_IF_BIGGER
        imageModel = if (typeImage == Constants.TYPE_OTHER_IMAGE) getString(R.string.url_image_ftel) + imageModel else imageModel
        if (AppUtils.isURL(imageModel)) {
            showLoading()
            Glide.with(context)
                    .load(imageModel)
                    .asBitmap()
                    .into(object : SimpleTarget<Bitmap>() {
                        override fun onResourceReady(resource: Bitmap?, glideAnimation: GlideAnimation<in Bitmap>?) {
                            fragViewImageDetail_imgView.setImageBitmap(resource, null, MIN_ZOOM, MAX_ZOOM)
                            hideLoading()
                        }
                    })
        } else {
            fragViewImageDetail_imgView.setScaleEnabled(false)
            fragViewImageDetail_imgView.doubleTapEnabled = false
            fragViewImageDetail_imgView.setImageResource(R.drawable.img_no_image)
        }
    }

}