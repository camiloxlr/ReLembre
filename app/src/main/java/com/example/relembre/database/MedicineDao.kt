package com.example.relembre.database


import androidx.room.*
import com.example.relembre.model.AlertEntity
import com.example.relembre.model.AlertWithMedicine
import com.example.relembre.model.MedicineEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MedicineDao {
    @Query("SELECT * FROM medicines where id = :id")
    fun getMedicine(id: Long): Flow<MedicineEntity>

    @Query("SELECT * FROM medicines")
    fun getMedicines(): Flow<List<MedicineEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addMedicine(item: MedicineEntity)

    @Query("UPDATE medicines SET notes = 'abc' WHERE id = :itemId")
    suspend fun updateMedicine(itemId: Long)

    @Delete
    suspend fun removeMedicine(item: MedicineEntity)

    @Query("SELECT * FROM alerts where id = :id")
    fun getAlert(id: Long): Flow<AlertEntity>

    @Query("SELECT * FROM alerts")
    fun getAlerts(): Flow<List<AlertEntity>>

    @Query("SELECT alerts.*, medicines.name AS medicineName FROM alerts LEFT JOIN medicines ON alerts.medicineId = medicines.id")
    fun getAlertsWithMedicine(): Flow<List<AlertWithMedicine>>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addAlert(item: AlertEntity)

    @Query("UPDATE alerts SET notes = 'abc' WHERE id = :itemId")
    suspend fun updateAlert(itemId: Long)

    @Delete
    suspend fun removeAlert(item: AlertEntity)
}