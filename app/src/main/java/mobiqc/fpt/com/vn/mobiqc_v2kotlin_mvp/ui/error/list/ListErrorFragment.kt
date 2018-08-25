package mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.error.list

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.fragment_list_error.*
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.R
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.data.network.model.ErrorInfrastructureModel
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.data.network.model.ResponseModel
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.others.constant.Constants
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.base.BaseFragment
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.error.list.diff.ListErrorAdapter
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.utils.AppUtils
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.utils.KeyboardUtils
import javax.inject.Inject


/**
 * * Created by Anh Pham on 08/09/2018.     **
 * * Copyright (c) 2018 by FPT Telecom      **
 */
class ListErrorFragment : BaseFragment(), ListErrorContract.ListErrorView {

    @Inject
    lateinit var presenter: ListErrorPresenter

    private var listError = ArrayList<ErrorInfrastructureModel>()
    private lateinit var adapterError: ListErrorAdapter

    companion object {
        fun newInstance(title: String): ListErrorFragment {
            val args = Bundle()
            args.putString(Constants.ARG_TITLE, title)
            val fragment = ListErrorFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_list_error, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getActivityComponent().inject(this)
        presenter.onAttach(this)
        activity?.let { KeyboardUtils.setupUI(view, activity = it) }
        initView()
    }

    private fun initView() {
        fragListError_tvToDate.setText(AppUtils.getCurrentDate(Constants.CURRENT_DATE))
        fragListError_tvFromDate.setText(AppUtils.getCurrentDate(Constants.CURRENT_DATE))
        initOnClick()
        requestDataError()
    }

    private fun initViewError() {
        adapterError = ListErrorAdapter { }
        adapterError.submitList(listError)
        fragListError_rvMain.apply {
            val layout = LinearLayoutManager(context)
            layout.orientation = LinearLayoutManager.VERTICAL
            layoutManager = layout
            adapter = adapterError
            setHasFixedSize(true)
            visibility = View.VISIBLE
        }
    }

    fun requestDataError() {
        presenter.let {
            showLoading()
            val map = HashMap<String, Any>()
            map[Constants.PARAMS_USER_NAME_NON_CAPWORD] = getSharePreferences().accountName
            map[Constants.PARAMS_FROM_DATE] = AppUtils.toConvertDateFormat(context, fragListError_tvFromDate.text.toString())
            map[Constants.PARAMS_TO_DATE] = AppUtils.toConvertDateFormat(context, fragListError_tvToDate.text.toString())
            it.getErrorInfrastructure(map)
        }
    }

    private fun initOnClick() {
        fragListError_tvToDate.setOnClickListener { AppUtils.showPickTime(context, fragListError_tvToDate, Constants.SET_MAX_DATE) }
        fragListError_tvFromDate.setOnClickListener { AppUtils.showPickTime(context, fragListError_tvFromDate, Constants.SET_MAX_DATE) }
        fragListError_tvToDate.onChange { it }
        fragListError_tvFromDate.onChange { it }
    }

    private fun handleDataError(list: ArrayList<ErrorInfrastructureModel>) {
        if (list.size != 0) {
            listError = list
            initViewError()
        }
        fragListError_tvNoData.visibility = if (list.size != 0) View.GONE else View.VISIBLE
        hideLoading()
    }

    override fun loadErrorInfrastructure(response: ResponseModel) {
        if (response.Code == Constants.REQUEST_SUCCESS)
            handleDataError(Gson().fromJson(Gson().toJson(response.Data), object : TypeToken<ArrayList<ErrorInfrastructureModel>>() {}.type))
        else AppUtils.showDialog(fragmentManager, content = response.Description, confirmDialogInterface = null)
    }


    override fun handleError(error: String) {
        hideLoading()
        AppUtils.showDialog(fragmentManager, content = error, confirmDialogInterface = null)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onDetach()
    }


    private fun EditText.onChange(cb: (String) -> Unit) {
        this.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (AppUtils.compareDate(fragListError_tvFromDate.text.toString(), fragListError_tvToDate.text.toString()))
                    requestDataError()
                else AppUtils.showDialog(fragmentManager, content = getString(R.string.error_date), confirmDialogInterface = null)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }
}