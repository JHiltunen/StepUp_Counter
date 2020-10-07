package com.jhiltunen.stepupcounter.logic.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.jhiltunen.stepupcounter.data.models.BodyMassIndex
import com.jhiltunen.stepupcounter.data.models.Steps
import com.jhiltunen.stepupcounter.data.models.User

/**
 * Defines SQL commands for the app to use for database.
 * Runs queries against Room database.
 */
@Dao
interface HealthDao {

    /**
     * Function to insert new user's data and after insert add zero steps to current date for the specific user.
     * @param user User object containing all user information.
     * @param date Represents current date.
     */
    @Transaction
    suspend fun insertIntoUsersAndInitializeSteps(user: User, date: String): Long {
        var id = addUser(user)
        addSteps(Steps(0, date, 0, -1, id))

        return id
    }

    /**
     * Adds new User to database.
     * @param user User object.
     * @return Returns newly created user id.
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addUser(user: User): Long

    /**
     * Updates users information with given user object that contains updated information.
     * @param user User object.
     */
    @Update(onConflict = OnConflictStrategy.IGNORE)
    suspend fun updateUser(user: User)

    /**
     * Adds body mass index value to specific date.
     * @param bodyMassIndex BodyMassIndex object
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addBodyMassIndexToDate(bodyMassIndex: BodyMassIndex)

    /**
     * Updates body mass index table row.
     * @param bodyMassIndex BodyMassIndex object.
     */
    @Update(onConflict = OnConflictStrategy.IGNORE)
    suspend fun updateCurrentDateBodyMassIndex(bodyMassIndex: BodyMassIndex)

    /**
     * Query to fetch users body mass index from specific date.
     * @param date Represents current date
     */
    @Query("SELECT * FROM body_mass_index WHERE date = :date AND id = :id")
    fun getUsersBodyMassIndexFromSpecificDate(date: String, id: Float): BodyMassIndex

    /**
     * Query to fetch users all body mass indexes.
     * @param id Id of the user.
     */
    @Query("SELECT * FROM body_mass_index WHERE userId = :id ORDER BY date DESC")
    fun getUsersAllBodyMassIndexes(id: Long): LiveData<List<BodyMassIndex>>

    /**
     * Adds new steps to database.
     * @param steps Steps object.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addSteps(steps: Steps)

    /**
     * Updates steps information with given steps object that contains updated information.
     * @param steps User object.
     */
    @Update(onConflict = OnConflictStrategy.IGNORE)
    suspend fun updateSteps(steps: Steps)

    /**
     * Query to select all users with specific id from database.
     * @param id Users id as Long.
     * @return LiveData<User>
     */
    @Query("SELECT * FROM users WHERE id = :id")
    fun getUser(id: Long): LiveData<User>

    /**
     * Query to get value of each steps row where userId and date is given.
     * @param userId Users id as Long.
     * @param date Tells which date steps count will be returned.
     * @return LiveData<Int>
     */
    @Query("SELECT value FROM steps WHERE userId = :userId AND date = :date")
    fun getUsersStepsCountFromSpecificDate(userId: Long, date: String): LiveData<Int>

    /**
     * Query to get previousSteps from specific date where userId is given.
     * @param userId Users id as Long.
     * @param date Tells which date previousSteps will be returned.
     * @return Int
     */
    @Query("SELECT previousSteps FROM steps WHERE userId = :userId AND date = :date")
    fun getUsersPreviousStepsFromSpecificDate(userId: Long, date: String): Int

    /**
     * Query to return entire row from steps table from specific date and with specific user id.
     * @param userId Users id as Long.
     * @param date Tells which date steps count will be returned.
     * @return Steps object.
     */
    @Query("SELECT * FROM steps WHERE userId = :userId AND date = :date")
    fun getUsersStepsFromSpecificDate(userId: Long, date: String): Steps

    /**
     * Query to return all dates user has recorded steps count.
     * @param userId Users id.
     * @return List<String>
     */
    @Query("SELECT date FROM steps WHERE userId = :userId")
    fun getAllUsersDates(userId: Long): List<String>

    /**
     * Query to return list of all users steps.
     * @param userId Users id.
     * @return List<String>
     */
    @Query ("SELECT * FROM steps WHERE userId = :userId")
    fun getAllUsersSteps(userId: Long): LiveData<List<Steps>>
}