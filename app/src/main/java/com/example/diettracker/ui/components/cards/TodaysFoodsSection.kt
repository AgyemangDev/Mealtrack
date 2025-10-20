package com.example.diettracker.ui.components.cards



import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class MealInfo(
    val name: String,
    val servings: Int,
    val calories: Int,
    val protein: Int,
    val carbs: Int,
    val fat: Int,
    val iron: Double = 0.0,
    val calcium: Double = 0.0,
    val vitamins: Double = 0.0
)

@Composable
fun TodaysFoodsSection(
    meals: List<MealInfo>,
    onEditMeal: (Int) -> Unit,
    onDeleteMeal: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = "Today's Foods",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(16.dp)
        )

        if (meals.isEmpty()) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(40.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No meals logged today",
                        fontSize = 16.sp,
                        color = Color.Gray
                    )
                }
            }
        } else {
            Column(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                meals.forEachIndexed { index, meal ->
                    MealCard(
                        meal = meal,
                        onEdit = { onEditMeal(index) },
                        onDelete = { onDeleteMeal(index) }
                    )
                }
            }
        }
    }
}