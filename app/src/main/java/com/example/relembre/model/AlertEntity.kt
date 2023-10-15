package com.example.relembre.model

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "alerts")
data class AlertEntity(
    val hours: Int,
    val minutes: Int,
    val dose: String,
    val notes: String,
    val medicineId: Long,
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0
)