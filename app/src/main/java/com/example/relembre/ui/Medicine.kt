package com.example.relembre.ui

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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.relembre.model.MedicineEntity

@Composable
fun Medicamentos(navController: NavHostController, viewModel: MainViewModel = hiltViewModel()) {
    val data = viewModel.allMedicines.collectAsState(initial = emptyList())

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top
    ) {
        TopAppBar(
            title = { Text(text = "Meus Medicamentos", color = Color.Black) },
            modifier = Modifier.background(Color.White)
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.BottomEnd
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            MedicineListSection(
                data,
                onItemClick = {
                    viewModel.getMedicine(it.id)
                },
                onDeleteClick = {
                    viewModel.removeMedicine(it)
                },
                navController
            )

            FloatingActionButton(
                onClick = {
                    navController.navigate(NavigationItem.CadastroMedicamento.route)
                },
                content = {
                    IconButton(
                        onClick = {
                            navController.navigate(NavigationItem.CadastroMedicamento.route)
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
fun MedicineListSection(
    list: State<List<MedicineEntity>>,
    onItemClick: (MedicineEntity) -> Unit,
    onDeleteClick: (MedicineEntity) -> Unit,
    navController: NavHostController // Add NavHostController parameter
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp)
    ) {
        items(list.value) { item ->
            MedicineItemCard(item, onItemClick, onDeleteClick, navController)
        }
    }
}

@Composable
fun MedicineItemCard(
    item: MedicineEntity,
    onItemClick: (MedicineEntity) -> Unit,
    onDeleteClick: (MedicineEntity) -> Unit,
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
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Clickable item text
            Text(
                text = item.name,
                modifier = Modifier
                    .weight(1f)
                    .clickable {
                        onItemClick(item)
                        // Navigate to VisualizarMedicamento when the item is clicked
                        navController.navigate("${NavigationItem.VisualizarMedicamento.route}/${item.id}")
                    },
                style = MaterialTheme.typography.body1
            )

            // Delete icon
            IconButton(onClick = {
                onDeleteClick(item)
            }) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CadastroMedicamento(navController: NavHostController, viewModel: MainViewModel = hiltViewModel(), onBack: () -> Unit) {

    var medicineName by remember { mutableStateOf(("")) }
    var notes by remember { mutableStateOf(("")) }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .background(Color.White)
        ) {
            TopAppBar(
                title = { Text(text = "Cadastrar Medicamento", color = Color.Black) },
                navigationIcon = {
                    IconButton(
                        onClick = { onBack() }
                    ) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 56.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            MedicineInputSection(
                medicineName = medicineName,
                notes = notes,
                onMedicineNameChange = {
                    medicineName = it
                },
                onNotesChange = {
                    notes = it
                },
                onAddItemClick = { medicineEntity ->
                    if (medicineEntity.name.isNotBlank()) {
                        viewModel.addMedicine(medicineEntity.name, medicineEntity.notes)
                        medicineName = ""
                        notes = ""
                        navController.navigateUp()
                    }
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VisualizarMedicamento(
    medicineEntity: MedicineEntity,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TopAppBar(
            title = { Text(text = "Visualizar Medicamento", color = Color.Black) },
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
            text = "Nome: ${medicineEntity.name}",
            style = MaterialTheme.typography.body1
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Observações: ${medicineEntity.notes}",
            style = MaterialTheme.typography.body1
        )
    }
}

@Composable
fun MedicineInputSection(
    medicineName: String,
    notes: String,
    onMedicineNameChange: (String) -> Unit,
    onNotesChange: (String) -> Unit,
    onAddItemClick: (MedicineEntity) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colors.surface)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        PlaceholderTextField(
            value = medicineName,
            onValueChange = {
                onMedicineNameChange(it)
            },
            textStyle = MaterialTheme.typography.body1,
            placeholder = "Nome",
            modifier = Modifier
                .weight(1f)
                .padding(16.dp)
        )
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colors.surface)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        PlaceholderTextField(
            value = notes,
            onValueChange = {
                onNotesChange(it)
            },
            textStyle = MaterialTheme.typography.body1,
            placeholder = "Observações",
            modifier = Modifier
                .weight(1f)
                .padding(16.dp)
        )
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colors.surface)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Button(
            onClick = {
                if (medicineName.isNotBlank()) {
                    val medicineEntity = MedicineEntity(medicineName, notes)
                    onAddItemClick(medicineEntity)
                }
            }
        ) {
            Text(text = "Salvar")
        }
    }
}