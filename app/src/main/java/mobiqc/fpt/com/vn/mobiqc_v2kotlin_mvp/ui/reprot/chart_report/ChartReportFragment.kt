package mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.reprot.chart_report

import android.graphics.Color
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import kotlinx.android.synthetic.main.fragment_chart_report.*
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.R
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.data.network.model.DetailReportModel
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.data.network.model.ReportModel
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.others.datacore.DataCore
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.base.BaseFragment
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.reprot.chart_report.diff.ChartRePortAdapter
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.reprot.data_report.DataReportFragment
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.utils.AppUtils
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.utils.KeyboardUtils

/**
 * * Created by Anh Pham on 08/09/2018.     **
 * * Copyright (c) 2018 by FPT Telecom      **
 */
class ChartReportFragment : BaseFragment(), OnChartValueSelectedListener {

    private var listDataReport = ArrayList<DetailReportModel>()
    private var listEntry = ArrayList<PieEntry>()
    private lateinit var mAdapterChart: ChartRePortAdapter

    companion object {
        fun newInstance(): ChartReportFragment {
            val args = Bundle()
            val fragment = ChartReportFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_chart_report, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.let { KeyboardUtils.setupUI(view, activity = it) }
        loadDataReport(ReportModel())
    }

    fun loadDataReport(model: ReportModel) {
        listDataReport.run {
            if (size != 0)
                clear()
            if (model.TK_Nong != 0)
                add(DetailReportModel(title = DataReportFragment.DEP_HOT, value = model.TK_Nong))
            if (model.TK_Nguoi != 0)
                add(DetailReportModel(title = DataReportFragment.DEP_COOL, value = model.TK_Nguoi))
            if (model.BT_Nong != 0)
                add(DetailReportModel(title = DataReportFragment.MAIN_HOT, value = model.BT_Nong))
            if (model.BT_Nguoi != 0)
                add(DetailReportModel(title = DataReportFragment.MAIN_COOL, value = model.BT_Nguoi))
            if (model.ChuyenDe != 0)
                add(DetailReportModel(title = DataReportFragment.SUBJ, value = model.ChuyenDe))
            if (model.Swap != 0)
                add(DetailReportModel(title = DataReportFragment.SWAP, value = model.Swap))
        }
        initChart()
        initRecyclerView()
        fragChartReport_llTitle.visibility = if (listDataReport.size != 0) View.VISIBLE else View.GONE
        fragChartReport_tvNoData.visibility = if (listDataReport.size == 0) View.VISIBLE else View.GONE
    }

    private fun initRecyclerView() {
        mAdapterChart = ChartRePortAdapter(AppUtils.getSumListData(listDataReport))
        mAdapterChart.submitList(listDataReport)
        fragChartReport_rvMain.apply {
            val layout = LinearLayoutManager(context)
            layout.orientation = LinearLayoutManager.VERTICAL
            layoutManager = layout
            setHasFixedSize(true)
            adapter = mAdapterChart
            isNestedScrollingEnabled = false
        }
    }

    private fun initChart() {
        initDataEntryChart()
        val pieDataSet = PieDataSet(listEntry, "")
        pieDataSet.run {
            //line while
            sliceSpace = 2f
            //list color
            colors = DataCore.getListcolor(context)
        }
        val pieDataChart = PieData(pieDataSet)
        pieDataChart.run {
            setValueTextColor(Color.WHITE)
            setValueTextSize(6f * resources.displayMetrics.scaledDensity)
            setValueFormatter(PercentFormatter())
        }
        fragChartReport_chart.run {
            data = pieDataChart
            //hide description
            description = null
            setTouchEnabled(true)
            isRotationEnabled = false
            animateY(600)
            //Radius center while
            holeRadius = 25f
            //hide item in chart
            legend.isEnabled = false
            setEntryLabelTextSize(6f * resources.displayMetrics.scaledDensity)
            //Shadow circle center
            transparentCircleRadius =0f
//            isHighlightPerTapEnabled = true
            setUsePercentValues(true)
        }
        fragChartReport_chart.setOnChartValueSelectedListener(this)
    }

    private fun initDataEntryChart() {
        if (listEntry.size != 0)
            listEntry.clear()
        listDataReport.forEach {
            if (it.value != 0)
                listEntry.add(PieEntry(it.value.toFloat(), ""))
        }
    }

    override fun onNothingSelected() {

    }

    override fun onValueSelected(e: Entry?, h: Highlight?) {

    }
}