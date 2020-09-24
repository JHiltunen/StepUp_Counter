package com.stepupcounter.stepupcounter.ui.analytics

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.stepupcounter.stepupcounter.R
import kotlin.random.Random


class AnalyticsFragment : Fragment() {

    private lateinit var analyticsViewModel: AnalyticsViewModel
    private val MAX_X_VALUE = 7
    private val MAX_Y_VALUE = 50
    private val MIN_Y_VALUE = 5
    private val SET_LABEL = "Steps count"
    private val DAYS = arrayOf("MON", "TUE", "WED", "THU", "FRI", "SAT", "SUN")

    private lateinit var chart : BarChart

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

        chart = root.findViewById(R.id.fragment_verticalbarchart_chart)

        val data: BarData = createChartData()
        configureChartAppearance()
        prepareChartData(data)

        return root
    }

    private fun configureChartAppearance() {
        chart.description.isEnabled = false
        chart.setDrawValueAboveBar(false)
        val xAxis = chart.xAxis
        xAxis.valueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                return DAYS[value.toInt()]
            }
        }
        val axisLeft = chart.axisLeft
        axisLeft.granularity = 10f
        axisLeft.axisMinimum = 0f
        val axisRight = chart.axisRight
        axisRight.granularity = 10f
        axisRight.axisMinimum = 0f
    }

    private fun createChartData(): BarData {
        val values: ArrayList<BarEntry> = ArrayList()
        for (i in 0 until MAX_X_VALUE) {
            val x = i.toFloat()
            val y: Float = MIN_Y_VALUE + Random.nextFloat() * (MAX_Y_VALUE - MIN_Y_VALUE)
            values.add(BarEntry(x, y))
        }
        val set1 = BarDataSet(values, SET_LABEL)
        val dataSets: ArrayList<IBarDataSet> = ArrayList()
        dataSets.add(set1)
        return BarData(dataSets)
    }

    private fun prepareChartData(data: BarData) {
        data.setValueTextSize(12f)
        chart.data = data
        chart.invalidate()
    }
}