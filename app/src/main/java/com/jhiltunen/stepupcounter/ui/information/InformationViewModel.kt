package com.jhiltunen.stepupcounter.ui.information

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.jhiltunen.stepupcounter.utils.Person
import com.jhiltunen.stepupcounter.utils.SharedPreferencesManager

class InformationViewModel(application: Application) : AndroidViewModel(application) {

    private var sharedPreferencesManager : SharedPreferencesManager = SharedPreferencesManager()
    private var person : Person = Person()

    var height = 0
    var weight = 0
    var gender = 0

    fun loadUserInformation() {
        person = sharedPreferencesManager.loadData(getApplication())
        height = person.getHeight()
        weight = person.getWeight()
        gender = person.getGender()
    }

    fun saveUserInformation() {
        person.setHeight(height)
        person.setWeight(weight)
        person.setGender(gender)
        sharedPreferencesManager.saveData(getApplication(), person)
    }
}