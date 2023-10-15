package com.example.relembre


import android.content.Context
import androidx.room.Room
import com.example.relembre.database.MedicineDao
import com.example.relembre.database.MedicineDatabase
import com.example.relembre.database.MedicineLocalDataSource
import com.example.relembre.repositories.MedicineRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppDI {

    @Provides
    @Singleton
    fun provideMedicineDao(medicineDatabase: MedicineDatabase): MedicineDao {
        return medicineDatabase.medicineDao()
    }

    @Provides
    @Singleton
    fun provideMedicineRepository(medicineLocalDataSource: MedicineLocalDataSource) =
        MedicineRepository(medicineLocalDataSource)

    @Provides
    @Singleton
    fun provideMedicineDatabase(
        @ApplicationContext applicationContext: Context
    ) : MedicineDatabase {
        return Room.databaseBuilder(
            applicationContext,
            MedicineDatabase::class.java,
            "medicines"
        ).build()
    }
}