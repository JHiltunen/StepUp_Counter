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

    private lateinit var steps : Steps
    private var sharedPreferencesManager : SharedPreferencesManager = SharedPreferencesManager()
    private val sdf : SimpleDateFormat = SimpleDateFormat("dd.MM.")
    private val sdfYearMonthDay : SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd")

    private lateinit var analyticsViewModel: AnalyticsViewModel
    private val SET_LABEL = "Steps count"

    private lateinit var barChart : BarChart
    private var labelNames: ArrayList<String>? = null
    private var stepsCountArrayList: ArrayList<Float> = ArrayList()


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
        steps = sharedPreferencesManager.loadData(this.requireActivity().applicationContext)
        labelNames = ArrayList()

        configureChartAppearance()

        return root
    }


    private fun configureChartAppearance() {
        val barDataSet = BarDataSet(createChartDataFromSharedPreferences(), "Daily steps")
        barDataSet.setColors(*ColorTemplate.COLORFUL_COLORS)
        val description = Description()
        description.text = "Steps"
        barChart.description = description

        val xAxis: XAxis = barChart.xAxis
        xAxis.valueFormatter = IndexAxisValueFormatter(labelNames)
        xAxis.position = XAxis.XAxisPosition.TOP
        xAxis.setDrawGridLines(false)
        xAxis.setDrawAxisLine(false)
        xAxis.granularity = 1f
        xAxis.labelCount = labelNames!!.size
        barChart.animateY(2000)

        val barData = BarData(barDataSet)
        barChart.data = barData
        barData.setValueTextSize(12f)
        barChart.invalidate()
    }

    private fun createChartDataFromSharedPreferences(): ArrayList<BarEntry> {
        val stepsData : ArrayList<BarEntry> = ArrayList()
        val dates : Array<String> = steps.getDaysAsArray()
        stepsCountArrayList = steps.getStepsAsList()
        for (i in stepsCountArrayList.indices) {
            val date: String = sdf.format(sdfYearMonthDay.parse(dates[i]))
            val stepsCountArrayList = stepsCountArrayList[i]
            stepsData.add(BarEntry(i.toFloat(), stepsCountArrayList))
            labelNames!!.add(date)
        }

        return stepsData
    }
}