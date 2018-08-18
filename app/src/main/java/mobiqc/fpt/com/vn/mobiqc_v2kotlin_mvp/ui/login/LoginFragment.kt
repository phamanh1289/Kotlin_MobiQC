package mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.login

import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_login.*
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.R
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.data.network.model.ResponseAccountGroupModel
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.data.network.model.ResponseModel
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.others.constant.Constants
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.base.BaseFragment
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.utils.AppUtils
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.utils.KeyboardUtils
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.utils.StartActivityUtils
import javax.inject.Inject

/**
 * * Created by Anh Pham on 08/02/2018.     **
 * * Copyright (c) 2018 by FPT Telecom      **
 */
class LoginFragment : BaseFragment(), LoginContract.LoginView {

    @Inject
    lateinit var presenter: LoginPresenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getActivityComponent().inject(this)
        presenter.onAttach(this)
        activity?.let { KeyboardUtils.setupUI(view, activity = it) }
        initView()
    }

    private fun initView() {
        fragLogin_tvSubmit.setOnClickListener {
            if (isValidate()) {
                showLoading()
                val map = HashMap<String, Any>()
                map[Constants.PARAMS_USER_NAME_NON_CAPWORD] = fragLogin_tvUser.text.toString()
                map[Constants.PARAMS_PASSWORD] = fragLogin_tvPass.text.toString()
                map[Constants.PARAMS_IMEI] = "358548066496528"
//                map["Imei"] = "352111083547090"
                presenter.postLogin(map)
            }
        }
        fragLogin_cbShowPass.setOnCheckedChangeListener { _, isCheck ->
            fragLogin_tvPass.transformationMethod =
                    if (isCheck) HideReturnsTransformationMethod.getInstance()
                    else PasswordTransformationMethod.getInstance()
        }
    }

    private fun isValidate(): Boolean {
        var error = ""
        val idUser = fragLogin_tvUser.text
        val pass = fragLogin_tvPass.text
        if (idUser.isBlank())
            error = getString(R.string.error_acc_empty)
//        else if (idUser.isBlank())
//            error = getString(R.string.error_acc_validate)
        else if (pass.isBlank())
            error = getString(R.string.error_pass_empty)
        else if (pass.length < 4)
            error = getString(R.string.error_pass_validate)
        if (error.isNotBlank())
            AppUtils.showDialog(fragmentManager, content = error, confirmDialogInterface = null)
        return error.isBlank()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.onDetach()
    }

    override fun loadLogin(response: ResponseModel) {
        if (response.Code == Constants.REQUEST_SUCCESS) {
            val map = HashMap<String, Any>()
            val userName = fragLogin_tvUser.text.toString()
            map[Constants.PARAMS_USER_NAME] = userName
            getSharePreferences().accountName = userName
            getSharePreferences().createDate = AppUtils.getCurrentDate(Constants.CURRENT_DATE)
            presenter.postMobiAccount(map)
        } else {
            hideLoading()
            AppUtils.showDialog(fragmentManager, content = response.Description, confirmDialogInterface = null)
        }
    }

    override fun loadMobiAccount(response: ResponseAccountGroupModel) {
        hideLoading()
        if (response.code == Constants.REQUEST_SUCCESS)
            getSharePreferences().mobiAccount = Gson().toJson(response.data)
        activity?.let { it ->
            StartActivityUtils().toMainActivity(it)
            it.finish()
        }
    }

    override fun handleError(response: String) {
        hideLoading()
        AppUtils.showDialog(fragmentManager, content = response, confirmDialogInterface = null)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onDetach()
    }
}

