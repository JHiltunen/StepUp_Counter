package com.jhiltunen.stepupcounter.ui.analytics

import android.icu.text.DecimalFormat
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.view.View
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
import com.jhiltunen.stepupcounter.R
import com.jhiltunen.stepupcounter.data.models.Steps
import kotlinx.coroutines.InternalCoroutinesApi
import java.util.stream.Collectors
import kotlin.math.pow


class AnalyticsFragment : Fragment(R.layout.fragment_analytics) {

    private val sdfYearMonthDay = SimpleDateFormat("yyyy-MM-dd")
    private val sdfDayMonth = SimpleDateFormat("dd-MM.")

    @InternalCoroutinesApi
    private lateinit var analyticsViewModel: AnalyticsViewModel
    private lateinit var barChart : BarChart

    @InternalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        analyticsViewModel = ViewModelProvider(this).get(AnalyticsViewModel::class.java)
        barChart = view.findViewById(R.id.fragment_verticalbarchart_chart)

        analyticsViewModel.getUsersAllSteps.observe(viewLifecycleOwner, {

            val labelNames: ArrayList<String> = ArrayList()

            val dates : Array<Any?> = it.stream().map {steps -> steps.date.toString() }.toArray()
            val stepsData : ArrayList<BarEntry> = ArrayList()

            val stepsCountArrayList: List<Float> = it.stream().map { steps -> steps.value.toFloat() }.collect(Collectors.toList())

            for (i in stepsCountArrayList.indices) {
                val date: String = sdfDayMonth.format(sdfYearMonthDay.parse(dates[i].toString())).toString()
                val stepsCountArrayListItem: Float = stepsCountArrayList[i]
                stepsData.add(BarEntry(i.toFloat(), stepsCountArrayListItem))
                labelNames.add(date)
            }

            val barDataSet = BarDataSet(stepsData, "Daily steps")
            barDataSet.setColors(*ColorTemplate.COLORFUL_COLORS)
            val description = Description()
            description.text = "Person"
            barChart.description = description

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
        })
    }
}