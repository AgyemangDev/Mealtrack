package com.example.diettracker.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.diettracker.viewmodel.SettingsViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingScreen(settingsViewModel: SettingsViewModel = viewModel()) {
    val userName by settingsViewModel.userName.collectAsState()
    val userEmail by settingsViewModel.userEmail.collectAsState()
    val calorieGoal by settingsViewModel.calorieGoal.collectAsState()
    val proteinGoal by settingsViewModel.proteinGoal.collectAsState()
    val carbsGoal by settingsViewModel.carbsGoal.collectAsState()
    val fatGoal by settingsViewModel.fatGoal.collectAsState()
    val weightGoal by settingsViewModel.weightGoal.collectAsState()
    val darkModeEnabled by settingsViewModel.darkModeEnabled.collectAsState()
    val notificationsEnabled by settingsViewModel.notificationsEnabled.collectAsState()
    val unitSystem by settingsViewModel.unitSystem.collectAsState()

    var showEditProfileDialog by remember { mutableStateOf(false) }
    var showGoalsDialog by remember { mutableStateOf(false) }
    var showWeightGoalDialog by remember { mutableStateOf(false) }
    var showUnitsDialog by remember { mutableStateOf(false) }
    var showClearDataDialog by remember { mutableStateOf(false) }
    var showLogoutDialog by remember { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Settings", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF5F5F5))
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            // Profile Section
            ProfileCard(userName, userEmail) { showEditProfileDialog = true }

            Spacer(modifier = Modifier.height(16.dp))

            // Goals Section
            SettingsSection(title = "Goals") {
                SettingsItem(
                    icon = Icons.Default.Info,
                    title = "Daily Nutrition Goals",
                    subtitle = "$calorieGoal kcal • ${proteinGoal}g protein • ${carbsGoal}g carbs • ${fatGoal}g fat",
                    onClick = { showGoalsDialog = true }
                )
                SettingsItem(
                    icon = Icons.Default.Star,
                    title = "Weight Goal",
                    subtitle = if (weightGoal > 0) "Target: $weightGoal kg" else "Not set",
                    onClick = { showWeightGoalDialog = true }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Preferences Section
            SettingsSection(title = "Preferences") {
                SettingsSwitchItem(
                    icon = Icons.Default.DateRange,
                    title = "Dark Mode",
                    subtitle = "Enable dark theme",
                    checked = darkModeEnabled,
                    onCheckedChange = { settingsViewModel.setDarkMode(it) }
                )
                SettingsSwitchItem(
                    icon = Icons.Default.Notifications,
                    title = "Notifications",
                    subtitle = "Meal reminders and updates",
                    checked = notificationsEnabled,
                    onCheckedChange = { settingsViewModel.setNotifications(it) }
                )
                SettingsItem(
                    icon = Icons.Default.Settings,
                    title = "Units",
                    subtitle = unitSystem,
                    onClick = { showUnitsDialog = true }
                )
            }
            
            // Other sections would go here...

            Spacer(modifier = Modifier.height(32.dp))

            // Logout Button
            LogoutButton { showLogoutDialog = true }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }

    // -- Dialogs --

    if (showEditProfileDialog) {
        EditProfileDialog(currentName = userName, currentEmail = userEmail, onDismiss = { showEditProfileDialog = false }) { name, email ->
            settingsViewModel.updateProfile(name, email)
            showEditProfileDialog = false
        }
    }

    if (showGoalsDialog) {
        GoalsDialog(currentCalories = calorieGoal, currentProtein = proteinGoal, currentCarbs = carbsGoal, currentFat = fatGoal, onDismiss = { showGoalsDialog = false }) { calories, protein, carbs, fat ->
            settingsViewModel.updateNutritionGoals(calories, protein, carbs, fat)
            showGoalsDialog = false
        }
    }

    if (showWeightGoalDialog) {
        WeightGoalDialog(currentWeight = weightGoal, onDismiss = { showWeightGoalDialog = false }) { weight ->
            settingsViewModel.setWeightGoal(weight)
            showWeightGoalDialog = false
        }
    }

    if (showUnitsDialog) {
        UnitsDialog(currentUnit = unitSystem, onDismiss = { showUnitsDialog = false }) { unit ->
            settingsViewModel.setUnitSystem(unit)
            showUnitsDialog = false
        }
    }

    if (showClearDataDialog) {
        AlertDialog(
            onDismissRequest = { showClearDataDialog = false },
            title = { Text("Clear History") },
            text = { Text("Are you sure you want to delete all meal records? This action cannot be undone.") },
            confirmButton = {
                Button(onClick = {
                    settingsViewModel.clearHistory()
                    showClearDataDialog = false
                    scope.launch { snackbarHostState.showSnackbar("All meal records have been deleted.") }
                }, colors = ButtonDefaults.buttonColors(containerColor = Color.Red)) {
                    Text("Clear Data")
                }
            },
            dismissButton = { Button(onClick = { showClearDataDialog = false }) { Text("Cancel") } }
        )
    }

    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text("Logout") },
            text = { Text("Are you sure you want to log out?") },
            confirmButton = {
                Button(onClick = {
                    settingsViewModel.logout()
                    showLogoutDialog = false
                    // Navigation logic to go back to login screen should be handled here
                }) { Text("Logout") }
            },
            dismissButton = { Button(onClick = { showLogoutDialog = false }) { Text("Cancel") } }
        )
    }
}

// -- Reusable Composables (Placeholders) --

@Composable
fun ProfileCard(userName: String, userEmail: String, onEditClick: () -> Unit) {
    Surface(modifier = Modifier.fillMaxWidth(), color = Color.White) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier.size(100.dp).clip(CircleShape).background(Color(0xFF668405)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = userName.split(" ").take(2).mapNotNull { it.firstOrNull()?.uppercaseChar() }.joinToString(""),
                    fontSize = 36.sp, fontWeight = FontWeight.Bold, color = Color.White
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = userName, fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Text(text = userEmail, fontSize = 14.sp, color = Color.Gray)
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = onEditClick, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF668405))) {
                Icon(imageVector = Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Edit Profile")
            }
        }
    }
}

@Composable
fun SettingsSection(title: String, content: @Composable ColumnScope.() -> Unit) {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Text(text = title, fontWeight = FontWeight.Bold, modifier = Modifier.padding(vertical = 8.dp))
        Surface(modifier = Modifier.fillMaxWidth(), shape = MaterialTheme.shapes.medium, color = Color.White) {
            Column { content() }
        }
    }
}

@Composable
fun SettingsItem(icon: ImageVector, title: String, subtitle: String, onClick: () -> Unit, textColor: Color = Color.Unspecified) {
    Row(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick).padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(imageVector = icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, fontWeight = FontWeight.Medium, color = textColor)
            Text(text = subtitle, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
        }
        Icon(imageVector = Icons.Default.ChevronRight, contentDescription = null, tint = Color.Gray)
    }
}

@Composable
fun SettingsSwitchItem(icon: ImageVector, title: String, subtitle: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(modifier = Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
        Icon(imageVector = icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, fontWeight = FontWeight.Medium)
            Text(text = subtitle, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
        }
        Switch(checked = checked, onCheckedChange = onCheckedChange)
    }
}

@Composable
fun LogoutButton(onLogoutClick: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
        color = Color.White, shape = MaterialTheme.shapes.medium
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().clickable(onClick = onLogoutClick).padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(imageVector = Icons.Default.ExitToApp, contentDescription = null, tint = Color.Red)
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "Logout", color = Color.Red, fontWeight = FontWeight.Medium)
        }
    }
}

@Composable
fun EditProfileDialog(currentName: String, currentEmail: String, onDismiss: () -> Unit, onSave: (String, String) -> Unit) {
    var name by remember { mutableStateOf(currentName) }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Edit Profile") },
        text = {
            Column {
                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Full Name") })
                OutlinedTextField(value = currentEmail, onValueChange = {}, label = { Text("Email") }, enabled = false)
            }
        },
        confirmButton = { Button(onClick = { onSave(name, currentEmail) }) { Text("Save") } },
        dismissButton = { Button(onClick = onDismiss) { Text("Cancel") } }
    )
}

@Composable
fun GoalsDialog(currentCalories: Int, currentProtein: Int, currentCarbs: Int, currentFat: Int, onDismiss: () -> Unit, onSave: (Int, Int, Int, Int) -> Unit) {
    var calories by remember { mutableStateOf(currentCalories.toString()) }
    // Add states for protein, carbs, fat
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Edit Daily Goals") },
        text = {
            Column {
                OutlinedTextField(value = calories, onValueChange = { calories = it }, label = { Text("Calories") })
                // Add fields for protein, carbs, fat here
            }
        },
        confirmButton = { Button(onClick = { onSave(calories.toIntOrNull() ?: 0, currentProtein, currentCarbs, currentFat) }) { Text("Save") } },
        dismissButton = { Button(onClick = onDismiss) { Text("Cancel") } }
    )
}

@Composable
fun WeightGoalDialog(currentWeight: Int, onDismiss: () -> Unit, onSave: (Int) -> Unit) {
    var weight by remember { mutableStateOf(currentWeight.toString()) }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Edit Weight Goal") },
        text = { OutlinedTextField(value = weight, onValueChange = { weight = it }, label = { Text("Weight (kg)") }) },
        confirmButton = { Button(onClick = { onSave(weight.toIntOrNull() ?: 0) }) { Text("Save") } },
        dismissButton = { Button(onClick = onDismiss) { Text("Cancel") } }
    )
}

@Composable
fun UnitsDialog(currentUnit: String, onDismiss: () -> Unit, onSelect: (String) -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Select Unit System") },
        text = {
            Column {
                Row(Modifier.fillMaxWidth().clickable { onSelect("Metric") }.padding(vertical = 8.dp)) {
                    RadioButton(selected = currentUnit == "Metric", onClick = { onSelect("Metric") })
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Metric (kg, g)")
                }
                Row(Modifier.fillMaxWidth().clickable { onSelect("Imperial") }.padding(vertical = 8.dp)) {
                    RadioButton(selected = currentUnit == "Imperial", onClick = { onSelect("Imperial") })
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Imperial (lbs, oz)")
                }
            }
        },
        confirmButton = { Button(onClick = onDismiss) { Text("Close") } }
    )
}