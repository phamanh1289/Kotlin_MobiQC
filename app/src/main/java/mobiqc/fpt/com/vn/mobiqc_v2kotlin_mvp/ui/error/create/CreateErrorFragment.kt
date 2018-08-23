package mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.error.create

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_create_error.*
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.R
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.data.interfaces.ConfirmDialogInterface
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.data.network.model.ResponseModel
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.data.network.model.SingleChoiceModel
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.data.network.model.TitleAndMenuModel
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.data.realm.infrastructure.InfrastructureRealmManager
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.data.realm.partner.PartnerRealmManager
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.others.constant.Constants
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.base.BaseFragment
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.image.UploadImageFragment
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.utils.AppUtils
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.utils.KeyboardUtils
import javax.inject.Inject


/**
 * * Created by Anh Pham on 08/09/2018.     **
 * * Copyright (c) 2018 by FPT Telecom      **
 */
class CreateErrorFragment : BaseFragment(), CreateErrorContract.CreateErrorView {

    @Inject
    lateinit var presenter: CreateErrorPresenter

    private var listParent = ArrayList<SingleChoiceModel>()
    private var listZone = ArrayList<SingleChoiceModel>()
    private var listBranch = ArrayList<SingleChoiceModel>()
    private var listDescription = ArrayList<SingleChoiceModel>()

    private var imageCode = ""
    private var positionParent = 0
    private var positionZone = 0
    private var positionBranch = 0
    private var positionDescription = 0
    //True -> đài trạm, false -> ngoại vi
    private var typeError = false
    //True -> chưa xử lý, false -> đã xử lý
    private var typeStatusError = false

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
        return inflater.inflate(R.layout.fragment_create_error, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getActivityComponent().inject(this)
        presenter.onAttach(this)
        activity?.let { KeyboardUtils.setupUI(view, activity = it) }
        initView()
    }

    private fun initView() {
        setTitle(TitleAndMenuModel(title = getString(R.string.menu_infrastructure_update_error), status = true, image = R.drawable.ic_notifications))
        initOnClick()
        handleStatusTypeError()
        getAllDataList()
    }

    fun setDefaultValueIndex(view: Int, index: Int) {
        when (view) {
            R.id.fragCreateError_tvZone -> {
                positionZone = index
                getDataBranch()
                positionBranch = Constants.FIRST_ITEM
                getDataPartner()
                positionParent = Constants.FIRST_ITEM
            }
            R.id.fragCreateError_tvBranch -> {
                positionBranch = index
                getDataPartner()
                positionParent = Constants.FIRST_ITEM
            }
            R.id.fragCreateError_tvPartner -> {
                positionParent = index
            }
        }
    }

    private fun initOnClick() {
        fragCreateError_llStation.setOnClickListener {
            handleStatusTypeError()
        }
        fragCreateError_llPeripheral.setOnClickListener {
            handleStatusTypeError()
        }
        fragCreateError_llDoneError.setOnClickListener {
            typeStatusError = !typeStatusError
            fragCreateError_imgDoneError.isSelected = typeStatusError
            fragCreateError_tvDoneError.isSelected = typeStatusError
        }
        fragCreateError_tvZone.setOnClickListener { AppUtils.showDialogSingChoice(fragmentManager, getString(R.string.search_location), listZone, fragCreateError_tvZone, positionZone) }
        fragCreateError_tvBranch.setOnClickListener { AppUtils.showDialogSingChoice(fragmentManager, getString(R.string.search_branch), listBranch, fragCreateError_tvBranch, positionBranch) }
        fragCreateError_tvPartner.setOnClickListener { AppUtils.showDialogSingChoice(fragmentManager, getString(R.string.supporter_error), listParent, fragCreateError_tvPartner, positionParent) }
        fragCreateError_tvDescription.setOnClickListener { AppUtils.showDialogSingChoice(fragmentManager, getString(R.string.error_detail_description), listDescription, fragCreateError_tvDescription, positionDescription) }
        fragCreateError_tvSubmit.setOnClickListener {
            addFragment(UploadImageFragment(), true, true)
//            AppUtils.showDialog(fragmentManager, content = getString(R.string.create_error_mess), confirmDialogInterface = object : ConfirmDialogInterface {
//                override fun onClickOk() {
//                    initParamUpDateError()
//                }
//
//                override fun onClickCancel() {
//
//                }
//            })
        }
    }

    private fun getAllDataList() {
        if (PartnerRealmManager().getCountPartner() == 0)
            PartnerRealmManager().importFromJson(resources)
        getDataZone()
        getDataBranch()
        getDataPartner()
    }

    private fun getDescriptionInfraStruct(type: String) {
        listDescription = InfrastructureRealmManager().getDescription(type)
        listDescription[positionDescription].let {
            fragCreateError_tvDescription.text = it.account
        }
    }

    private fun getTypeInfraStruct(type: Boolean): String {
        return getString(if (type) R.string.error_station else R.string.error_peripheral)
    }

    private fun getDataPartner() {
        listParent = PartnerRealmManager().getPartner(listZone[positionZone].account, listBranch[positionBranch].account)
        listParent[Constants.FIRST_ITEM].let {
            fragCreateError_tvPartner.text = it.account
        }
    }

    private fun getDataBranch() {
        listBranch = PartnerRealmManager().getBranch(listZone[positionZone].account)
        listBranch[Constants.FIRST_ITEM].let {
            fragCreateError_tvBranch.text = it.account
        }
    }

    private fun getDataZone() {
        listZone = PartnerRealmManager().getZone()
        listZone[Constants.FIRST_ITEM].let {
            fragCreateError_tvZone.text = it.account
        }
    }

    private fun handleStatusTypeError() {
        typeError = !typeError
        fragCreateError_imgStation.isSelected = typeError
        fragCreateError_tvStation.isSelected = typeError
        fragCreateError_imgPeripheral.isSelected = !typeError
        fragCreateError_tvPeripheral.isSelected = !typeError
        if (InfrastructureRealmManager().getCountInfrast() == 0)
            InfrastructureRealmManager().importFromJson(resources)
        getDescriptionInfraStruct(getTypeInfraStruct(typeError))
    }

    private fun initParamUpDateError() {
        presenter.let {
            imageCode = AppUtils.generateImageCode(listBranch[positionBranch].account)
            val subMap = HashMap<String, Any>()
            subMap[Constants.PARAMS_AREA] = listZone[positionZone].account
            subMap[Constants.PARAMS_BRANCH] = listBranch[positionBranch].account
            subMap[Constants.PARAMS_TYPE] = getTypeInfraStruct(typeError)
            subMap[Constants.PARAMS_ELEMENT] = fragCreateError_tvSubError.text.toString()
            subMap[Constants.PARAMS_DESCRIPTION] = listDescription[positionDescription].account
            subMap[Constants.PARAMS_PARTNER] = listParent[positionParent].account
            subMap[Constants.PARAMS_NOTE] = fragCreateError_tvNote.text.toString()
            subMap[Constants.PARAMS_IMAGE_CODE] = imageCode
            subMap[Constants.PARAMS_MAIL_TO] = ""
            subMap[Constants.PARAMS_STATUS] = if (fragCreateError_llDoneError.isSelected) Constants.DONE_STATUS_ERROR else Constants.YET_STATUS_ERROR
            subMap[Constants.PARAMS_CREATE_BY] = getSharePreferences().accountName
            val map = HashMap<String, Any>()
            map[Constants.PARAMS_ERROR] = subMap
            it.postInsertErrorInfrastructure(map)
        }
    }

    private fun handleRequestSuccess() {
        AppUtils.showDialog(fragmentManager, content = getString(R.string.create_error_success), confirmDialogInterface = object : ConfirmDialogInterface {
            override fun onClickOk() {
                addFragment(UploadImageFragment.newInstance(imageCode), true, true)
            }

            override fun onClickCancel() {

            }
        })
    }

    private fun clearDataWhenRequestSuccess() {
        fragCreateError_tvNote.setText("")
        fragCreateError_tvSubError.setText("")
        fragCreateError_llDoneError.isSelected = false
    }

    override fun loadInsertErrorInfrastructure(response: ResponseModel) {
        if (response.Code == Constants.REQUEST_SUCCESS) {
            handleRequestSuccess()
        } else
            AppUtils.showDialog(fragmentManager, content = response.Description, confirmDialogInterface = null)
        clearDataWhenRequestSuccess()
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