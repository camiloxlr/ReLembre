package com.example.relembre.database

import com.example.relembre.model.AlertEntity
import com.example.relembre.model.MedicineEntity
import javax.inject.Inject

class MedicineLocalDataSource @Inject constructor(
    private val medicineDao: MedicineDao
){

    fun getItems() = medicineDao.getMedicines()

    fun getMedicine(id: Long) = medicineDao.getMedicine(id);

    suspend fun addMedicine(name: String, notes: String) = medicineDao.addMedicine(MedicineEntity(name, notes));

    suspend fun removeMedicine(item: MedicineEntity) = medicineDao.removeMedicine(item);

    suspend fun updateMedicine(itemId: Long) = medicineDao.updateMedicine(itemId);

    fun getAlerts() = medicineDao.getAlerts()
    fun getAlertsWithMedicine() = medicineDao.getAlertsWithMedicine()

    fun getAlert(id: Long) = medicineDao.getAlert(id);

    suspend fun addAlert(
        hours: Int,
        minutes: Int,
        dose: String,
        notes: String,
        medicineId: Long
    ) = medicineDao.addAlert(AlertEntity(
            hours,
            minutes,
            dose,
            notes,
            medicineId
        )
    );

    suspend fun removeAlert(item: AlertEntity) = medicineDao.removeAlert(item);

    suspend fun updateAlert(itemId: Long) = medicineDao.updateAlert(itemId);
}