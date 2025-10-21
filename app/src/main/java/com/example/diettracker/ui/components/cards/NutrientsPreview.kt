package com.example.diettracker.ui.components.cards



import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.diettracker.R

data class NutrientInfo(
    val name: String,
    val current: Int,
    val goal: Int,
    val imageRes: Int
)

@Composable
fun NutrientsPreviewSection(
    totalProtein: Int,
    totalCarbs: Int,
    onNavigateToAllNutrients: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        // Header Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Nutrients Preview",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            TextButton(onClick = onNavigateToAllNutrients) {
                Text(text = "More", color = Color(0xFF668405), fontSize = 16.sp)
            }
        }

        // Nutrients List
        val updatedNutrients = listOf(
            NutrientInfo("Protein", totalProtein, 100, R.drawable.protein),
            NutrientInfo("Carbs", totalCarbs, 250, R.drawable.cabo)
        )

        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(updatedNutrients) { nutrient ->
                NutrientCard(nutrient = nutrient)
            }
        }
    }
}

@Composable
private fun NutrientCard(nutrient: NutrientInfo) {
    Card(
        modifier = Modifier
            .width(200.dp)
            .height(240.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Image(
                painter = painterResource(id = nutrient.imageRes),
                contentDescription = nutrient.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = nutrient.name,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "${nutrient.current}/${nutrient.goal}g",
                fontSize = 18.sp,
                color = Color.Gray,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}