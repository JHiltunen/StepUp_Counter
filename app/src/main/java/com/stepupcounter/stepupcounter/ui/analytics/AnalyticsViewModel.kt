package com.stepupcounter.stepupcounter.ui.analytics

import android.app.Application
import androidx.lifecycle.*
import com.github.mikephil.charting.data.BarEntry
import com.stepupcounter.stepupcounter.utils.SharedPreferencesManager
import com.stepupcounter.stepupcounter.utils.Steps
import java.text.SimpleDateFormat

class AnalyticsViewModel(application: Application) : AndroidViewModel(application) {

    private val sdf : SimpleDateFormat = SimpleDateFormat("dd.MM.")
    private val sdfYearMonthDay : SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd")
    private var labelNames: ArrayList<String>? = null

    private var sharedPreferencesManager : SharedPreferencesManager = SharedPreferencesManager()
    private lateinit var steps : Steps

    init {
        labelNames = ArrayList()
    }

    fun createChartDataFromSharedPreferences(): ArrayList<BarEntry> {
        steps = sharedPreferencesManager.loadData(getApplication())

        val stepsData : ArrayList<BarEntry> = ArrayList()
        val dates : Array<String> = steps.getDaysAsArray()
        val stepsCountArrayList = steps.getStepsAsList()

        for (i in stepsCountArrayList.indices) {
            val date: String = sdf.format(sdfYearMonthDay.parse(dates[i]))
            val stepsCountArrayList = stepsCountArrayList[i]
            stepsData.add(BarEntry(i.toFloat(), stepsCountArrayList))
            labelNames!!.add(date)
        }

        return stepsData
    }

    fun getLabelNames(): ArrayList<String> {
        return labelNames!!
    }
}