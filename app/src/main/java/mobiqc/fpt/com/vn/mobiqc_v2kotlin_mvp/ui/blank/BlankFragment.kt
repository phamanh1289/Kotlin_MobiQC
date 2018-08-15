package mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.blank

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.R
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.others.constant.Constants
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.base.BaseFragment
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.utils.AppUtils
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.utils.KeyboardUtils
import javax.inject.Inject

/**
 * * Created by Anh Pham on 08/09/2018.     **
 * * Copyright (c) 2018 by FPT Telecom      **
 */
class BlankFragment : BaseFragment(), BlankContract.View {
    @Inject
    lateinit var presenter: BlankPresenter

    companion object {
        fun newInstance(type: Int): BlankFragment {
            val args = Bundle()
            args.putInt(Constants.ARG_TYPE_CONTRACT, type)
            val fragment = BlankFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_detail_contract, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        getActivityComponent().inject(this)
        presenter.onAttach(this)
        activity?.let { KeyboardUtils.setupUI(view, activity = it) }
        initView()
    }

    private fun initView() {

    }

    override fun handleError(error: String) {
        hideLoading()
        AppUtils.showDialog(fragmentManager, content = error, confirmDialogInterface = null)
    }
}