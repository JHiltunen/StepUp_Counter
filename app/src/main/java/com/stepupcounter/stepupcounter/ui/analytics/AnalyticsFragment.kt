package com.stepupcounter.stepupcounter.ui.analytics

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import com.stepupcounter.stepupcounter.R
import com.stepupcounter.stepupcounter.utils.SharedPreferencesManager
import com.stepupcounter.stepupcounter.utils.Steps
import java.text.SimpleDateFormat


class AnalyticsFragment : Fragment() {

    private lateinit var analyticsViewModel: AnalyticsViewModel
    private lateinit var barChart : BarChart

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        analyticsViewModel =
                ViewModelProvider(this).get(AnalyticsViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_analytics, container, false)
        val textView: TextView = root.findViewById(R.id.text_analytics)
        analyticsViewModel.text.observe(viewLifecycleOwner, {
            textView.text = it
        })

        barChart = root.findViewById(R.id.fragment_verticalbarchart_chart)

        analyticsViewModel = ViewModelProvider(this).get(AnalyticsViewModel::class.java)
        configureChartAppearance()

        return root
    }

    private fun configureChartAppearance() {
        val barDataSet = BarDataSet(analyticsViewModel.createChartDataFromSharedPreferences(), "Daily steps")
        barDataSet.setColors(*ColorTemplate.COLORFUL_COLORS)
        val description = Description()
        description.text = "Steps"
        barChart.description = description

        val labelNames = analyticsViewModel.getLabelNames()

        val xAxis: XAxis = barChart.xAxis
        xAxis.valueFormatter = IndexAxisValueFormatter(labelNames)
        xAxis.position = XAxis.XAxisPosition.TOP
        xAxis.setDrawGridLines(false)
        xAxis.setDrawAxisLine(false)
        xAxis.granularity = 1f
        xAxis.labelCount = labelNames.size

        val barData = BarData(barDataSet)
        barChart.data = barData
        barData.setValueTextSize(12f)
        barChart.invalidate()
    }
}