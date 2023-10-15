package com.example.relembre.ui

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.relembre.R
import com.example.relembre.model.AlertEntity
import com.example.relembre.notifications.AlertReceiver
import com.example.relembre.ui.theme.ReLembreTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import java.util.*

private const val CHANNEL_ID = "medicine"
private const val ALERT_ID = 123

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createNotificationChannel(this)
        setContent {
            ReLembreTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    var showMainContent by remember { mutableStateOf(false) }

                    if (showMainContent) {
                        Navigation()
                    } else {
                        SplashScreen()
                    }

                    LaunchedEffect(Unit) {
                        delay(2000)
                        showMainContent = true
                    }
                }
            }
        }
    }

    fun scheduleAlarmManager(alertEntity: AlertEntity) {
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, AlertReceiver::class.java)
        val flag = PendingIntent.FLAG_IMMUTABLE
        val pendingIntent = PendingIntent.getBroadcast(this, ALERT_ID, intent, flag)


        val triggerTimeMillis = calculateTriggerTimeMillis(alertEntity.hours, alertEntity.minutes)
        alarmManager.set(AlarmManager.RTC, triggerTimeMillis, pendingIntent)
    }

    fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager = context.getSystemService(NotificationManager::class.java)
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Medicine Reminders",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }
    }

}

@Composable
fun Home(navController: NavHostController, viewModel: MainViewModel = hiltViewModel()) {
    val data = viewModel.allAlertsWithMedicine.collectAsState(initial = emptyList()).value

    val filteredData = data.filter { alert ->
        val alertTime = Calendar.getInstance().apply {
            timeZone = TimeZone.getDefault()

            set(Calendar.HOUR_OF_DAY, alert.alert.hours)
            set(Calendar.MINUTE, alert.alert.minutes)
            set(Calendar.SECOND, 0)
        }

        val currentTime = Calendar.getInstance().apply {
            timeZone = TimeZone.getDefault()
        }

        alertTime > currentTime
    }

    println("filtereddataSize:"+filteredData.size)

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top
    ) {
        TopAppBar(
            title = { Text(text = "Hoje", color = Color.Black) },
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
                filteredData,
                onItemClick = {
                    viewModel.getAlert(it.id)
                },
                onDeleteClick = {
                    viewModel.removeAlert(it)
                },
                navController
            )
            if (data.isNotEmpty()) {
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
}

@Composable
fun NavigationController(navController: NavHostController, viewModel: MainViewModel = hiltViewModel()) {
    NavHost(navController = navController, startDestination = NavigationItem.Hoje.route) {

        composable(NavigationItem.Hoje.route) {
            Home(navController)
        }

        composable(NavigationItem.Medicamentos.route) {
            Medicamentos(navController)
        }

        composable(NavigationItem.CadastroMedicamento.route) {
            CadastroMedicamento(navController) {
                navController.navigateUp() // Navigate back when the function is invoked
            }
        }

        composable(
            "${NavigationItem.VisualizarMedicamento.route}/{itemId}",
            arguments = listOf(navArgument("itemId") { type = NavType.LongType })
        ) { backStackEntry ->
            val itemId = backStackEntry.arguments?.getLong("itemId") ?: 0
            val medicineEntity = viewModel.getMedicine(itemId).collectAsState(null).value

            if (medicineEntity != null) {
                VisualizarMedicamento(medicineEntity) {
                    navController.navigateUp() // Navigate back when the function is invoked
                }
            }
        }

        composable(NavigationItem.Alertas.route) {
            Alertas(navController)
        }

        composable(NavigationItem.CadastroAlerta.route) {
            val mainActivity = LocalContext.current as MainActivity
            CadastroAlerta(navController, viewModel, mainActivity)  {
                navController.navigateUp() // Navigate back when the function is invoked
            }
        }

        composable(
            "${NavigationItem.VisualizarAlerta.route}/{itemId}",
            arguments = listOf(navArgument("itemId") { type = NavType.LongType })
        ) { backStackEntry ->
            val itemId = backStackEntry.arguments?.getLong("itemId") ?: 0
            val alertEntity = viewModel.getAlert(itemId).collectAsState(null).value

            if (alertEntity != null) {
                VisualizarAlerta(alertEntity) {
                    navController.navigateUp() // Navigate back when the function is invoked
                }
            }
        }
    }
}


@Composable
fun Navigation() {

    val navController = rememberNavController()

    val items = listOf(
        NavigationItem.Hoje,
        NavigationItem.Medicamentos,
        NavigationItem.Alertas
    )


    Scaffold(topBar = { TopAppBar(title = { Text(text = "ReLembre") }) },
        bottomBar = {
            BottomNavigation(backgroundColor = MaterialTheme.colors.background) {

                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route


                items.forEach {
                    BottomNavigationItem(selected = currentRoute == it.route,
                        label = {
                            Text(
                                text = it.label,
                                color = if (currentRoute == it.route) Color.DarkGray else Color.LightGray
                            )
                        },
                        icon = {
                            Icon(
                                imageVector = it.icons, contentDescription = null,
                                tint = if (currentRoute == it.route) Color.DarkGray else Color.LightGray
                            )

                        },

                        onClick = {
                            if(currentRoute!=it.route){

                                navController.graph?.startDestinationRoute?.let {
                                    navController.popBackStack(it,true)
                                }

                                navController.navigate(it.route){
                                    launchSingleTop = true
                                }

                            }

                        })

                }


            }


        }) {

        NavigationController(navController = navController)

    }

}


@Composable
fun PlaceholderTextField(
    modifier: Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    textStyle: TextStyle,
    placeholder: String,

    ) {

    var isFocused by remember {
        mutableStateOf(false)
    }

    Box(
        modifier = modifier
            .background(Color.White, RoundedCornerShape(8.dp))
            .border(
                width = 2.dp,
                color = if (isFocused) MaterialTheme.colors.primary else Color.LightGray,
                shape = RoundedCornerShape(8.dp)
            )
    ) {
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            textStyle = textStyle,
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .onFocusChanged {
                    isFocused = it.isFocused
                }
                .focusRequester(FocusRequester())
        )

        if (value.isEmpty()) {
            Text(
                text = placeholder,
                style = textStyle.copy(color = Color.Gray),
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

@Composable
fun Alertas() {
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
            Text(text = "Alertas")

        }
    }
}

@Composable
fun SplashScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = null,
            modifier = Modifier.fillMaxSize()
        )
    }
}

fun calculateTriggerTimeMillis(hours: Int, minutes: Int): Long {
    val calendar = Calendar.getInstance()
    val currentTimeMillis = calendar.timeInMillis
    calendar.set(Calendar.HOUR_OF_DAY, hours)
    calendar.set(Calendar.MINUTE, minutes)
    calendar.set(Calendar.SECOND, 0)
    val triggerTimeMillis = calendar.timeInMillis

    if (triggerTimeMillis <= currentTimeMillis) {
        calendar.add(Calendar.DAY_OF_YEAR, 1)
        return calendar.timeInMillis
    }

    return triggerTimeMillis - currentTimeMillis
}

