package com.example.relembre.repositories

import com.example.relembre.database.MedicineLocalDataSource
import com.example.relembre.model.AlertEntity
import com.example.relembre.model.MedicineEntity
import javax.inject.Inject

class MedicineRepository  @Inject constructor(
    private val dataSource: MedicineLocalDataSource
){

    fun getItems() = dataSource.getItems()

    suspend fun addMedicine(name: String, notes: String) = dataSource.addMedicine(name, notes)

    fun getMedicine(id: Long) = dataSource.getMedicine(id)

    suspend fun updateMedicine(itemId: Long) = dataSource.updateMedicine(itemId)

    suspend fun removeMedicine(item: MedicineEntity) = dataSource.removeMedicine(item)

    fun getAlerts() = dataSource.getAlerts()
    fun getAlertsWithMedicine() = dataSource.getAlertsWithMedicine()

    suspend fun addAlert(
        hours: Int,
        minutes: Int,
        dose: String,
        notes: String,
        medicineId: Long
    ) = dataSource.addAlert(
        hours,
        minutes,
        dose,
        notes,
        medicineId)

    fun getAlert(id: Long) = dataSource.getAlert(id)

    suspend fun updateAlert(itemId: Long) = dataSource.updateAlert(itemId)

    suspend fun removeAlert(item: AlertEntity) = dataSource.removeAlert(item)
}