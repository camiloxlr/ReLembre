package com.example.relembre.model

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "medicines")
data class MedicineEntity(
    val name: String,
    val notes: String,
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0
)