package com.example.relembre.model

import androidx.room.Embedded
import androidx.room.Relation

data class MedicineWithAlerts(
    @Embedded val medicine: MedicineEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "medicineId"
    )
    val alerts: List<AlertEntity>
)
