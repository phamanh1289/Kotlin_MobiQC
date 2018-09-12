package vn.com.fpt.mobiqc.ui.error.list

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
import vn.com.fpt.mobiqc.R
import vn.com.fpt.mobiqc.data.interfaces.ConfirmDialogInterface
import vn.com.fpt.mobiqc.data.interfaces.MenuCheckListDialogInterface
import vn.com.fpt.mobiqc.data.network.model.*
import vn.com.fpt.mobiqc.data.realm.partner.PartnerRealmManager
import vn.com.fpt.mobiqc.others.constant.Constants
import vn.com.fpt.mobiqc.others.datacore.DataCore
import vn.com.fpt.mobiqc.others.dialog.SendEmailDialog
import vn.com.fpt.mobiqc.ui.base.BaseFragment
import vn.com.fpt.mobiqc.ui.error.create.CreateErrorFragment
import vn.com.fpt.mobiqc.ui.error.list.diff.ListErrorAdapter
import vn.com.fpt.mobiqc.ui.image.view_image.ViewImageFragment
import vn.com.fpt.mobiqc.utils.AppUtils
import vn.com.fpt.mobiqc.utils.KeyboardUtils
import javax.inject.Inject


/**
 * * Created by Anh Pham on 08/09/2018.     **
 * * Copyright (c) 2018 by FPT Telecom      **
 */
class ListErrorFragment : BaseFragment(), ListErrorContract.ListErrorView, MenuCheckListDialogInterface {

    @Inject
    lateinit var presenter: ListErrorPresenter

    private var listError = ArrayList<ErrorInfrastructureModel>()
    lateinit var adapterError: ListErrorAdapter
    var positionListError = 0
    private var mSendEmail = ""
    private var mCcEmail = ""
    //True : move to ViewImageFramgent, false : nothing
    private var isCheck = false

    companion object {
        const val DEFAULT_IS_HTML_BODY = 1
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
        setTitle(TitleAndMenuModel(title = arguments?.getString(Constants.ARG_TITLE)
                ?: "", status = true, image = R.drawable.ic_notifications))
        fragListError_tvToDate.setText(AppUtils.getCurrentDate(Constants.CURRENT_DATE))
        fragListError_tvFromDate.setText(AppUtils.getCurrentDate(Constants.CURRENT_DATE))
        initOnClick()
        requestDataError()
    }

    private fun initViewError() {
        adapterError = ListErrorAdapter {
            positionListError = it
            AppUtils.showMenuCheckListDialog(fragmentManager, confirmDialogInterface = this, index = it, typeOption = true)
        }
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
        fragListError_tvToDate.setOnClickListener { AppUtils.showPickTime(context, fragListError_tvToDate, Constants.SET_CURRENT_IS_MAX_DATE) }
        fragListError_tvFromDate.setOnClickListener { AppUtils.showPickTime(context, fragListError_tvFromDate, Constants.SET_CURRENT_IS_MAX_DATE) }
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

    private fun handleSendEmail() {
        presenter.let {
            val dialogSendMail = SendEmailDialog()
            val modelError = listError[positionListError]
            mCcEmail = getSharePreferences().accountName
            mSendEmail = PartnerRealmManager().getEmail(modelError.Area, modelError.Branch, modelError.Partner) ?: ""
            dialogSendMail.setDataDialog(mSendEmail, mCcEmail, object : ConfirmDialogInterface {
                override fun onClickOk() {
                    mSendEmail = dialogSendMail.getListSendMail()
                    mCcEmail = dialogSendMail.getListCcMail()
                    if (AppUtils.isValidEmail(fragmentManager, mSendEmail, context))
                        if (AppUtils.isValidEmail(fragmentManager, mCcEmail, context)) {
                            showLoading()
                            dialogSendMail.dismiss()
                            presenter.getAlbumCode(getSharePreferences().userToken, listError[positionListError].ImageCode)
                        }
                }

                override fun onClickCancel() {
                    dialogSendMail.dismiss()
                }
            })
            dialogSendMail.show(fragmentManager, SendEmailDialog::
            class.java.simpleName)
        }
    }

    //Start : sự kiện onclick menu dialog
    override fun onClickDetail(index: Int) {
        addFragment(ViewImageFragment.newInstance(listError[positionListError].ID, listError[positionListError].ImageCode), true, true)
    }

    override fun onClickError(index: Int) {
        handleSendEmail()
    }

    override fun onClickUpdateDetail(index: Int) {
        addFragment(CreateErrorFragment.newInstance(listError[index]), true, true)
    }

    override fun onClickUpdateStatus(index: Int) {
        presenter.let {
            showLoading()
            val model = listError[positionListError]
            model.Status = Constants.DONE_STATUS_ERROR
            val map = HashMap<String, Any>()
            map[Constants.PARAMS_ERROR] = model
            it.postUpdateErrorInfrastructure(map)
        }
    }
    //End : sự kiện onclick menu dialog

    override fun loadUpdateErrorInfrastructure(response: ResponseModel) {
        if (response.Code == Constants.REQUEST_SUCCESS) {
            adapterError.notifyItemChanged(positionListError)
            AppUtils.showDialog(fragmentManager, content = response.Description, confirmDialogInterface = null)
            hideLoading()
        }
    }

    private fun handleDataAlbumCode(data: ResponseResultModel) {
        if (data.ErrorCode == Constants.REQUEST_TOKEN_SUCCESS) {
            val listImage: ArrayList<ResultImageModel> = Gson().fromJson(Gson().toJson(data.Results), object : TypeToken<ArrayList<ResultImageModel>>() {}.type)
            val result = StringBuilder()
            listImage.forEach { result.append(AppUtils.toAddHtmlToImage(it.link)) }
            presenter.let {
                val model = listError[positionListError]
                val map = HashMap<String, Any>()
                map[Constants.PARAMS_TO] = mSendEmail
                map[Constants.PARAMS_MAIL_CC] = mCcEmail
                map[Constants.PARAMS_MAIL_SUBJECT] = getString(R.string.subject_email, model.ID.toString(), model.Type.toLowerCase(), model.Element)
                map[Constants.PARAMS_MAIL_BODY] = DataCore.getBaseBodyMail(model, result.toString())
                map[Constants.PARAMS_MAIL_HTML_BODY] = DEFAULT_IS_HTML_BODY
                it.postSendMail(map)
            }
        } else {
            hideLoading()
            AppUtils.showDialog(fragmentManager, content = data.Message, confirmDialogInterface = null)
        }
    }

    override fun loadSendMail(response: ResultEmailModel) {
        hideLoading()
        AppUtils.showDialog(fragmentManager, content = response.ResultDESC, confirmDialogInterface = null)
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

    private fun EditText.onChange(cb: (String) -> Unit) {
        this.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val fromDateChoice = fragListError_tvFromDate.text.toString()
                val toDateChoice = fragListError_tvToDate.text.toString()
                if (AppUtils.compareDate(fromDateChoice, toDateChoice) || (fromDateChoice == toDateChoice))
                    requestDataError()
                else
                    AppUtils.showDialog(fragmentManager, content = getString(R.string.error_date), confirmDialogInterface = null)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }
}