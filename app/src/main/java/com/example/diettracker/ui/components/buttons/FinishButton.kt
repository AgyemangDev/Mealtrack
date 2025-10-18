package com.example.diettracker.ui.components.buttons

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.diettracker.data.AgeRangeInfo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun FinishButton(
    navController: NavController,
    fullName: String,
    email: String,
    password: String,
    ageRangeInfo: AgeRangeInfo,
    onSuccess: () -> Unit,
    modifier: Modifier = Modifier
) {
    val firestore = FirebaseFirestore.getInstance()
    val auth = FirebaseAuth.getInstance()

    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var showSuccessDialog by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        CustomButton(
            text = if (isLoading) "Creating Account..." else "Finish",
            onClick = {
                // Reset errors
                errorMessage = null
                isLoading = true

                // 1️⃣ Create Firebase Auth account
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnSuccessListener { authResult ->
                        // 2️⃣ Get UID of newly created user
                        val uid = authResult.user?.uid ?: ""

                        // 3️⃣ Prepare Firestore data
                        val userData = hashMapOf(
                            "fullName" to fullName,
                            "email" to email,
                            "ageRange" to ageRangeInfo.range,
                            "ageDescription" to ageRangeInfo.description,
                            "uid" to uid
                        )

                        // 4️⃣ Write to Firestore "users" collection with UID as document ID
                        firestore.collection("users")
                            .document(uid)
                            .set(userData)
                            .addOnSuccessListener {
                                isLoading = false
                                showSuccessDialog = true
                            }
                            .addOnFailureListener { e ->
                                isLoading = false
                                errorMessage = e.message ?: "Failed to save user data"
                                println("🔥 Firestore Error: ${e.message}")
                            }

                    }
                    .addOnFailureListener { e ->
                        isLoading = false
                        errorMessage = e.message ?: "Failed to create account"
                        println("🔥 Auth Error: ${e.message}")
                    }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        )
    }

    // ✅ Success Dialog
    if (showSuccessDialog) {
        AlertDialog(
            onDismissRequest = { showSuccessDialog = false },
            title = { Text("Account Created") },
            text = { Text("Your account has been successfully created!") },
            confirmButton = {
                Button(
                    onClick = {
                        showSuccessDialog = false
                        onSuccess()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text("Go to Home")
                }
            }
        )
    }

    // ❌ Error Dialog
    if (errorMessage != null) {
        AlertDialog(
            onDismissRequest = { errorMessage = null },
            title = { Text("Error") },
            text = { Text(errorMessage!!) },
            confirmButton = {
                Button(
                    onClick = { errorMessage = null },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("OK")
                }
            }
        )
    }
}
