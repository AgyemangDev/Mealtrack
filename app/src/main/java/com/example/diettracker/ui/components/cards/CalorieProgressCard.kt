import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
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

    // Color zones
    val barColor = when {
        calorieProgress < 0.4f -> Color(0xFFE57373) // Red
        calorieProgress < 0.8f -> Color(0xFFFFD600) // Yellow
        else -> Color(0xFF388E3C) // Green
    }

    // Status description
    val statusText = when {
        calorieProgress < 0.4f -> "Below recommended intake"
        calorieProgress < 0.8f -> "Getting close to target"
        else -> "Calorie goal met!"
    }

    Card(
        modifier = modifier
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
                    color = barColor,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "$totalCalories kcal consumed",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = barColor
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            LinearProgressIndicator(
                progress = { calorieProgress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(12.dp)
                    .clip(RoundedCornerShape(6.dp)),
                color = barColor,
                trackColor = Color(0xFFE0E0E0),
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = statusText,
                fontSize = 15.sp,
                color = barColor
            )
        }
    }
}
