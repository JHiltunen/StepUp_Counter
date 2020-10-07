package com.jhiltunen.stepupcounter.logic.repository

import android.util.Log
import androidx.lifecycle.LiveData
import com.jhiltunen.stepupcounter.data.models.BodyMassIndex
import com.jhiltunen.stepupcounter.data.models.Steps
import com.jhiltunen.stepupcounter.data.models.User
import com.jhiltunen.stepupcounter.logic.dao.HealthDao
import java.text.SimpleDateFormat
import java.util.Date

/**
 * Class has access to Health Data Access object.
 * Calls function that executes SQL commands.
 */
class HealthRepository(private val healthDao: HealthDao, var id: Long) {

    /**
     * Get observable LiveData of users information
     */
    val getUser: LiveData<User> = healthDao.getUser(id)

    /**
     * Function to add user and initialize current date steps count value to zero.
     * @param user User object containing user data.
     * @param date Current date
     */
    suspend fun insertIntoUsersAndInitializeSteps(user: User, date: String): Long {
        return healthDao.insertIntoUsersAndInitializeSteps(user, date)
    }

    /**
     * Function to update users information.
     * @param user User object with updated user information.
     */
    suspend fun updateUser(user: User) {
        healthDao.updateUser(user)
    }

    /**
     * Function to add BodyMassIndex to specific date.
     * @param bodyMassIndex BodyMassIndex object containing all BodyMassIndex data.
     */
    suspend fun addBodyMassIndexToDate(bodyMassIndex: BodyMassIndex) {
        healthDao.addBodyMassIndexToDate(bodyMassIndex)
    }

    /**
     * Function to get users body mass indexes from all dates.
     * @return LiveData<List<BodyMassIndex>> Returns LiveData of all rows from body_mass_index table.
     */
    fun getUsersAllBodyMassIndexes(): LiveData<List<BodyMassIndex>> {
        return healthDao.getUsersAllBodyMassIndexes(id)
    }

    /**
     * Function to get users steps count from specific date.
     * @param date Represents the date from which to fetch steps count.
     * @return LiveData<Int> return LiveData from specific date steps value.
     */
    fun getUsersStepsCountFromSpecificDate(date: String): LiveData<Int> {
        Log.d("HealthRepository", "getUsersStepsCountFromSpecificDate: userid=$id")
        Log.d("HealthRepository", "getUsersStepsCountFromSpecificDate: date=$date")
        return healthDao.getUsersStepsCountFromSpecificDate(id, date)
    }

    /**
     * Function to get users steps from specific date.
     * @param date Represents the date from which to fetch steps count.
     * @return LiveData<Date> return LiveData from specific date. Returns entire steps table row with all information.
     */
    fun getUsersStepsFromSpecificDate(date: String): Steps {
        return healthDao.getUsersStepsFromSpecificDate(id, date)
    }

    /**
     * Function to get users previousSteps value from specific date.
     * @param date represents the date when previousSteps value is retrieved.
     * @return Int value of previousSteps from specific date.
     */
    fun getUsersPreviousStepsFromSpecificDate(date: String): Int {
        return healthDao.getUsersPreviousStepsFromSpecificDate(id, date)
    }

    /**
     * Function to get last saved Date.
     * @return Last saved date as string.
     */
    fun getUsersLastSavedDate(): Date {
        var datesAsStringList = healthDao.getAllUsersDates(id)

        return datesAsStringList.stream().map { date -> SimpleDateFormat("yyyy-MM-dd").parse(date) }.max(Date::compareTo).get()
    }

    /**
     * Function to get list of all users steps from every date.
     * @return LiveData<List<Steps>> List of users all steps.
     */
    fun getAllUsersSteps():LiveData<List<Steps>> {
        return healthDao.getAllUsersSteps(id)
    }

    /**
     * Function to add new steps.
     * @param steps Steps object that contains all steps data.
     */
    suspend fun addSteps(steps: Steps) {
        healthDao.addSteps(steps)
    }

    /**
     * Function to update steps value.
     * @param steps Steps object that contains updated steps value
     */
    suspend fun updateSteps(steps: Steps) {
        healthDao.updateSteps(steps)
    }

    /**
     * Function to update body mass index value.
     * @param bodyMassIndex BodyMassIndex object that contains updated BodyMassIndex value.
     */
    suspend fun updateBodyMassIndexToDate(bodyMassIndex: BodyMassIndex) {
        healthDao.updateCurrentDateBodyMassIndex(bodyMassIndex)
    }
}