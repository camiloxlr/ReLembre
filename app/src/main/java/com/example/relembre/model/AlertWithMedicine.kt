package com.example.relembre.model

import androidx.room.ColumnInfo
import androidx.room.Embedded

data class AlertWithMedicine(
    @Embedded
    val alert: AlertEntity,

    @ColumnInfo(name = "medicineName")
    val medicineName: String
)
