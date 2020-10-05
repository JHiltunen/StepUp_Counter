package com.jhiltunen.stepupcounter.ui.information

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.jhiltunen.stepupcounter.R
import com.jhiltunen.stepupcounter.data.database.HealthDatabase
import com.jhiltunen.stepupcounter.data.models.User
import com.jhiltunen.stepupcounter.logic.repository.HealthRepository
import com.jhiltunen.stepupcounter.utils.Person
import com.jhiltunen.stepupcounter.utils.SharedPreferencesManager
import kotlinx.android.synthetic.main.fragment_information.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.launch

@InternalCoroutinesApi
class InformationViewModel(application: Application) : AndroidViewModel(application) {

    private var sharedPreferencesManager : SharedPreferencesManager = SharedPreferencesManager()
    //private var person : Person = Person()
    val getUser: LiveData<User>

    private val repository: HealthRepository

    init {
        val healthDao = HealthDatabase.getDatabase(application).healthDao()
        repository = HealthRepository(healthDao, sharedPreferencesManager.loadUserId(getApplication<Application>().applicationContext))
        getUser = repository.getUser
    }
}