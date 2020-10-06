package com.jhiltunen.stepupcounter.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Represents room database table "users".
 * @param id Is id for specific user.
 * @param username Users username.
 * @param height Users height in centimeters.
 * @param weight Users weight in kilograms.
 * @param gender Tells users gender.
 */
@Entity(tableName = "users")
data class User (
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val username: String,
    val height: Int,
    val weight: Int,
    val gender: String
)