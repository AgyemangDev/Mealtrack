package com.example.diettracker.models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class User(
    val uid: String = "",
    val fullName: String = "",
    val email: String = "",
    val ageRange: String = "",
    val ageDescription: String = ""
)

class UserViewModel : ViewModel() {

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user

    init {
        // Fetch user once on ViewModel creation
        fetchCurrentUser()
    }

    private fun fetchCurrentUser() {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            db.collection("users").document(currentUser.uid).get()
                .addOnSuccessListener { doc ->
                    if (doc.exists()) {
                        _user.value = User(
                            uid = doc.getString("uid") ?: currentUser.uid,
                            fullName = doc.getString("fullName") ?: "",
                            email = doc.getString("email") ?: "",
                            ageRange = doc.getString("ageRange") ?: "",
                            ageDescription = doc.getString("ageDescription") ?: ""
                        )
                    }
                }
        }
    }

    /**
     * Updates the user's age range in Firestore and locally
     */
    fun updateAgeRange(newRange: String, newDescription: String = "") {
        val currentUser = auth.currentUser ?: return
        val userRef = db.collection("users").document(currentUser.uid)

        // Update Firestore
        userRef.update(
            mapOf(
                "ageRange" to newRange,
                "ageDescription" to newDescription
            )
        ).addOnSuccessListener {
            // Update local state
            _user.value = _user.value?.copy(
                ageRange = newRange,
                ageDescription = newDescription
            )
        }.addOnFailureListener { e ->
            // Optional: handle error (e.g., log it)
            e.printStackTrace()
        }
    }
}
