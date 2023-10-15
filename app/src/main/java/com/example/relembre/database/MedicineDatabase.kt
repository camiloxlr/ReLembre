package com.example.relembre.database
import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.relembre.model.AlertEntity
import com.example.relembre.model.MedicineEntity


@Database(entities = [MedicineEntity::class, AlertEntity::class], version = 1)
abstract class MedicineDatabase: RoomDatabase() {
    abstract fun medicineDao(): MedicineDao
}