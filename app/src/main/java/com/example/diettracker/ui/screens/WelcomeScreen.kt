package com.example.diettracker.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.diettracker.R
import com.example.diettracker.ui.components.modal.WelcomeScreenModal

@Composable
fun WelcomeScreen(navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF6FAF5)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.welcomeimage1),
                contentDescription = "App Logo",
                modifier = Modifier
                    .size(300.dp)
                    .padding(bottom = 16.dp)
            )

            Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                Text(
                    text = "Eat Smarter, Live Better",
                    fontSize = 35.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    lineHeight = 40.sp,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "MealTrack helps you understand your nutrition, making healthy eating simple and enjoyable every day.",
                    fontSize = 16.sp,
                    color = Color(0xFF45464A),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
            WelcomeScreenModal(navController = navController)
        }
    }
}
