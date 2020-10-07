package com.jhiltunen.stepupcounter.logic.repository

import android.util.Log
import androidx.lifecycle.LiveData
import com.jhiltunen.stepupcounter.data.models.BodyMassIndex
import com.jhiltunen.stepupcounter.data.models.Steps
import com.jhiltunen.stepupcounter.data.models.User
import com.jhiltunen.stepupcounter.logic.dao.HealthDao
import java.text.SimpleDateFormat
import java.util.Date

class HealthRepository(private val healthDao: HealthDao, var id: Long) {

    val getUser: LiveData<User> = healthDao.getUser(id)

    suspend fun insertIntoUsersAndInitializeSteps(user: User, date: String): Long {
        return healthDao.insertIntoUsersAndInitializeSteps(user, date)
    }

    suspend fun addUser(user: User): Long {
        return healthDao.addUser(user)
    }

    suspend fun updateUser(user: User) {
        healthDao.updateUser(user)
    }

    suspend fun addBodyMassIndexToDate(bodyMassIndex: BodyMassIndex) {
        healthDao.addBodyMassIndexToDate(bodyMassIndex)
    }

    fun getUsersAllBodyMassIndexes(): LiveData<List<BodyMassIndex>> {
        return healthDao.getUsersAllBodyMassIndexes(id)
    }

    fun getUsersStepsCountFromSpecificDate(date: String): LiveData<Int> {
        Log.d("HealthRepository", "getUsersStepsCountFromSpecificDate: userid=$id")
        Log.d("HealthRepository", "getUsersStepsCountFromSpecificDate: date=$date")
        return healthDao.getUsersStepsCountFromSpecificDate(id, date)
    }

    fun getUsersStepsFromSpecificDate(date: String): Steps {
        return healthDao.getUsersStepsFromSpecificDate(id, date)
    }

    fun getUsersPreviousStepsFromSpecificDate(date: String): Int {
        return healthDao.getUsersPreviousStepsFromSpecificDate(id, date)
    }

    fun getUsersLastSavedDate(): Date {
        var datesAsStringList = healthDao.getAllUsersDates(id)

        return datesAsStringList.stream().map { date -> SimpleDateFormat("yyyy-MM-dd").parse(date) }.max(Date::compareTo).get()
    }

    fun getAllUsersSteps():LiveData<List<Steps>> {
        return healthDao.getAllUsersSteps(id)
    }

    suspend fun addSteps(steps: Steps) {
        healthDao.addSteps(steps)
    }

    suspend fun updateSteps(steps: Steps) {
        healthDao.updateSteps(steps)
    }

    suspend fun updateBodyMassIndexToDate(bodyMassIndex: BodyMassIndex) {
        healthDao.updateCurrentDateBodyMassIndex(bodyMassIndex)
    }
}