package com.jhiltunen.stepupcounter.ui.analytics

import android.app.Application
import androidx.lifecycle.*
import com.jhiltunen.stepupcounter.data.database.HealthDatabase
import com.jhiltunen.stepupcounter.data.models.Steps
import com.jhiltunen.stepupcounter.logic.repository.HealthRepository
import com.jhiltunen.stepupcounter.utils.SharedPreferencesManager
import kotlinx.coroutines.InternalCoroutinesApi

@InternalCoroutinesApi
class AnalyticsViewModel(application: Application) : AndroidViewModel(application) {

    private var sharedPreferencesManager : SharedPreferencesManager = SharedPreferencesManager()

    // use LiveData so we can observe if the data in database changes
    val getUsersAllSteps: LiveData<List<Steps>>
    private val repository: HealthRepository

    init {
        // create new Health Data Access Object
        val healthDao = HealthDatabase.getDatabase(application).healthDao()
        // initialize new repository and load userId from shared preferences
        repository = HealthRepository(healthDao, sharedPreferencesManager.loadUserId(getApplication<Application>().applicationContext))
        // get list of all steps related to user
        getUsersAllSteps = repository.getAllUsersSteps()
    }
}