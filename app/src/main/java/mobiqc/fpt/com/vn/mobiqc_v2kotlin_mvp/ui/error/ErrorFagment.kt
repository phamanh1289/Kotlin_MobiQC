package mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.error

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_error.*
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.R
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.data.network.model.SingleChoiceModel
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.others.constant.Constants
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.others.datacore.DataCore
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.base.BaseFragment
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.utils.AppUtils
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.utils.KeyboardUtils
import javax.inject.Inject

/**
 * * Created by Anh Pham on 08/09/2018.     **
 * * Copyright (c) 2018 by FPT Telecom      **
 */
class ErrorFagment : BaseFragment(), ErrorContract.ErrorView {
    @Inject
    lateinit var presenter: ErrorPresenter

    private lateinit var listTypeKS: ArrayList<SingleChoiceModel>
    private lateinit var listIndoor: ArrayList<SingleChoiceModel>
    private lateinit var listUserKS: ArrayList<SingleChoiceModel>
    private lateinit var listGroupError: ArrayList<SingleChoiceModel>
    private lateinit var listMainError: ArrayList<SingleChoiceModel>
    private lateinit var listDescription: ArrayList<SingleChoiceModel>

    companion object {
        fun newInstance(type: Int): ErrorFagment {
            val args = Bundle()
            args.putInt(Constants.ARG_TYPE_CONTRACT, type)
            val fragment = ErrorFagment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_error, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getActivityComponent().inject(this)
        presenter.onAttach(this)
        activity?.let { KeyboardUtils.setupUI(view, activity = it) }
        initView()
    }

    private fun initView() {
        initAllListData()
    }

    private fun initAllListData() {
        listTypeKS = DataCore.getListTypeKS(context)
        listIndoor = DataCore.getListIndoor(context)
        DataCore.getListDepartment(context)
        initOnClick()
    }

    private fun initOnClick() {
        fragError_tvTypeKS.setOnClickListener { AppUtils.showDialogSingChoice(fragmentManager, getString(R.string.error_detail_type), listTypeKS, fragError_tvTypeKS) }
        fragError_tvIndoor.setOnClickListener { AppUtils.showDialogSingChoice(fragmentManager, getString(R.string.error_detail_main_indoor), listIndoor, fragError_tvTypeKS) }
        fragError_tvUserKS.setOnClickListener { }
        fragError_tvTypeError.setOnClickListener { }
        fragError_tvMainError.setOnClickListener { }
        fragError_tvDescription.setOnClickListener { }
        fragError_tvSubmit.setOnClickListener { }
    }

    override fun handleError(error: String) {
        hideLoading()
        AppUtils.showDialog(fragmentManager, content = error, confirmDialogInterface = null)
    }
}