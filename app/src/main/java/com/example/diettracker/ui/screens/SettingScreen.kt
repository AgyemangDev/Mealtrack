package com.example.diettracker.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
                title = {
                    Text(
                        text = "Settings",
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
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
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = Color.White
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Profile Picture with initials
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF668405)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = userName.split(" ")
                                .take(2)
                                .mapNotNull { it.firstOrNull()?.uppercaseChar() }
                                .joinToString(""),
                            fontSize = 36.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = userName,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = userEmail,
                        fontSize = 14.sp,
                        color = Color.Gray
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = { showEditProfileDialog = true },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF668405)
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Edit Profile")
                    }
                }
            }

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

            Spacer(modifier = Modifier.height(16.dp))

            // Data & Privacy Section
            SettingsSection(title = "Data & Privacy") {
                SettingsItem(
                    icon = Icons.Default.Share,
                    title = "Export Data",
                    subtitle = "Download your nutrition data",
                    onClick = {
                        settingsViewModel.exportData()
                        kotlinx.coroutines.launch {
                            snackbarHostState.showSnackbar("Data export feature coming soon")
                        }
                    }
                )
                SettingsItem(
                    icon = Icons.Default.Delete,
                    title = "Clear History",
                    subtitle = "Delete all meal records",
                    onClick = { showClearDataDialog = true },
                    textColor = Color.Red
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // About Section
            SettingsSection(title = "About") {
                SettingsItem(
                    icon = Icons.Default.Info,
                    title = "App Version",
                    subtitle = "1.0.0",
                    onClick = { }
                )
                SettingsItem(
                    icon = Icons.Default.Email,
                    title = "Contact Support",
                    subtitle = "Get help with the app",
                    onClick = {
                        kotlinx.coroutines.launch {
                            snackbarHostState.showSnackbar("Opening email client...")
                        }
                    }
                )
                SettingsItem(
                    icon = Icons.Default.CheckCircle,
                    title = "Terms & Privacy",
                    subtitle = "Read our policies",
                    onClick = {
                        kotlinx.coroutines.launch {
                            snackbarHostState.showSnackbar("Opening privacy policy...")
                        }
                    }
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Logout Button
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                color = Color.White,
                shape = MaterialTheme.shapes.medium
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showLogoutDialog = true }
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.ExitToApp,
                        contentDescription = null,
                        tint = Color.Red
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Logout",
                        color = Color.Red,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }

    // Edit Profile Dialog
    if (showEditProfileDialog) {
        EditProfileDialog(
            currentName = userName,
            currentEmail = userEmail,
            onDismiss = { showEditProfileDialog = false },
            onSave = { name, email ->
                settingsViewModel.updateProfile(name, email)
                showEditProfileDialog = false
            }
        )
    }

    // Goals Dialog
    if (showGoalsDialog) {
        GoalsDialog(
            currentCalories = calorieGoal,
            currentProtein = proteinGoal,
            currentCarbs = carbsGoal,
            currentFat = fatGoal,
            onDismiss = { showGoalsDialog = false },
            onSave = { calories, protein, carbs, fat ->
                settingsViewModel.updateNutritionGoals(calories, protein, carbs, fat)
                showGoalsDialog = false
            }
        )
    }

    // Weight Goal Dialog
    if (showWeightGoalDialog) {
        WeightGoalDialog(
            currentWeight = weightGoal,
            onDismiss = { showWeightGoalDialog = false },
            onSave = { weight ->
                settingsViewModel.setWeightGoal(weight)
                showWeightGoalDialog = false
            }
        )
    }

    // Units Dialog
    if (showUnitsDialog) {
        UnitsDialog(
            currentUnit = unitSystem,
            onDismiss = { showUnitsDialog = false },
            onSelect = { unit ->
                settingsViewModel.setUnitSystem(unit)
                showUnitsDialog = false
            }
        )
    }

    // Clear Data Confirmation Dialog
    if (showClearDataDialog) {
        AlertDialog(
            onDismissRequest = { showClearDataDialog = false },
            title = { Text("Clear All Data?") },
            text = { Text("This will permanently delete all your meal records. This action cannot be undone.") },
            confirmButton = {
                Button(
                    onClick = {
                        settingsViewModel.clearAllData()
                        showClearDataDialog = false
                        kotlinx.coroutines.launch {
                            snackbarHostState.showSnackbar("All data cleared")
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                ) {
                    Text("Delete All")
                }
            },
            dismissButton = {
                TextButton(onClick = { showClearDataDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    // Logout Confirmation Dialog
    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text("Logout") },
            text = { Text("Are you sure you want to logout?") },
            confirmButton = {
                Button(
                    onClick = {
                        settingsViewModel.logout()
                        showLogoutDialog = false
                        kotlinx.coroutines.launch {
                            snackbarHostState.showSnackbar("Logged out successfully")
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                ) {
                    Text("Logout")
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun SettingsSection(
    title: String,
    content: @Composable () -> Unit
) {
    Column {
        Text(
            text = title,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.Gray,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = Color.White
        ) {
            Column {
                content()
            }
        }
    }
}

@Composable
fun SettingsItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit,
    textColor: Color = Color.Black
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = if (textColor == Color.Red) Color.Red else Color(0xFF668405),
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontWeight = FontWeight.Medium,
                color = textColor
            )
            Text(
                text = subtitle,
                fontSize = 12.sp,
                color = Color.Gray
            )
        }
        Icon(
            imageVector = Icons.Default.KeyboardArrowRight,
            contentDescription = null,
            tint = Color.Gray
        )
    }
}

@Composable
fun SettingsSwitchItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color(0xFF668405),
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = subtitle,
                fontSize = 12.sp,
                color = Color.Gray
            )
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = Color(0xFF668405)
            )
        )
    }
}

@Composable
fun EditProfileDialog(
    currentName: String,
    currentEmail: String,
    onDismiss: () -> Unit,
    onSave: (String, String) -> Unit
) {
    var name by remember { mutableStateOf(currentName) }
    var email by remember { mutableStateOf(currentEmail) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Edit Profile") },
        text = {
            Column {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Name") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onSave(name, email) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF668405)
                ),
                enabled = name.isNotBlank() && email.isNotBlank()
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun GoalsDialog(
    currentCalories: Int,
    currentProtein: Int,
    currentCarbs: Int,
    currentFat: Int,
    onDismiss: () -> Unit,
    onSave: (Int, Int, Int, Int) -> Unit
) {
    var calories by remember { mutableStateOf(currentCalories.toString()) }
    var protein by remember { mutableStateOf(currentProtein.toString()) }
    var carbs by remember { mutableStateOf(currentCarbs.toString()) }
    var fat by remember { mutableStateOf(currentFat.toString()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Daily Nutrition Goals") },
        text = {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState())
            ) {
                OutlinedTextField(
                    value = calories,
                    onValueChange = { calories = it.filter { char -> char.isDigit() } },
                    label = { Text("Calories (kcal)") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = protein,
                    onValueChange = { protein = it.filter { char -> char.isDigit() } },
                    label = { Text("Protein (g)") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = carbs,
                    onValueChange = { carbs = it.filter { char -> char.isDigit() } },
                    label = { Text("Carbs (g)") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = fat,
                    onValueChange = { fat = it.filter { char -> char.isDigit() } },
                    label = { Text("Fat (g)") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val cal = calories.toIntOrNull() ?: currentCalories
                    val pro = protein.toIntOrNull() ?: currentProtein
                    val car = carbs.toIntOrNull() ?: currentCarbs
                    val ft = fat.toIntOrNull() ?: currentFat
                    onSave(cal, pro, car, ft)
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF668405)
                )
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun WeightGoalDialog(
    currentWeight: Int,
    onDismiss: () -> Unit,
    onSave: (Int) -> Unit
) {
    var weight by remember { mutableStateOf(if (currentWeight > 0) currentWeight.toString() else "") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Set Weight Goal") },
        text = {
            OutlinedTextField(
                value = weight,
                onValueChange = { weight = it.filter { char -> char.isDigit() } },
                label = { Text("Target Weight (kg)") },
                modifier = Modifier.fillMaxWidth()
            )
        },
        confirmButton = {
            Button(
                onClick = {
                    val w = weight.toIntOrNull() ?: 0
                    onSave(w)
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF668405)
                )
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun UnitsDialog(
    currentUnit: String,
    onDismiss: () -> Unit,
    onSelect: (String) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Choose Unit System") },
        text = {
            Column {
                RadioButtonOption(
                    text = "Metric (kg, cm)",
                    selected = currentUnit == "Metric (kg, cm)",
                    onClick = { onSelect("Metric (kg, cm)") }
                )
                RadioButtonOption(
                    text = "Imperial (lb, in)",
                    selected = currentUnit == "Imperial (lb, in)",
                    onClick = { onSelect("Imperial (lb, in)") }
                )
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Close")
            }
        }
    )
}

@Composable
fun RadioButtonOption(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = selected,
            onClick = onClick,
            colors = RadioButtonDefaults.colors(
                selectedColor = Color(0xFF668405)
            )
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = text)
    }
}