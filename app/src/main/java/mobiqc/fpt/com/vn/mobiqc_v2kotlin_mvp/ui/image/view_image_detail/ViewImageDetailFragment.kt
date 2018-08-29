package mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.image.view_image_detail

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
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.R
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.others.constant.Constants
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.base.BaseFragment
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.utils.KeyboardUtils

/**
 * * Created by Anh Pham on 08/09/2018.     **
 * * Copyright (c) 2018 by FPT Telecom      **
 */
class ViewImageDetailFragment : BaseFragment() {

    private var imageModel = ""

    companion object {
        const val MIN_ZOOM = 2f
        const val MAX_ZOOM = 4f
        fun newInstance(model: String): ViewImageDetailFragment {
            val args = Bundle()
            args.putString(Constants.ARG_IMAGE_CODE, model)
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
        initView()
    }

    private fun initView() {
        handleArgument()
    }

    private fun handleArgument() {
        arguments?.let {
            imageModel = it.getString(Constants.ARG_IMAGE_CODE) ?: ""
        }
        Glide.with(context)
                .load(imageModel)
                .asBitmap()
                .into(object : SimpleTarget<Bitmap>() {
                    override fun onResourceReady(resource: Bitmap?, glideAnimation: GlideAnimation<in Bitmap>?) {
                        fragViewImageDetail_imgView.displayType = ImageViewTouchBase.DisplayType.FIT_IF_BIGGER
                        fragViewImageDetail_imgView.setImageBitmap(resource, null, MIN_ZOOM, MAX_ZOOM)
                    }
                })
    }

}