package com.example.relembre.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

sealed class NavigationItem(val route: String, val label: String, val icons: ImageVector) {

    object Hoje : NavigationItem("home", "Hoje", Icons.Default.DateRange)
    object Medicamentos :
        NavigationItem("medicine", "Medicamentos", Icons.Default.Face)

    object Alertas : NavigationItem("alerts","Alertas",Icons.Default.Star)

    object CadastroMedicamento : NavigationItem("storeMedicine","Cadastro de Medicamento",Icons.Default.Search)
    object VisualizarMedicamento : NavigationItem("viewMedicine","Informações do Medicamento",Icons.Default.Search)

    object CadastroAlerta : NavigationItem("storeAlert","Cadastro de Alerta",Icons.Default.Search)
    object VisualizarAlerta : NavigationItem("viewAlert","Informações do Alerta",Icons.Default.Search)
}
