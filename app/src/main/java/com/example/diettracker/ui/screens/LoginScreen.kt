package com.example.diettracker.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import androidx.compose.ui.platform.LocalContext
import androidx.compose.material3.Text
import com.example.diettracker.R
import com.example.diettracker.ui.components.headers.AppHeader
import com.example.diettracker.ui.components.inputs.AppTextField
import com.example.diettracker.ui.components.buttons.CustomButton
import com.example.diettracker.ui.navigation.Screen

@Composable
fun LoginScreen(navController: NavController) {
    var isLoading by remember { mutableStateOf(false) }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .background(Color(0xFFF6FAF5))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
        ) {

            AppHeader(
                title = "Login",
                onBackClick = { navController.popBackStack() }
            )


            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(R.drawable.login_banner)
                    .build(),
                contentDescription = "Login Banner",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .width(300.dp)
            )


            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                    .background(Color(0xFFF6FAF5))
                    .padding(horizontal = 20.dp, vertical = 24.dp)
            ) {
                AppTextField(
                    label = "Email",
                    placeholder = "mealtrack@example.com",
                    value = email,
                    onValueChange = { email = it }
                )

                Spacer(modifier = Modifier.height(16.dp))

                AppTextField(
                    label = "Password",
                    placeholder = "Enter your password",
                    value = password,
                    onValueChange = { password = it },
                    isPassword = true
                )

                Spacer(modifier = Modifier.height(24.dp))

                CustomButton(
                    text = if (isLoading) "Logging in..." else "Login",
                    onClick = {
                        if (!isLoading) {
                            isLoading = true
                            // TODO: Implement login logic
                            navController.navigate(Screen.Home.route)
                            isLoading = false
                        }
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Don't have an account? ",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                    Text(
                        text = "Sign up",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF668405),
                        modifier = Modifier.clickable {
                            navController.navigate(Screen.Register.route)
                        }
                    )
                }
            }
        }
    }
}
