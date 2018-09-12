package vn.com.fpt.mobiqc.ui.reprot

import android.os.Bundle
import android.support.v4.view.ViewPager
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_report.*
import vn.com.fpt.mobiqc.R
import vn.com.fpt.mobiqc.data.network.model.ReportModel
import vn.com.fpt.mobiqc.data.network.model.ResponseModel
import vn.com.fpt.mobiqc.data.network.model.TitleAndMenuModel
import vn.com.fpt.mobiqc.others.constant.Constants
import vn.com.fpt.mobiqc.ui.base.BaseFragment
import vn.com.fpt.mobiqc.ui.check_list.all_check_list.BaseViewPagerAdapter
import vn.com.fpt.mobiqc.ui.contract.search_contract.SearchFragment
import vn.com.fpt.mobiqc.ui.reprot.chart_report.ChartReportFragment
import vn.com.fpt.mobiqc.ui.reprot.data_report.DataReportFragment
import vn.com.fpt.mobiqc.utils.AppUtils
import vn.com.fpt.mobiqc.utils.KeyboardUtils
import javax.inject.Inject

/**
 * * Created by Anh Pham on 08/09/2018.     **
 * * Copyright (c) 2018 by FPT Telecom      **
 */
class ReportFragment : BaseFragment(), ReportContract.ReportView {
    @Inject
    lateinit var presenter: ReportPresenter
    private lateinit var mViewPagerAdapter: BaseViewPagerAdapter
    private var itemReport: ReportModel? = null
    private var indexTabLayout = 0
    var fromDate = ""
    var toDate = ""

    companion object {
        fun newInstance(type: Int): SearchFragment {
            val args = Bundle()
            args.putInt(Constants.ARG_TYPE_CONTRACT, type)
            val fragment = SearchFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_report, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getActivityComponent().inject(this)
        presenter.onAttach(this)
        activity?.let { KeyboardUtils.setupUI(view, activity = it) }
        initView()
    }

    private fun initView() {
        setTitle(TitleAndMenuModel(title = getString(R.string.menu_bao_cao_so_lieu), status = true, image = R.drawable.ic_notifications))
        initOnClick()
        setUpTabLayout()
        initParamRequestData()
    }

    private fun initOnClick() {
        fragReport_tvFromDate.setText(AppUtils.getCurrentDate(Constants.CURRENT_DATE))
        fragReport_tvToDate.setText(AppUtils.getCurrentDate(Constants.CURRENT_DATE))
        fragReport_tvFromDate.onChange { it }
        fragReport_tvToDate.onChange { it }
        fragReport_tvFromDate.setOnClickListener { AppUtils.showPickTime(context, fragReport_tvFromDate, Constants.SET_CURRENT_IS_MAX_DATE) }
        fragReport_tvToDate.setOnClickListener { AppUtils.showPickTime(context, fragReport_tvToDate, Constants.SET_CURRENT_IS_MAX_DATE) }
    }

    fun initParamRequestData() {
        presenter.let {
            showLoading()
            fromDate = AppUtils.toConvertDateFormat(context, fragReport_tvFromDate.text.toString())
            toDate = AppUtils.toConvertDateFormat(context, fragReport_tvToDate.text.toString())
            val map = HashMap<String, Any>()
            map[Constants.PARAMS_USER_NAME_NON_CAPWORD] = getSharePreferences().accountName
            map[Constants.PARAMS_FROM_DATE] = fromDate
            map[Constants.PARAMS_TO_DATE] = toDate
            it.getControlErrorReport(map)
        }
    }

    private fun setUpTabLayout() {
        mViewPagerAdapter = BaseViewPagerAdapter(childFragmentManager)
        mViewPagerAdapter.addTabFragment(DataReportFragment.newInstance(), getString(R.string.data_report))
        mViewPagerAdapter.addTabFragment(ChartReportFragment.newInstance(), getString(R.string.chart_report))
        fragReport_vPager.adapter = mViewPagerAdapter
        fragReport_tabLayout.setupWithViewPager(fragReport_vPager)
        fragReport_vPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {
                indexTabLayout = position
                handleFragment(indexTabLayout)
            }
        })
    }

    private fun handleFragment(index: Int) {
        hideLoading()
        itemReport?.let {
            val fragment = mViewPagerAdapter.getItem(index)
            when (fragment) {
                is DataReportFragment -> fragment.loadDataReport(it)
                is ChartReportFragment -> fragment.loadDataReport(it)
            }
        }
    }

    private fun handleDataReport(data: Any) {
        var sData = data.toString()
        if (sData.contains("[")) sData = sData.replace("[", "")
        if (sData.contains("]")) sData = sData.replace("]", "")
        itemReport = Gson().fromJson(sData, ReportModel::class.java)
        handleFragment(indexTabLayout)
    }

    override fun loadControlErrorReport(response: ResponseModel) {
        if (response.Code == Constants.REQUEST_SUCCESS)
            handleDataReport(response.Data)
        else {
            hideLoading()
            AppUtils.showDialog(fragmentManager, content = response.Description, confirmDialogInterface = null)
        }
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
                val fromDateChoice = fragReport_tvFromDate.text.toString()
                val toDateChoice = fragReport_tvToDate.text.toString()
                if (AppUtils.compareDate(fromDateChoice, toDateChoice) || (fromDateChoice == toDateChoice))
                    initParamRequestData()
                else
                    AppUtils.showDialog(fragmentManager, content = getString(R.string.error_date), confirmDialogInterface = null)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }
}