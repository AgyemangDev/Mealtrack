package com.example.diettracker.ui.components.inputs

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun FoodSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearchClick: () -> Unit,
    searchIcon: ImageVector
) {
    val inputHeight = 56.dp // Rounded height for both input and button

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = query,
            onValueChange = onQueryChange,
            placeholder = { Text("Search for a food...") },
            leadingIcon = { Icon(searchIcon, contentDescription = null) },
            modifier = Modifier
                .weight(1f)
                .height(inputHeight),
            shape = RoundedCornerShape(16.dp),
            singleLine = true
        )

        Spacer(modifier = Modifier.width(8.dp))

        Button(
            onClick = onSearchClick,
            modifier = Modifier
                .height(inputHeight),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF668405)) // green
        ) {
            Icon(
                imageVector = searchIcon,
                contentDescription = null,
                tint = Color.White // white icon for contrast
            )
        }
    }
}
