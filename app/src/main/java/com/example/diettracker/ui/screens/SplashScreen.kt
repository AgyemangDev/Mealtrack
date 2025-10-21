package com.example.diettracker.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.diettracker.R
import com.example.diettracker.ui.navigation.Screen
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun SplashScreen(navController: NavController) {
    val auth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()

    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            // Fetch Firestore user data
            db.collection("users").document(currentUser.uid).get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        navController.navigate(Screen.Main.route) {
                            popUpTo(Screen.Welcome.route) { inclusive = true }
                        }
                    } else {
                        auth.signOut()
                        navController.navigate(Screen.Login.route) {
                            popUpTo(Screen.Welcome.route) { inclusive = true }
                        }
                    }
                    isLoading = false
                }
                .addOnFailureListener {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Welcome.route) { inclusive = true }
                    }
                    isLoading = false
                }
        } else {
            navController.navigate(Screen.Login.route) {
                popUpTo(Screen.Welcome.route) { inclusive = true }
            }
            isLoading = false
        }
    }

    if (isLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = rememberAsyncImagePainter(R.drawable.welcomeimage1), // your splash icon
                contentDescription = "Splash Logo",
                modifier = Modifier
                    .size(150.dp) // adjust size as needed
            )
        }
    }
}
