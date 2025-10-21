package com.example.diettracker.ui.components.dialogs

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.diettracker.ui.components.cards.MealInfo
import com.example.diettracker.ui.components.buttons.CustomButton
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun MealDialog(
    meal: MealInfo? = null,
    onDismiss: () -> Unit,
    onSave: (MealInfo) -> Unit
) {
    var mealName by remember { mutableStateOf(meal?.name ?: "") }
    var servings by remember { mutableStateOf(meal?.servings?.toString() ?: "1") }
    var calories by remember { mutableStateOf(meal?.calories?.toString() ?: "") }
    var protein by remember { mutableStateOf(meal?.protein?.toString() ?: "") }
    var carbs by remember { mutableStateOf(meal?.carbs?.toString() ?: "") }
    var fat by remember { mutableStateOf(meal?.fat?.toString() ?: "") }
    var iron by remember { mutableStateOf(meal?.iron?.toString() ?: "") }
    var calcium by remember { mutableStateOf(meal?.calcium?.toString() ?: "") }
    var vitamins by remember { mutableStateOf(meal?.vitamins?.toString() ?: "") }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            shape = MaterialTheme.shapes.large,
            color = Color.White
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = if (meal == null) "Add Meal" else "Edit Meal",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(20.dp))


                OutlinedTextField(
                    value = mealName,
                    onValueChange = { mealName = it },
                    label = { Text("Meal Name") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(12.dp))


                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = servings,
                        onValueChange = { servings = it },
                        label = { Text("Servings") },
                        modifier = Modifier.weight(1f),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = calories,
                        onValueChange = { calories = it },
                        label = { Text("Calories ") },
                        modifier = Modifier.weight(1f),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))


                Text(
                    text = "Macronutrients",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF668405)
                )

                Spacer(modifier = Modifier.height(8.dp))


                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = protein,
                        onValueChange = { protein = it },
                        label = { Text("Protein (g)") },
                        modifier = Modifier.weight(1f),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = carbs,
                        onValueChange = { carbs = it },
                        label = { Text("Carbs (g)") },
                        modifier = Modifier.weight(1f),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))


                OutlinedTextField(
                    value = fat,
                    onValueChange = { fat = it },
                    label = { Text("Fat (g)") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(16.dp))


                Text(
                    text = "Micronutrients",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF668405)
                )

                Spacer(modifier = Modifier.height(8.dp))


                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = iron,
                        onValueChange = { iron = it },
                        label = { Text("Iron (mg)") },
                        modifier = Modifier.weight(1f),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = calcium,
                        onValueChange = { calcium = it },
                        label = { Text("Calcium ") },
                        modifier = Modifier.weight(1f),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        singleLine = true
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))


                OutlinedTextField(
                    value = vitamins,
                    onValueChange = { vitamins = it },
                    label = { Text("Vitamins (mg)") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    CustomButton(
                        text = "Cancel",
                        onClick = onDismiss,
                        backgroundColor = Color.White,
                        contentColor = Color(0xFF668405),
                        modifier = Modifier.weight(1f)
                    )

                    CustomButton(
                        text = "Save",
                        onClick = {
                            if (mealName.isNotBlank() && calories.isNotBlank()) {
                                val currentTime = SimpleDateFormat("h:mm a", Locale.getDefault())
                                    .format(Calendar.getInstance().time)

                                onSave(
                                    MealInfo(
                                        name = mealName,
                                        servings = servings.toIntOrNull() ?: 1,
                                        calories = calories.toIntOrNull() ?: 0,
                                        protein = protein.toIntOrNull() ?: 0,
                                        carbs = carbs.toIntOrNull() ?: 0,
                                        fat = fat.toIntOrNull() ?: 0,
                                        iron = iron.toDoubleOrNull() ?: 0.0,
                                        calcium = calcium.toDoubleOrNull() ?: 0.0,
                                        vitamins = vitamins.toDoubleOrNull() ?: 0.0
                                    )
                                )
                            }
                        },
                        backgroundColor = Color(0xFF668405),
                        contentColor = Color.White,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}