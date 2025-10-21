package com.example.diettracker.ui.components.cards

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.diettracker.data.ageRangeData
import com.example.diettracker.models.UserViewModel
import kotlinx.coroutines.launch

@Composable
fun ProfileCard(
    userName: String,
    userEmail: String,
    calorieGoal: Int,
    proteinGoal: Int,
    carbsGoal: Int,
    fatGoal: Int,
    userAgeRange: String,
    userViewModel: UserViewModel,
    onEditClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scope = rememberCoroutineScope()

    // Dropdown state
    var expanded by remember { mutableStateOf(false) }
    var selectedAgeRange by remember { mutableStateOf(userAgeRange) }

    Surface(
        modifier = modifier.fillMaxWidth(),
        color = Color.White,
        shadowElevation = 4.dp,
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // --- Avatar ---
            ProfileAvatar(userName = userName)

            Spacer(modifier = Modifier.height(16.dp))

            // --- Name & Email ---
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

            // --- Age Range Dropdown ---
            Box {
                OutlinedButton(
                    onClick = { expanded = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Age Range: $selectedAgeRange")
                }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    ageRangeData.forEach { ageData ->
                        DropdownMenuItem(
                            text = { Text(ageData.label) },
                            onClick = {
                                selectedAgeRange = ageData.label
                                expanded = false

                                // Update user ageRange in Firestore
                                scope.launch {
                                    userViewModel.updateAgeRange(ageData.range)
                                }
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // --- Daily Nutrition Goals Card ---
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Daily Nutrition Goals", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Column { Text("Calories"); Text("$calorieGoal kcal") }
                        Column { Text("Protein"); Text("$proteinGoal g") }
                        Column { Text("Carbs"); Text("$carbsGoal g") }
                        Column { Text("Fats"); Text("$fatGoal g") }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // --- Edit Profile Button ---
            Button(
                onClick = onEditClick,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF668405)),
                modifier = Modifier.fillMaxWidth()
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
private fun ProfileAvatar(userName: String) {
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
}
