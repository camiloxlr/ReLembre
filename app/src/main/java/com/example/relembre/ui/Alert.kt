package com.example.relembre.ui

import android.content.Intent
import android.provider.CalendarContract
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.relembre.model.AlertEntity
import com.example.relembre.model.AlertWithMedicine
import com.example.relembre.model.MedicineEntity
import java.util.*

@Composable
fun Alertas(navController: NavHostController, viewModel: MainViewModel = hiltViewModel()) {
    val data = viewModel.allAlertsWithMedicine.collectAsState(initial = emptyList()).value

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top
    ) {
        TopAppBar(
            title = { Text(text = "Meus Alertas", color = Color.Black) },
            modifier = Modifier.background(Color.White)
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.BottomEnd
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            AlertListSection(
                data,
                onItemClick = {
                    viewModel.getAlert(it.id)
                },
                onDeleteClick = {
                    viewModel.removeAlert(it)
                },
                navController
            )

            FloatingActionButton(
                onClick = {
                    navController.navigate(NavigationItem.CadastroAlerta.route)
                },
                content = {
                    IconButton(
                        onClick = {
                            navController.navigate(NavigationItem.CadastroAlerta.route)
                        }
                    ) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = "Add")
                    }
                },
                modifier = Modifier
                    .padding(4.dp, 60.dp)
            )
        }
    }
}

@Composable
fun AlertListSection(
    alertsWithMedicineNames: List<AlertWithMedicine>,
    onItemClick: (AlertEntity) -> Unit,
    onDeleteClick: (AlertEntity) -> Unit,
    navController: NavHostController
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp)
    ) {
        items(alertsWithMedicineNames) { item ->
            AlertItemCard(item, onItemClick, onDeleteClick, navController)
        }

        if (alertsWithMedicineNames.isEmpty()) {
            item {
                Text(
                    text = "Para começar, cadastre um medicamento e seus alertas.",
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    textAlign = TextAlign.Center,
                    style = TextStyle(fontSize = 16.sp, color = Color.Gray)
                )
            }
        }
    }
}

@Composable
fun AlertItemCard(
    item: AlertWithMedicine,
    onItemClick: (AlertEntity) -> Unit,
    onDeleteClick: (AlertEntity) -> Unit,
    navController: NavHostController
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = MaterialTheme.shapes.medium,
        elevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .clickable {
                    onItemClick(item.alert)
                    navController.navigate("${NavigationItem.VisualizarAlerta.route}/${item.alert.id}")
                },
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(16.dp)
            ) {
                Text(
                    text = item.medicineName,
                    style = MaterialTheme.typography.body1
                )

                Text(
                    text = "${item.alert.hours}h ${item.alert.minutes}m",
                    style = MaterialTheme.typography.body1
                )

                Text(
                    text = item.alert.notes,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.body1
                )
            }

            IconButton(
                onClick = {
                    onDeleteClick(item.alert)
                },
                modifier = Modifier
                    .padding(16.dp)
            ) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete")
            }
        }
    }
}

@Composable
fun CadastroAlerta(navController: NavHostController, viewModel: MainViewModel = hiltViewModel(), mainActivity: MainActivity, onBack: () -> Unit) {
    var hours by remember { mutableStateOf(0) }
    var minutes by remember { mutableStateOf(0) }
    var dose by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }
    var selectedMedicine by remember { mutableStateOf<MedicineEntity?>(null) }

    val medicineList by viewModel.allMedicines.collectAsState(initial = emptyList())

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            TopAppBar (
                title = { Text(text = "Cadastrar Alerta", color = Color.Black) },
                Modifier.background(Color.White),
                navigationIcon = {
                    IconButton(
                        onClick = { onBack() }
                    ) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )

            Spacer(modifier = Modifier.height(16.dp))
            AlertInputSection(
                selectedHour = hours,
                selectedMinute = minutes,
                dose = dose,
                notes = notes,
                medicineList = medicineList,
                selectedMedicine = selectedMedicine,
                onHoursChange = {
                    hours = it
                },
                onMinutesChange = {
                    minutes = it
                },
                onDoseChange = {
                    dose = it
                },
                onNotesChange = {
                    notes = it
                },
                onMedicineSelected = {
                    selectedMedicine = it
                },
                onAddItemClick = { alertEntity ->
                    if (alertEntity.hours > 0 && alertEntity.minutes > 0) {
                        viewModel.addAlert(alertEntity.hours, alertEntity.minutes, alertEntity.dose, alertEntity.notes, alertEntity.medicineId)
                        println("aqui1: salvou")

                        mainActivity.scheduleAlarmManager(alertEntity)

                        hours = 0
                        minutes = 0
                        dose = ""
                        notes = ""
                        selectedMedicine = null
                        navController.navigateUp()
                    }
                }
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VisualizarAlerta(
    alertEntity: AlertEntity,
    onBack: () -> Unit
) {
    var addToCalendarClicked by remember { mutableStateOf(false )}

    val addToCalendarResult = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { _ ->
        //
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TopAppBar(
            title = { Text(text = "Visualizar Alerta", color = Color.Black) },
            Modifier.background(Color.White),
            navigationIcon = {
                IconButton(
                    onClick = { onBack() }
                ) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                }
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Alert Name: ${alertEntity.medicineId}",
            style = MaterialTheme.typography.body1
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Notes: ${alertEntity.notes}",
            style = MaterialTheme.typography.body1
        )

        Button(
            onClick = {
                addToCalendarClicked = true

                val intent = Intent(Intent.ACTION_INSERT)
                    .setData(CalendarContract.Events.CONTENT_URI)
                    .putExtra(CalendarContract.Events.TITLE, "Medicine: ${alertEntity.medicineId}")
                    .putExtra(
                        CalendarContract.EXTRA_EVENT_BEGIN_TIME,
                        calculateEventTime(alertEntity.hours, alertEntity.minutes)
                    )
                    .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, calculateEventEndTime(alertEntity.hours, alertEntity.minutes, 1))
                    .putExtra(CalendarContract.Events.DESCRIPTION, alertEntity.notes)

                addToCalendarResult.launch(intent)
            }
        ) {
            Text(text = "Add to Calendar")
        }
    }
}

@Composable
fun AlertInputSection(
    selectedHour: Int,
    selectedMinute: Int,
    dose: String,
    notes: String,
    medicineList: List<MedicineEntity>,
    selectedMedicine: MedicineEntity?,
    onHoursChange: (Int) -> Unit,
    onMinutesChange: (Int) -> Unit,
    onDoseChange: (String) -> Unit,
    onNotesChange: (String) -> Unit,
    onMedicineSelected: (MedicineEntity?) -> Unit,
    onAddItemClick: (AlertEntity) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedHour by remember { mutableStateOf(0) }
    var selectedMinute by remember { mutableStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colors.surface)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text("Horas", style = TextStyle(fontWeight = FontWeight.Bold))
                HourMinuteSelect(
                    label = "Horas",
                    items = (0..23).map { it.toString() },
                    selectedItem = selectedHour.toString(),
                    onItemSelected = { hour ->
                        selectedHour = hour.toInt()
                    }
                )
            }
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text("Minutos", style = TextStyle(fontWeight = FontWeight.Bold))
                HourMinuteSelect(
                    label = "Minutos",
                    items = (0..59).map { it.toString() },
                    selectedItem = selectedMinute.toString(),
                    onItemSelected = { minute ->
                        selectedMinute = minute.toInt()
                    }
                )
            }
        }
        PlaceholderTextField(
            modifier = Modifier
                .padding(16.dp),
            value = dose,
            onValueChange = {
                onDoseChange(it)
            },
            textStyle = MaterialTheme.typography.body1,
            placeholder = "Dose"
        )
        PlaceholderTextField(
            modifier = Modifier
                .padding(16.dp),
            value = notes,
            onValueChange = {
                onNotesChange(it)
            },
            textStyle = MaterialTheme.typography.body1,
            placeholder = "Observações"
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colors.surface)
                .clickable { expanded = !expanded }
        ) {
            Text(
                text = selectedMedicine?.name ?: "Selecione um medicamento",
                style = MaterialTheme.typography.body1,
                modifier = Modifier.padding(16.dp),
            )

            if (expanded) {
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    medicineList.forEach { medicine ->
                        DropdownMenuItem(
                            onClick = {
                                onMedicineSelected(medicine)
                                expanded = false
                            }
                        ) {
                            Text(text = medicine.name)
                        }
                    }
                }
            }
        }
        Button(
            onClick = {
                if (selectedHour > 0 && selectedMinute > 0 && selectedMedicine?.id ?: 0 > 0) {
                    val alertEntity = AlertEntity(
                        hours = selectedHour,
                        minutes = selectedMinute,
                        dose = dose,
                        notes = notes,
                        medicineId = selectedMedicine?.id ?: 0
                    )
                    onAddItemClick(alertEntity)
                }
            }
        ) {
            Text(text = "Salvar")
        }
    }
}
@Composable
fun HourMinuteSelect(
    label: String,
    items: List<String>,
    selectedItem: String,
    onItemSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colors.surface)
            .clickable { expanded = true }
    ) {
        Text(
            text = selectedItem,
            style = MaterialTheme.typography.body1,
            modifier = Modifier.padding(16.dp),
        )

        if (expanded) {
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                items.forEach { item ->
                    DropdownMenuItem(
                        onClick = {
                            onItemSelected(item)
                            expanded = false
                        }
                    ) {
                        Text(text = item)
                    }
                }
            }
        }
    }
}

// Calculate the event start time (milliseconds since epoch) based on hours and minutes
fun calculateEventTime(hours: Int, minutes: Int): Long {
    val calendar = Calendar.getInstance()
    calendar.set(Calendar.HOUR_OF_DAY, hours)
    calendar.set(Calendar.MINUTE, minutes)
    return calendar.timeInMillis
}

// Calculate the event end time by adding the event duration to the start time
fun calculateEventEndTime(hours: Int, minutes: Int, eventDurationMinutes: Int): Long {
    val startTime = calculateEventTime(hours, minutes)
    val endTime = startTime + eventDurationMinutes * 60 * 1000 // Convert minutes to milliseconds
    return endTime
}