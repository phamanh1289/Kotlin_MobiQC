package mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.check_list.search

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.fragment_search_check_list.*
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.R
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.data.network.model.ContractDetailModel
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.data.network.model.ResponseModel
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.data.network.model.TitleAndMenuModel
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.others.constant.Constants
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.base.BaseFragment
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.blank.SearchCheckListContract
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.blank.SearchCheckListPresenter
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.check_list.all_check_list.AllCheckListFragment
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.check_list.create_check_list.CreateCheckListFragment
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.check_list.create_pre_check_list.CreatePreCheckListFragment
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.check_list.search.diff.SearchCheckListAdapter
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.utils.AppUtils
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.utils.KeyboardUtils
import javax.inject.Inject

/**
 * * Created by Anh Pham on 08/09/2018.     **
 * * Copyright (c) 2018 by FPT Telecom      **
 */
class SearchCheckListFragment : BaseFragment(), SearchCheckListContract.SearchCheckListView {

    @Inject
    lateinit var presenter: SearchCheckListPresenter

    private var menuType = 0
    private var searchKey = ""
    private var fromDate = ""
    private var titleFragment = ""
    private var toDate = ""
    private var locationId = 0
    private var searchType = 0
    private var typeCL = 0
    private var isReport = false
    private lateinit var adapterCheckList: SearchCheckListAdapter
    private var listDataCheckList = ArrayList<ContractDetailModel>()

    companion object {
        fun newInstance(type: Int, searchType: Int, keySearch: String, location: Int, isReport: Boolean): SearchCheckListFragment {
            val args = Bundle()
            args.putInt(Constants.ARG_MENU_TYPE, type)
            args.putInt(Constants.ARG_SEARCH_TYPE, searchType)
            args.putString(Constants.ARG_SEARCH_KEY, keySearch)
            args.putInt(Constants.ARG_LOCATION, location)
            val fragment = SearchCheckListFragment()
            fragment.arguments = args
            return fragment
        }

        fun newInstance(type: Int, typeCL: Int, isReport: Boolean, fromDate: String, toDate: String, title: String): SearchCheckListFragment {
            val args = Bundle()
            args.putInt(Constants.ARG_SEARCH_TYPE, type)
            args.putInt(Constants.ARG_TYPE_CHECKLIST, typeCL)
            args.putString(Constants.PARAMS_FROM_DATE, fromDate)
            args.putString(Constants.PARAMS_TO_DATE, toDate)
            args.putBoolean(Constants.ARG_IS_REPORT, isReport)
            args.putString(Constants.ARG_TITLE, title)
            val fragment = SearchCheckListFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_search_check_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getActivityComponent().inject(this)
        presenter.onAttach(this)
        activity?.let { KeyboardUtils.setupUI(view, activity = it) }
        initView()
    }

    private fun initView() {
        getDataArgument()
        initParamsRequest()
    }

    private fun getDataArgument() {
        arguments?.let {
            menuType = it.getInt(Constants.ARG_MENU_TYPE)
            searchType = it.getInt(Constants.ARG_SEARCH_TYPE)
            typeCL = it.getInt(Constants.ARG_TYPE_CHECKLIST)
            searchKey = it.getString(Constants.ARG_SEARCH_KEY) ?: ""
            locationId = it.getInt(Constants.ARG_LOCATION)
            isReport = it.getBoolean(Constants.ARG_IS_REPORT)
            fromDate = it.getString(Constants.PARAMS_FROM_DATE) ?: ""
            toDate = it.getString(Constants.PARAMS_TO_DATE) ?: ""
            titleFragment = it.getString(Constants.ARG_TITLE) ?: ""
        }
        setTitle(TitleAndMenuModel(title = if (isReport) titleFragment else getString(R.string.result_search), status = false))
    }

    private fun initParamsRequest() {
        presenter.let {
            showLoading()
            val map = HashMap<String, Any>()
            if (isReport) {
                map[Constants.PARAMS_TYPE] = searchType
                map[Constants.PARAMS_TO_DATE] = toDate
                map[Constants.PARAMS_FROM_DATE] = fromDate
                map[Constants.PARAMS_TYPE_CL] = typeCL
                map[Constants.PARAMS_USER_NAME_NON_CAPWORD] = getSharePreferences().accountName
                it.getReportControlErrorDetail(map)
            } else {
                map[Constants.PARAMS_SEARCH_TYPE] = searchType
                map[Constants.PARAMS_INFO] = searchKey
                map[Constants.PARAMS_LOCATION_ID] = locationId
                it.getListContract(map)
            }
        }
    }

    private fun handleDataListContract(list: ArrayList<ContractDetailModel>) {
        listDataCheckList = list
        if (listDataCheckList.size != 0) {
            adapterCheckList = SearchCheckListAdapter {
                if (!isReport) {
                    val model = Gson().toJson(listDataCheckList[it])
                    when (menuType) {
                        Constants.ARG_MENU_CNL -> {
                            addFragment(AllCheckListFragment.newInstance(model, listDataCheckList[it].Contract), true, true)
                        }
                        Constants.ARG_MENU_PCL -> {
                            addFragment(CreatePreCheckListFragment.newInstance(model), true, true)
                        }
                        Constants.ARG_MENU_CL -> {
                            addFragment(CreateCheckListFragment.newInstance(model), true, true)
                        }
                    }
                }
            }
            adapterCheckList.submitList(listDataCheckList)
            fragSearchCheckList_rvMain.apply {
                val layout = LinearLayoutManager(context)
                layout.orientation = LinearLayoutManager.VERTICAL
                layoutManager = layout
                setHasFixedSize(true)
                adapter = adapterCheckList
            }
            fragSearchCheckList_rvMain.visibility = View.VISIBLE
        } else fragSearchCheckList_tvNoData.visibility = View.VISIBLE
        hideLoading()
    }

    override fun loadListContract(response: ResponseModel) {
        if (response.Code == Constants.REQUEST_SUCCESS) {
            handleDataListContract(Gson().fromJson(if (isReport) response.Data.toString() else Gson().toJson(response.Data), object : TypeToken<ArrayList<ContractDetailModel>>() {}.type))
        } else
            AppUtils.showDialog(fragmentManager, content = response.Description, confirmDialogInterface = null)
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