package com.jhiltunen.stepupcounter.data.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Represents database body_mass_index table.
 * @param id That's generated automatically.
 * @param date Date when body mass index value is recorded.
 * @param userId Foreignkey reference to users table id
 */
@Entity(tableName = "body_mass_index", indices = [Index(value = ["date"], unique = true)], foreignKeys = [ForeignKey(entity = User::class, parentColumns = arrayOf("id"), childColumns = arrayOf("userId"), onDelete = ForeignKey.CASCADE)])
data class BodyMassIndex (
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val date: String,
    val bodyMassIndex: Double,
    val userId: Long
)