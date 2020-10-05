package com.jhiltunen.stepupcounter.ui.analytics

import android.app.Application
import androidx.lifecycle.*
import com.github.mikephil.charting.data.BarEntry
import com.jhiltunen.stepupcounter.data.database.HealthDatabase
import com.jhiltunen.stepupcounter.data.models.Steps
import com.jhiltunen.stepupcounter.data.models.User
import com.jhiltunen.stepupcounter.logic.repository.HealthRepository
import com.jhiltunen.stepupcounter.utils.SharedPreferencesManager
import com.jhiltunen.stepupcounter.utils.Person
import kotlinx.coroutines.InternalCoroutinesApi
import java.text.SimpleDateFormat

@InternalCoroutinesApi
class AnalyticsViewModel(application: Application) : AndroidViewModel(application) {

    private var sharedPreferencesManager : SharedPreferencesManager = SharedPreferencesManager()
    val getUsersAllSteps: LiveData<List<Steps>>
    private val repository: HealthRepository

    init {
        val healthDao = HealthDatabase.getDatabase(application).healthDao()
        repository = HealthRepository(healthDao, sharedPreferencesManager.loadUserId(getApplication<Application>().applicationContext))
        getUsersAllSteps = repository.getAllUsersSteps()
    }

    private val sdf : SimpleDateFormat = SimpleDateFormat("dd.MM.")
    private val sdfYearMonthDay : SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd")
    private var labelNames: ArrayList<String>? = null

    private lateinit var person : Person

    init {
        labelNames = ArrayList()
    }

    fun createChartDataFromSharedPreferences(): ArrayList<BarEntry> {
        person = sharedPreferencesManager.loadData(getApplication())

        val stepsData : ArrayList<BarEntry> = ArrayList()
        val dates : Array<String> = person.getDaysAsArray()
        val stepsCountArrayList = person.getStepsAsList()

        for (i in stepsCountArrayList.indices) {
            val date: String = sdf.format(sdfYearMonthDay.parse(dates[i]))
            val stepsCountArrayListItem = stepsCountArrayList[i]
            stepsData.add(BarEntry(i.toFloat(), stepsCountArrayListItem))
            labelNames!!.add(date)
        }

        return stepsData
    }

    fun getLabelNames(): ArrayList<String> {
        return labelNames!!
    }
}