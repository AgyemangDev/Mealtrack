package com.example.diettracker.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.diettracker.viewmodel.SettingsViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingScreen(
    settingsViewModel: SettingsViewModel = viewModel(),
    onLogoutComplete: () -> Unit = {}
) {
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
            ProfileCard(userName, userEmail) { showEditProfileDialog = true }

            Spacer(modifier = Modifier.height(16.dp))

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

            SettingsSection(title = "Preferences") {

                SettingsItem(
                    icon = Icons.Default.Speed,
                    title = "Unit System",
                    subtitle = unitSystem,
                    onClick = {
                        scope.launch {
                            snackbarHostState.showSnackbar("Unit system preferences coming soon")
                        }
                    }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            LogoutButton { showLogoutDialog = true }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }

    if (showEditProfileDialog) {
        EditProfileDialog(
            currentName = userName,
            onDismiss = { showEditProfileDialog = false }
        ) { name ->
            settingsViewModel.updateProfile(name, userEmail)
            showEditProfileDialog = false
        }
    }

    if (showGoalsDialog) {
        GoalsDialog(
            currentCalories = calorieGoal,
            currentProtein = proteinGoal,
            currentCarbs = carbsGoal,
            currentFat = fatGoal,
            onDismiss = { showGoalsDialog = false }
        ) { c, p, carbs, f ->
            settingsViewModel.updateNutritionGoals(c, p, carbs, f)
            showGoalsDialog = false
        }
    }

    if (showWeightGoalDialog) {
        WeightGoalDialog(
            currentWeight = weightGoal,
            onDismiss = { showWeightGoalDialog = false }
        ) { weight ->
            settingsViewModel.setWeightGoal(weight)
            showWeightGoalDialog = false
        }
    }

    // Logout confirmation dialog
    if (showLogoutDialog) {
        val isLoggingOut by settingsViewModel.isLoggingOut.collectAsState()

        AlertDialog(
            onDismissRequest = { if (!isLoggingOut) showLogoutDialog = false },
            title = { Text("Logout") },
            text = {
                if (isLoggingOut) {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CircularProgressIndicator(modifier = Modifier.size(24.dp))
                        Spacer(modifier = Modifier.width(16.dp))
                        Text("Logging out...")
                    }
                } else {
                    Text("Are you sure you want to log out?")
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        scope.launch {
                            try {
                                settingsViewModel.logoutSuspend()
                                showLogoutDialog = false
                                onLogoutComplete()
                            } catch (e: Exception) {
                                snackbarHostState.showSnackbar("Logout failed: ${e.message}")
                                showLogoutDialog = false
                            }
                        }
                    },
                    enabled = !isLoggingOut,
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                ) {
                    Text("Logout")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showLogoutDialog = false },
                    enabled = !isLoggingOut
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun ProfileCard(userName: String, userEmail: String, onEditClick: () -> Unit) {
    Surface(modifier = Modifier.fillMaxWidth(), color = Color.White) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF668405)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = userName.split(" ").take(2)
                        .mapNotNull { it.firstOrNull()?.uppercaseChar() }
                        .joinToString(""),
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = userName, fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Text(text = userEmail, fontSize = 14.sp, color = Color.Gray)
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = onEditClick,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF668405))
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
}

@Composable
fun SettingsSection(title: String, content: @Composable ColumnScope.() -> Unit) {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Text(
            text = title,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 8.dp)
        )
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.medium,
            color = Color.White
        ) {
            Column { content() }
        }
    }
}

@Composable
fun SettingsItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit,
    textColor: Color = Color.Unspecified
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
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, fontWeight = FontWeight.Medium, color = textColor)
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
        }
        Icon(
            imageVector = Icons.Default.ChevronRight,
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
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, fontWeight = FontWeight.Medium)
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
        }
        Switch(checked = checked, onCheckedChange = onCheckedChange)
    }
}

@Composable
fun LogoutButton(onLogoutClick: () -> Unit) {
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
                .clickable(onClick = onLogoutClick)
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
            Text(text = "Logout", color = Color.Red, fontWeight = FontWeight.Medium)
        }
    }
}

@Composable
fun EditProfileDialog(
    currentName: String,
    onDismiss: () -> Unit,
    onSave: (String) -> Unit
) {
    var name by remember { mutableStateOf(currentName) }
    var isError by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Edit Profile") },
        text = {
            Column {
                OutlinedTextField(
                    value = name,
                    onValueChange = {
                        name = it
                        isError = it.isBlank()
                    },
                    label = { Text("Full Name") },
                    isError = isError,
                    supportingText = if (isError) {
                        { Text("Name cannot be empty") }
                    } else null
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { if (name.isNotBlank()) onSave(name) },
                enabled = name.isNotBlank()
            ) {
                Text("Save")
            }
        },
        dismissButton = { Button(onClick = onDismiss) { Text("Cancel") } }
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

    val isValid = calories.toIntOrNull()?.let { it > 0 } == true &&
            protein.toIntOrNull()?.let { it >= 0 } == true &&
            carbs.toIntOrNull()?.let { it >= 0 } == true &&
            fat.toIntOrNull()?.let { it >= 0 } == true

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Edit Daily Goals") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = calories,
                    onValueChange = { calories = it.filter { c -> c.isDigit() } },
                    label = { Text("Calories") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = protein,
                    onValueChange = { protein = it.filter { c -> c.isDigit() } },
                    label = { Text("Protein (g)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = carbs,
                    onValueChange = { carbs = it.filter { c -> c.isDigit() } },
                    label = { Text("Carbs (g)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = fat,
                    onValueChange = { fat = it.filter { c -> c.isDigit() } },
                    label = { Text("Fat (g)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onSave(
                        calories.toInt(),
                        protein.toInt(),
                        carbs.toInt(),
                        fat.toInt()
                    )
                },
                enabled = isValid
            ) {
                Text("Save")
            }
        },
        dismissButton = { Button(onClick = onDismiss) { Text("Cancel") } }
    )
}

@Composable
fun WeightGoalDialog(
    currentWeight: Int,
    onDismiss: () -> Unit,
    onSave: (Int) -> Unit
) {
    var weight by remember { mutableStateOf(if (currentWeight > 0) currentWeight.toString() else "") }
    val isValid = weight.toIntOrNull()?.let { it > 0 } == true

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Edit Weight Goal") },
        text = {
            OutlinedTextField(
                value = weight,
                onValueChange = { weight = it.filter { c -> c.isDigit() } },
                label = { Text("Weight (kg)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
        },
        confirmButton = {
            Button(
                onClick = { onSave(weight.toInt()) },
                enabled = isValid
            ) {
                Text("Save")
            }
        },
        dismissButton = { Button(onClick = onDismiss) { Text("Cancel") } }
    )
}