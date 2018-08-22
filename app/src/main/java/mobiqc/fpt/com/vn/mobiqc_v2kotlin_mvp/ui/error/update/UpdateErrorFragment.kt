package mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.error.update

import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.SphericalUtil
import kotlinx.android.synthetic.main.fragment_error.*
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.R
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.data.interfaces.ConfirmDialogInterface
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.data.network.model.ResponseModel
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.data.network.model.SingleChoiceModel
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.data.network.model.TitleAndMenuModel
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.data.realm.error.ErrorRealmManager
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.others.constant.Constants
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.others.datacore.DataCore
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.others.service.LocationService
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.base.BaseFragment
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.contract.detail_contract.DetailContractFragment
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.utils.AppUtils
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.utils.KeyboardUtils
import java.util.*
import javax.inject.Inject
import kotlin.collections.HashMap


/**
 * * Created by Anh Pham on 08/09/2018.     **
 * * Copyright (c) 2018 by FPT Telecom      **
 */
class UpdateErrorFragment : BaseFragment(), ErrorContract.ErrorView, ConfirmDialogInterface {

    @Inject
    lateinit var presenter: ErrorPresenter

    private lateinit var locationService: LocationService
    private var locationUser: Location? = null
    private lateinit var latLngGuest: LatLng
    private lateinit var latLngUser: LatLng
    private lateinit var listTypeKS: ArrayList<SingleChoiceModel>
    private lateinit var listIndoor: ArrayList<SingleChoiceModel>
    private lateinit var listUserKS: ArrayList<SingleChoiceModel>
    private lateinit var listTypeError: ArrayList<SingleChoiceModel>
    private lateinit var listMainError: ArrayList<SingleChoiceModel>
    private lateinit var listDescription: ArrayList<SingleChoiceModel>
    var positionTypeKS = 0
    var positionIndoor = 0
    var positionUserKS = 0
    var positionTypeError = 0
    var positionMainError = 0
    var positionDescription = 0
    var distanceBetween: Double = 0.0
    private var typeCheckList = 0
    private var objId = ""
    private var contractNumber = ""
    private var coordinateUser = ""
    private var coordinateGuest = ""
    private var supId = ""
    private var userUpdate = ""
    private var contractDate = ""
    //False : lần đầu mở tab -> Nhóm lỗi : không có, True : lấy item thứ 2 Nhóm lỗi
    private var checkFirstChoice = false

    companion object {
        fun newInstance(supId: String, type: String, contract: String, typeCheckList: Int, userUpdate: String, contractDate: String): UpdateErrorFragment {
            val args = Bundle()
            args.putString(Constants.ARG_SUPID, supId)
            args.putString(Constants.ARG_OBJID, type)
            args.putString(Constants.ARG_CONTRACT_NUMBER, contract)
            args.putInt(Constants.ARG_TYPE_CHECKLIST, typeCheckList)
            args.putString(Constants.ARG_UPDATE_BY, userUpdate)
            args.putString(Constants.ARG_OBJ_CREATEDATE, contractDate)
            val fragment = UpdateErrorFragment()
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
            objId = it.getString(Constants.ARG_OBJID)
            contractNumber = it.getString(Constants.ARG_CONTRACT_NUMBER) ?: ""
            supId = it.getString(Constants.ARG_SUPID) ?: ""
            typeCheckList = it.getInt(Constants.ARG_TYPE_CHECKLIST)
            userUpdate = it.getString(Constants.ARG_UPDATE_BY) ?: ""
            contractDate = it.getString(Constants.ARG_OBJ_CREATEDATE) ?: ""
            fragError_tvNumber.text = objId
        }
        setTitle(TitleAndMenuModel(title = contractNumber, status = true, image = R.drawable.ic_info))
    }

    //Start : Lấy dữ liệu từ database lên
    private fun setAllData() {
        setDataTypeKS()
        setDataTypeError()
        setDataMainError()
        setDataDescription()
    }

    private fun setDataTypeKS() {
        listUserKS = ErrorRealmManager().getDistinctDepartment()
        if (listUserKS.size != 0)
            fragError_tvUserKS.text = listUserKS[Constants.FIRST_ITEM].account
    }

    private fun setDataTypeError() {
        listTypeError = ErrorRealmManager().getDistinctTypeError(listUserKS[positionUserKS].account)
        if (listTypeError.size != 0) {
            listTypeError[Constants.FIRST_ITEM].status = false
            positionTypeError = if (!checkFirstChoice) Constants.FIRST_ITEM else Constants.SECOND_ITEM
            listTypeError[positionTypeError].status = true
            fragError_tvTypeError.text = listTypeError[positionTypeError].account
        }
    }

    private fun setDataMainError() {
        listMainError = ErrorRealmManager().getDistinctMainError(listUserKS[positionUserKS].account, listTypeError[positionTypeError].account, positionTypeError == Constants.FIRST_ITEM)
        if (listMainError.size != 0)
            fragError_tvMainError.text = listMainError[Constants.FIRST_ITEM].account
    }

    private fun setDataDescription() {
        listDescription = ErrorRealmManager().getDistinctDescription(listUserKS[positionUserKS].account, listTypeError[positionTypeError].account, listMainError[positionMainError].account, positionTypeError == Constants.FIRST_ITEM)
        if (listDescription.size != 0)
            fragError_tvDescription.text = listDescription[Constants.FIRST_ITEM].account
    }
    //End :Lấy dữ liệu từ database lên

    fun setDefaultValueIndex(view: Int, index: Int) {
        when (view) {
            R.id.fragError_tvUserKS -> {
                positionUserKS = index
                positionTypeError = Constants.FIRST_ITEM
                positionMainError = Constants.FIRST_ITEM
                positionDescription = Constants.FIRST_ITEM
            }
            R.id.fragError_tvTypeError -> {
                positionTypeError = index
                positionMainError = Constants.FIRST_ITEM
                positionDescription = Constants.FIRST_ITEM
            }
            R.id.fragError_tvMainError -> {
                positionMainError = index
                positionDescription = Constants.FIRST_ITEM
            }
            R.id.fragError_tvTypeKS -> positionTypeKS = index
            R.id.fragError_tvIndoor -> positionIndoor = index
        }
    }

    fun setDefaultData(view: Int) {
        when (view) {
            R.id.fragError_tvUserKS -> {
                checkFirstChoice = true
                setDataTypeError()
                setDataMainError()
                setDataDescription()
            }
            R.id.fragError_tvTypeError -> {
                checkFirstChoice = positionTypeError == Constants.FIRST_ITEM
                setDataMainError()
                setDataDescription()
            }
            R.id.fragError_tvMainError -> {
                setDataDescription()
            }
        }
    }

    fun showDetailContract() {
        addFragment(DetailContractFragment.newInstance(supId, Constants.STATUS_COMPLETED, objId.toInt(), typeCheckList, contractNumber, contractDate), true, true)
    }

    private fun initOnClick() {
        fragError_tvTypeKS.setOnClickListener { AppUtils.showDialogSingChoice(fragmentManager, getString(R.string.error_detail_type), listTypeKS, fragError_tvTypeKS,positionTypeKS) }
        fragError_tvIndoor.setOnClickListener { AppUtils.showDialogSingChoice(fragmentManager, getString(R.string.error_detail_main_indoor), listIndoor, fragError_tvIndoor,positionIndoor) }
        fragError_tvUserKS.setOnClickListener {
            AppUtils.showDialogSingChoice(fragmentManager, getString(R.string.error_detail_user), listUserKS, fragError_tvUserKS, positionUserKS)
        }
        fragError_tvTypeError.setOnClickListener {
            AppUtils.showDialogSingChoice(fragmentManager, getString(R.string.error_detail_type_error), listTypeError, fragError_tvTypeError, positionTypeError)
        }
        fragError_tvMainError.setOnClickListener {
            AppUtils.showDialogSingChoice(fragmentManager, getString(R.string.error_detail_main_error), listMainError, fragError_tvMainError, positionMainError)
        }
        fragError_tvDescription.setOnClickListener {
            AppUtils.showDialogSingChoice(fragmentManager, getString(R.string.error_detail_description), listDescription, fragError_tvDescription, positionDescription)
        }
        fragError_tvMoney.onChange { }
        fragError_tvSubmit.setOnClickListener { AppUtils.showDialog(fragmentManager, content = getString(R.string.error_notify), actionCancel = true, confirmDialogInterface = this) }
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

    private fun initParamsRequestError() {
        presenter.let {
            //init data Error
            val mapContract = HashMap<String, Any>()
            mapContract[Constants.PARAMS_TYPE_UPDATE] = listTypeKS[positionTypeKS].id
            mapContract[Constants.PARAMS_INDOOR] = listIndoor[positionIndoor].id
            mapContract[Constants.PARAMS_ERROR_DEPARTMENT] = fragError_tvUserKS.text.toString()
            mapContract[Constants.PARAMS_CABLE] = AppUtils.removeAddPref(fragError_tvCableCheck.text.toString()).toInt()
            mapContract[Constants.PARAMS_MONEY] = AppUtils.removeAddPref(fragError_tvMoney.text.toString()).toInt()
            mapContract[Constants.PARAMS_ERROR_TYPE] = fragError_tvTypeError.text.toString()
            mapContract[Constants.PARAMS_ERROR_MAIN] = fragError_tvMainError.text.toString()
            mapContract[Constants.PARAMS_ERROR_DESCRIPTION] = fragError_tvDescription.text.toString()
            mapContract[Constants.PARAMS_DESCRIPTION] = fragError_tvNote.text.toString()
            mapContract[Constants.PARAMS_USER_CL] = userUpdate
            mapContract[Constants.PARAMS_DISTANCE] = AppUtils.getTwoDecimal(distanceBetween)
            mapContract[Constants.PARAMS_COORDINATE_CUS] = coordinateGuest
            mapContract[Constants.PARAMS_COORDINATE_USER] = coordinateUser
            mapContract[Constants.PARAMS_TYPE_CL] = typeCheckList
            mapContract[Constants.PARAMS_OBJID] = objId
            mapContract[Constants.PARAMS_ERROR_ID] = listDescription[positionDescription].id
            mapContract[Constants.PARAMS_SUPID] = supId
            //init data call api
            val map = HashMap<String, Any>()
            map[Constants.PARAMS_USER_NAME] = getSharePreferences().accountName
            map[Constants.PARAMS_IMEI_LOW] = getSharePreferences().imeiDevice
            map[Constants.PARAMS_CONTRACT] = mapContract
            //call api
            it.postUpdateError(map)
        }
    }

    private fun postRequestHaveLocation() {
        val error = when {
            distanceBetween == Constants.NO_DISTANCE_RULE -> {
                getString(R.string.notify_error_type_no_update)
            }
            distanceBetween < Constants.DISTANCE_RULE -> {
                getString(R.string.notify_error_type_ok)
            }
            else -> {
                getString(R.string.notify_error_type_over)
            }
        }
        val messError = getString(R.string.notify_update_error, AppUtils.changeFormatDistance(distanceBetween), error)
        AppUtils.showDialog(fragmentManager, content = messError, actionCancel = true, confirmDialogInterface = object : ConfirmDialogInterface {
            override fun onClickOk() {
                initParamsRequestError()
            }

            override fun onClickCancel() {

            }
        })
        hideLoading()
    }

    private fun handleHaveLocation(location: String) {
        locationService = LocationService(context)
        locationService.getLocationManager()
        if (!locationService.getStatusGps()!!) {
            AppUtils.showDialog(fragmentManager, content = getString(R.string.notify_off_GPS), actionCancel = true, confirmDialogInterface = object : ConfirmDialogInterface {
                override fun onClickOk() {
                    val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                    startActivity(intent)
                }

                override fun onClickCancel() {

                }

            })
            hideLoading()
        } else {
            coordinateGuest = AppUtils.getLocationUser(location)
            latLngGuest = AppUtils.getLatLng(location)
            locationUser = locationService.getLocationUser()
            locationUser?.let {
                latLngUser = LatLng(it.latitude, it.longitude)
                coordinateUser = "${it.latitude},${it.longitude}"
                distanceBetween = SphericalUtil.computeDistanceBetween(latLngUser, latLngGuest)
            }
            postRequestHaveLocation()
        }
    }

    override fun loadCoordinateContract(response: ResponseModel) {
        if (response.Data.toString().isBlank()) {
            hideLoading()
            AppUtils.showDialog(fragmentManager, content = getString(R.string.notify_no_location), actionCancel = true, confirmDialogInterface = object : ConfirmDialogInterface {
                override fun onClickOk() {
                    initParamsRequestError()
                }

                override fun onClickCancel() {

                }
            })
        } else {
            handleHaveLocation(response.Data.toString())
        }
    }

    override fun loadUpdateError(response: ResponseModel) {
        AppUtils.showDialog(fragmentManager, content = response.Description, confirmDialogInterface = null)
    }

    override fun handleError(error: String) {
        hideLoading()
        AppUtils.showDialog(fragmentManager, content = error, confirmDialogInterface = null)
    }

    override fun onClickOk() {
        presenter.let {
            showLoading()
            val map = HashMap<String, Any>()
            map[Constants.PARAMS_USER_NAME_LOW] = getSharePreferences().accountName
            map[Constants.PARAMS_CONTRACT] = contractNumber
            it.getCoordinateContract(map)
        }
    }

    override fun onClickCancel() {

    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onDetach()
    }
}