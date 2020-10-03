package com.jhiltunen.stepupcounter.ui.analytics

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import com.jhiltunen.stepupcounter.R


class AnalyticsFragment : Fragment(R.layout.fragment_analytics) {

    private lateinit var analyticsViewModel: AnalyticsViewModel
    private lateinit var barChart : BarChart

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        analyticsViewModel = ViewModelProvider(this).get(AnalyticsViewModel::class.java)
        barChart = view.findViewById(R.id.fragment_verticalbarchart_chart)
        configureChartAppearance()
    }

    private fun configureChartAppearance() {
        val barDataSet = BarDataSet(analyticsViewModel.createChartDataFromSharedPreferences(), "Daily steps")
        barDataSet.setColors(*ColorTemplate.COLORFUL_COLORS)
        val description = Description()
        description.text = "Person"
        barChart.description = description

        val labelNames = analyticsViewModel.getLabelNames()

        val xAxis: XAxis = barChart.xAxis
        xAxis.valueFormatter = IndexAxisValueFormatter(labelNames)
        xAxis.position = XAxis.XAxisPosition.TOP
        xAxis.setDrawGridLines(false)
        xAxis.setDrawAxisLine(false)
        xAxis.granularity = 1f
        xAxis.labelCount = labelNames.size
        barChart.extraBottomOffset = -50f

        val barData = BarData(barDataSet)
        barChart.data = barData
        barData.setValueTextSize(12f)
        barChart.invalidate()
    }
}