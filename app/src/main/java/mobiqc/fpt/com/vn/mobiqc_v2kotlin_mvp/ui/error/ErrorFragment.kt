package mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.error

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import kotlinx.android.synthetic.main.fragment_error.*
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.R
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.data.network.model.SingleChoiceModel
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.data.realm.error.ErrorRealmManager
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.others.constant.Constants
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.others.datacore.DataCore
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.base.BaseFragment
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.utils.AppUtils
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.utils.KeyboardUtils
import java.util.*
import javax.inject.Inject


/**
 * * Created by Anh Pham on 08/09/2018.     **
 * * Copyright (c) 2018 by FPT Telecom      **
 */
class ErrorFragment : BaseFragment(), ErrorContract.ErrorView {
    @Inject
    lateinit var presenter: ErrorPresenter

    private lateinit var listTypeKS: ArrayList<SingleChoiceModel>
    private lateinit var listIndoor: ArrayList<SingleChoiceModel>
    private lateinit var listUserKS: ArrayList<SingleChoiceModel>
    private lateinit var listTypeError: ArrayList<SingleChoiceModel>
    private lateinit var listMainError: ArrayList<SingleChoiceModel>
    private lateinit var listDescription: ArrayList<SingleChoiceModel>
    var positionUserKS = 0
    var positionTypeError = 0
    var positionMainError = 0
    private var objName = ""

    companion object {
        fun newInstance(type: String): ErrorFragment {
            val args = Bundle()
            args.putString(Constants.ARG_OBJID, type)
            val fragment = ErrorFragment()
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
        getDataBundle(arguments)
        listTypeKS = DataCore.getListTypeKS(context)
        fragError_tvTypeKS.text = listTypeKS[Constants.FIRST_ITEM].account
        listIndoor = DataCore.getListIndoor(context)
        fragError_tvIndoor.text = listIndoor[Constants.FIRST_ITEM].account
        setAllData()
        initOnClick()
    }

    private fun getDataBundle(arguments: Bundle?) {
        arguments?.let {
            objName = it.getString(Constants.ARG_OBJID)
            fragError_tvNumber.text = objName
        }
    }

    fun setAllData() {
        setDataTypeKS()
        setDataTypeError()
        setDataMainError()
        setDataDescription()
    }

    fun setDataTypeKS() {
        listUserKS = ErrorRealmManager().getDistinctDepartment()
        fragError_tvUserKS.text = listUserKS[Constants.FIRST_ITEM].account
    }

    fun setDataTypeError() {
        listTypeError = ErrorRealmManager().getDistinctTypeError(listUserKS[positionUserKS].account)
        fragError_tvTypeError.text = listTypeError[Constants.FIRST_ITEM].account
    }

    fun setDataMainError() {
        listMainError = ErrorRealmManager().getDistinctMainError(listUserKS[positionUserKS].account, listTypeError[positionTypeError].account)
        fragError_tvMainError.text = listMainError[Constants.FIRST_ITEM].account
    }

    fun setDataDescription() {
        listDescription = ErrorRealmManager().getDistinctDescription(listUserKS[positionUserKS].account, listTypeError[positionTypeError].account, listMainError[positionMainError].account)
        fragError_tvDescription.text = listDescription[Constants.FIRST_ITEM].account
    }

    private fun initOnClick() {
        fragError_tvTypeKS.setOnClickListener { AppUtils.showDialogSingChoice(fragmentManager, getString(R.string.error_detail_type), listTypeKS, fragError_tvTypeKS) }
        fragError_tvIndoor.setOnClickListener { AppUtils.showDialogSingChoice(fragmentManager, getString(R.string.error_detail_main_indoor), listIndoor, fragError_tvIndoor) }
        fragError_tvUserKS.setOnClickListener {
            AppUtils.showDialogSingChoice(fragmentManager, getString(R.string.error_detail_user), listUserKS, fragError_tvUserKS)
        }
        fragError_tvTypeError.setOnClickListener {
            AppUtils.showDialogSingChoice(fragmentManager, getString(R.string.error_detail_type_error), listTypeError, fragError_tvTypeError)
        }
        fragError_tvMainError.setOnClickListener {
            AppUtils.showDialogSingChoice(fragmentManager, getString(R.string.error_detail_main_error), listMainError, fragError_tvMainError)
        }
        fragError_tvDescription.setOnClickListener {
            AppUtils.showDialogSingChoice(fragmentManager, getString(R.string.error_detail_description), listDescription, fragError_tvDescription)
        }
        fragError_tvMoney.onChange { }
        fragError_tvSubmit.setOnClickListener { }
    }

    private fun EditText.onChange(cb: (String) -> Unit) {
        this.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                fragError_tvMoney.removeTextChangedListener(this)
                try {
                    fragError_tvMoney.setText(AppUtils.autoInsertDot(s.toString()))
                    fragError_tvMoney.setSelection(fragError_tvMoney.text.length)
                } catch (e: NumberFormatException) {
                    e.printStackTrace()
                }
                fragError_tvMoney.addTextChangedListener(this)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    override fun handleError(error: String) {
        hideLoading()
        AppUtils.showDialog(fragmentManager, content = error, confirmDialogInterface = null)
    }
}