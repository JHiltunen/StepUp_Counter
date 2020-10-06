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

    // SimpleDateFormat to help processing Dates
    private val sdfYearMonthDay = SimpleDateFormat("yyyy-MM-dd")
    private val sdfDayMonth = SimpleDateFormat("dd.MM.")

    @InternalCoroutinesApi
    private lateinit var analyticsViewModel: AnalyticsViewModel
    private lateinit var barChart : BarChart

    @InternalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // get the view model
        analyticsViewModel = ViewModelProvider(this).get(AnalyticsViewModel::class.java)
        // fin bar chart from view
        barChart = view.findViewById(R.id.fragment_verticalbarchart_chart)

        // Get users all steps and observe LiveData from database
        // when there is change in steps table, draw bar chart again
        analyticsViewModel.getUsersAllSteps.observe(viewLifecycleOwner, {
            // it = list of Steps object
            val labelNames: ArrayList<String> = ArrayList()
            // get dates from List<Steps> to String array
            val dates : Array<Any?> = it.stream().map {steps -> steps.date.toString() }.toArray()
            // create ArrayList for bar entries/steps values
            val stepsData : ArrayList<BarEntry> = ArrayList()

            // Get steps.value from each Steps object and convert to float. Collect all float values to List<Float>
            val stepsCountArrayList: List<Float> = it.stream().map { steps -> steps.value.toFloat() }.collect(Collectors.toList())

            // loop trough stepsCountArrayList
            for (i in stepsCountArrayList.indices) {
                // parse yyyy-MM-dd to Date object
                // then format the date as MM.dd. to show on UI
                // for example 2020-10-15 will be shown on chart as 10.15.
                val date: String = sdfDayMonth.format(sdfYearMonthDay.parse(dates[i].toString())).toString()
                // get the current item from list
                val stepsCountArrayListItem: Float = stepsCountArrayList[i]
                // add new bar entry to ArrayList
                stepsData.add(BarEntry(i.toFloat(), stepsCountArrayListItem))
                // add date in MM.dd. format to x-axis label names
                labelNames.add(date)
            }

            // create new data set
            val barDataSet = BarDataSet(stepsData, "Daily steps")
            // set color for chart bars
            barDataSet.setColors(*ColorTemplate.JOYFUL_COLORS)
            // set description for data set
            val description = Description()
            description.text = "Steps/day"
            barChart.description = description

            // format x axis
            val xAxis: XAxis = barChart.xAxis
            xAxis.valueFormatter = IndexAxisValueFormatter(labelNames)
            xAxis.position = XAxis.XAxisPosition.TOP
            xAxis.setDrawGridLines(false)
            xAxis.setDrawAxisLine(false)
            xAxis.granularity = 1f
            xAxis.labelCount = labelNames.size
            barChart.extraBottomOffset = -50f

            // set data to chart
            val barData = BarData(barDataSet)
            barChart.data = barData
            barData.setValueTextSize(12f)
            barChart.invalidate()
        })
    }
}