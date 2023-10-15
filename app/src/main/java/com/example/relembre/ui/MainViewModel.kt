package com.example.relembre.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.relembre.model.AlertEntity
import com.example.relembre.model.AlertWithMedicine
import com.example.relembre.model.MedicineEntity
import com.example.relembre.repositories.MedicineRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: MedicineRepository,
    ):ViewModel() {

    val allMedicines: Flow<List<MedicineEntity>> = repository.getItems()

    fun getMedicine(id: Long): Flow<MedicineEntity> {
        return repository.getMedicine(id)
    }

    fun addMedicine(name: String, notes: String) {
        viewModelScope.launch {
            repository.addMedicine(name, notes)
        }
    }

    fun removeMedicine(item: MedicineEntity) {
        viewModelScope.launch {
            repository.removeMedicine(item)
        }
    }

    fun updateMedicine(itemId: Long) {
        viewModelScope.launch {
            repository.updateMedicine(itemId)
        }
    }

    val allAlerts: Flow<List<AlertEntity>> = repository.getAlerts()

    val allAlertsWithMedicine: Flow<List<AlertWithMedicine>> = repository.getAlertsWithMedicine()

    fun getAlert(id: Long): Flow<AlertEntity> {
        return repository.getAlert(id)
    }

    fun addAlert(hours: Int,
                 minutes: Int,
                 dose: String,
                 notes: String,
                 medicineId: Long) {
        viewModelScope.launch {
            repository.addAlert(hours,
                minutes,
                dose,
                notes,
                medicineId)
        }
    }

    fun removeAlert(item: AlertEntity) {
        viewModelScope.launch {
            repository.removeAlert(item)
        }
    }

    fun updateAlert(itemId: Long) {
        viewModelScope.launch {
            repository.updateAlert(itemId)
        }
    }

}