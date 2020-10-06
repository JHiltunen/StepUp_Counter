package com.jhiltunen.stepupcounter.data.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Represents room database table "steps".
 * @param id Is id for specific date.
 * @param date Tells which day steps are.
 * @param value Tells how many steps user has taken on specific date.
 * @param previousSteps Tells the value where we started counting steps.
 * @param userId Refrences users table user id.
 */
@Entity(tableName = "steps", indices = [Index(value = ["date"], unique = true)], foreignKeys = [ForeignKey(entity = User::class, parentColumns = arrayOf("id"), childColumns = arrayOf("userId"), onDelete = ForeignKey.CASCADE)])
data class Steps (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val date: String,
    val value: Int,
    val previousSteps: Int,
    val userId: Long

)