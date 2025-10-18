package com.example.diettracker.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.diettracker.ui.components.headers.AppHeader
import com.example.diettracker.ui.components.inputs.AppTextField
import com.example.diettracker.ui.components.buttons.CustomButton
import com.example.diettracker.ui.navigation.Screen
import com.example.diettracker.ui.components.dialogs.AppAlertDialog
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import com.example.diettracker.ui.utils.ValidationUtils

@Composable
fun RegisterScreen(navController: NavController) {
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

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
                title = "Sign Up",
                onBackClick = { navController.popBackStack() }
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                    .background(Color(0xFFF6FAF5))
                    .padding(horizontal = 20.dp, vertical = 24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Full Name field with auto-format
                AppTextField(
                    label = "Full Name",
                    placeholder = "e.g. John Doe",
                    value = fullName,
                    onValueChange = { fullName = ValidationUtils.formatFullName(it) }
                )

                // Email field
                AppTextField(
                    label = "Email",
                    placeholder = "mealtrack@example.com",
                    value = email,
                    onValueChange = { email = it }
                )

                // Password field
                AppTextField(
                    label = "Password",
                    placeholder = "Enter your password",
                    value = password,
                    onValueChange = { password = it },
                    isPassword = true
                )

                // Confirm Password field
                AppTextField(
                    label = "Confirm Password",
                    placeholder = "Re-enter your password",
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    isPassword = true
                )

                Spacer(modifier = Modifier.height(24.dp))

                CustomButton(
                    text = if (isLoading) "Creating account..." else "Sign Up",
                    onClick = {
                        if (!isLoading) {
                            when {
                                fullName.isBlank() -> {
                                    errorMessage = "Full name is required"
                                    showDialog = true
                                }
                                !ValidationUtils.isValidFullName(fullName) -> {
                                    errorMessage = "Full name must contain at least two words"
                                    showDialog = true
                                }
                                email.isBlank() -> {
                                    errorMessage = "Email is required"
                                    showDialog = true
                                }
                                !ValidationUtils.isValidEmail(email) -> {
                                    errorMessage = "Please enter a valid email address"
                                    showDialog = true
                                }
                                password.isBlank() -> {
                                    errorMessage = "Password is required"
                                    showDialog = true
                                }
                                !ValidationUtils.isValidPassword(password) -> {
                                    errorMessage =
                                        "Password must be at least 8 characters long and include an uppercase letter, a lowercase letter, a number, and a special character"
                                    showDialog = true
                                }
                                password != confirmPassword -> {
                                    errorMessage = "The passwords you entered do not match. Please try again."
                                    showDialog = true
                                }
                                else -> {
                                    isLoading = true
                                    val encodedFullName =
                                        URLEncoder.encode(fullName, StandardCharsets.UTF_8.toString())
                                    val encodedEmail =
                                        URLEncoder.encode(email, StandardCharsets.UTF_8.toString())
                                    val encodedPassword =
                                        URLEncoder.encode(password, StandardCharsets.UTF_8.toString())

                                    navController.navigate(
                                        Screen.AgeRange.createRoute(
                                            encodedFullName,
                                            encodedEmail,
                                            encodedPassword
                                        )
                                    )
                                    isLoading = false
                                }
                            }
                        }
                    }
                )
            }

            Spacer(modifier = Modifier.height(5.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Already have an account?",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
                Text(
                    text = "  Log In",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF668405),
                    modifier = Modifier.clickable {
                        navController.navigate(Screen.Login.route)
                    }
                )
            }
        }

        AppAlertDialog(
            showDialog = showDialog,
            onDismiss = { showDialog = false },
            title = if (errorMessage.contains("match")) "Password Mismatch" else "Validation Error",
            message = errorMessage
        )
    }
}
