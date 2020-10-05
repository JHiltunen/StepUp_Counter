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
}