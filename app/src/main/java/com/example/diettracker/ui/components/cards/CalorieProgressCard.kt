package com.example.diettracker.ui.components.cards



import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CalorieProgressCard(
    totalCalories: Int,
    calorieGoal: Int,
    modifier: Modifier = Modifier
) {
    val calorieProgress = (totalCalories.toFloat() / calorieGoal.toFloat()).coerceIn(0f, 1f)
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Today's Calories",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "$totalCalories/$calorieGoal kcal",
                    fontSize = 18.sp,
                    color = Color(0xFF668405),
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "$totalCalories",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF668405)
            )
            Text(
                text = "kcal consumed",
                fontSize = 16.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(16.dp))

            LinearProgressIndicator(
                progress = { calorieProgress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(12.dp)
                    .clip(RoundedCornerShape(6.dp)),
                color = Color(0xFF668405),
                trackColor = Color(0xFFE0E0E0),
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Goal: $calorieGoal kcal",
                fontSize = 16.sp,
                color = Color.Gray
            )
        }
    }


}