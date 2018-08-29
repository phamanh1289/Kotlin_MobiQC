package mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.reprot.chart_report

import android.graphics.Color
import android.os.Bundle
import android.support.v4.content.ContextCompat
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
import com.github.mikephil.charting.utils.ColorTemplate
import kotlinx.android.synthetic.main.fragment_chart_report.*
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.R
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.data.network.model.ReportModel
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.base.BaseFragment
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.utils.KeyboardUtils

/**
 * * Created by Anh Pham on 08/09/2018.     **
 * * Copyright (c) 2018 by FPT Telecom      **
 */
class ChartReportFragment : BaseFragment(), OnChartValueSelectedListener {

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
        initView()
    }

    private fun initView() {
        handleDataArgument()
        initChart()
    }

    fun loadDataReport(model : ReportModel) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun initChart() {
        val mode = ReportModel(1, 2, 3, 4, 5, 6)
        val listEntry = ArrayList<PieEntry>()
        listEntry.add(PieEntry(mode.TK_Nong.toFloat(), ""))
        listEntry.add(PieEntry(mode.TK_Nguoi.toFloat(), ""))
        listEntry.add(PieEntry(mode.BT_Nguoi.toFloat(), ""))
        listEntry.add(PieEntry(mode.BT_Nong.toFloat(), ""))
        listEntry.add(PieEntry(mode.ChuyenDe.toFloat(), ""))
        listEntry.add(PieEntry(mode.Swap.toFloat(), ""))
        val pieDataSet = PieDataSet(listEntry, "")
        val colors = java.util.ArrayList<Int>()
        context?.let {
            colors.add(ContextCompat.getColor(it, R.color.bright_blue))
            colors.add(ContextCompat.getColor(it, R.color.bright_red))
            colors.add(ContextCompat.getColor(it, R.color.blue))
            colors.add(ContextCompat.getColor(it, R.color.red_text))
            colors.add(ContextCompat.getColor(it, R.color.colorPrimary))
            colors.add(ContextCompat.getColor(it, R.color.steel))
        }
        colors.add(ColorTemplate.getHoloBlue())
        pieDataSet.colors = colors
        val pieData = PieData(pieDataSet)
        pieData.setValueTextColor(Color.WHITE)
        pieData.setValueTextSize(6f * resources.displayMetrics.scaledDensity)
        pieData.setValueFormatter(PercentFormatter())
        fragChartReport_chart.data = pieData
        fragChartReport_chart.setOnChartValueSelectedListener(this)
        fragChartReport_chart.run {
            description = null
            setTouchEnabled(true)
            isRotationEnabled = false
            animateY(600)
            //Radius center while
            holeRadius = 25f
            legend.isEnabled = false
            setEntryLabelTextSize(6f * resources.displayMetrics.scaledDensity)
            transparentCircleRadius = 0f
            isHighlightPerTapEnabled = true
            setUsePercentValues(true)
        }
    }


    private fun handleDataArgument() {

    }

    override fun onNothingSelected() {

    }

    override fun onValueSelected(e: Entry?, h: Highlight?) {

    }
}