package mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.error.create

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.R
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.others.constant.Constants
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.base.BaseFragment
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.utils.KeyboardUtils


/**
 * * Created by Anh Pham on 08/09/2018.     **
 * * Copyright (c) 2018 by FPT Telecom      **
 */
class CreateErrorFragment : BaseFragment() {



    companion object {
        fun newInstance(title: String): CreateErrorFragment {
            val args = Bundle()
            args.putString(Constants.ARG_TITLE, title)
            val fragment = CreateErrorFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_error, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        getActivityComponent().inject(this)
//        presenter.onAttach(this)
        activity?.let { KeyboardUtils.setupUI(view, activity = it) }
        initView()
    }

    private fun initView() {
    }

    override fun onDestroy() {
        super.onDestroy()
//        presenter.onDetach()
    }
}