package mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.reprot.data_report

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_data_report.*
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.R
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.data.network.model.DetailReportModel
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.data.network.model.ReportModel
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.others.constant.Constants
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.base.BaseFragment
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.check_list.search.SearchCheckListFragment
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.reprot.ReportFragment
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.reprot.data_report.diff.DataReportAdapter
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.utils.AppUtils
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.utils.KeyboardUtils

/**
 * * Created by Anh Pham on 08/09/2018.     **
 * * Copyright (c) 2018 by FPT Telecom      **
 */
class DataReportFragment : BaseFragment() {

    private var listDataReport = ArrayList<DetailReportModel>()
    private var adapterReport: DataReportAdapter? = null

    companion object {
        const val DEP_HOT = "Triển khai - Nóng"
        const val DEP_COOL = "Triển khai - Nguội"
        const val MAIN_HOT = "Bảo trì - Nóng"
        const val MAIN_COOL = "Bảo trì - Nguội"
        const val SUBJ = "Chuyên đề"
        const val SWAP = "Swap"

        fun newInstance(): DataReportFragment {
            val args = Bundle()
            val fragment = DataReportFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_data_report, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.let { KeyboardUtils.setupUI(view, activity = it) }
        initView()
    }

    private fun initView() {
        adapterReport = DataReportAdapter {
            if (listDataReport[it].value != 0) {
                val model = listDataReport[it]
                initParamRequestData(model.typeCL, model.typeError, model.title)
            } else AppUtils.showDialog(fragmentManager, content = getString(R.string.no_data), confirmDialogInterface = null)
        }
        adapterReport?.submitList(listDataReport)
        fragDataReport_rvMain.apply {
            val layout = LinearLayoutManager(context)
            layout.orientation = LinearLayoutManager.VERTICAL
            layoutManager = layout
            adapter = adapterReport
            setHasFixedSize(true)
        }
    }

    private fun initParamRequestData(typeCL: Int, typeError: Int, title: String) {
        val fromDate = (parentFragment as ReportFragment).fromDate
        val toDate = (parentFragment as ReportFragment).toDate
        addFragment(SearchCheckListFragment.newInstance(type = typeError, typeCL = typeCL, title = title, fromDate = fromDate, toDate = toDate, isReport = true), true, true)
    }

    fun loadDataReport(model: ReportModel) {
        listDataReport.run {
            if (size != 0)
                clear()
            add(DetailReportModel(title = DEP_HOT, value = model.TK_Nong, typeCL = Constants.DEPLOYMENT, typeError = Constants.TYPE_KS_HOT))
            add(DetailReportModel(title = DEP_COOL, value = model.TK_Nguoi, typeCL = Constants.DEPLOYMENT, typeError = Constants.TYPE_KS_COOL))
            add(DetailReportModel(title = MAIN_HOT, value = model.BT_Nong, typeCL = Constants.MAINTENANCE, typeError = Constants.TYPE_KS_HOT))
            add(DetailReportModel(title = MAIN_COOL, value = model.BT_Nguoi, typeCL = Constants.MAINTENANCE, typeError = Constants.TYPE_KS_COOL))
            add(DetailReportModel(title = SUBJ, value = model.ChuyenDe, typeCL = Constants.PARAMS_NO_VALUE_INT, typeError = Constants.TYPE_KS_SUBJ))
            add(DetailReportModel(title = SWAP, value = model.Swap, typeCL = Constants.PARAMS_NO_VALUE_INT, typeError = Constants.TYPE_KS_SWAP))
        }
        adapterReport?.notifyDataSetChanged()
    }

}