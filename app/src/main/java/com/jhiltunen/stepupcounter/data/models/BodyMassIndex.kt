package com.jhiltunen.stepupcounter.data.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "body_mass_index", foreignKeys = [ForeignKey(entity = User::class, parentColumns = arrayOf("id"), childColumns = arrayOf("userId"), onDelete = ForeignKey.CASCADE)])
data class BodyMassIndex (
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val date: String,
    val bodyMassIndex: Double,
    val userId: Long
)