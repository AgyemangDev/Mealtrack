package com.example.diettracker.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.diettracker.models.Food
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class FoodViewModel : ViewModel() {

    private val db = Firebase.firestore
    private val auth = FirebaseAuth.getInstance()

    private val _foodItems = MutableStateFlow<List<Food>>(emptyList())
    val foodItems: StateFlow<List<Food>> = _foodItems

    private var firestoreListener: ListenerRegistration? = null

    private val authStateListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
        val user = firebaseAuth.currentUser
        if (user != null) {
            attachFirestoreListener(user.uid)
        } else {
            detachFirestoreListener()
            _foodItems.value = emptyList()
        }
    }

    init {
        // Listen for auth state changes
        auth.addAuthStateListener(authStateListener)

        // Immediately check the current user, as the listener only fires on state changes.
        auth.currentUser?.uid?.let {
            attachFirestoreListener(it)
        }
    }

    private fun attachFirestoreListener(userId: String) {
        // Detach any existing listener to avoid duplicates
        detachFirestoreListener()
        viewModelScope.launch {
            firestoreListener = db.collection("users").document(userId).collection("foods")
                .addSnapshotListener { snapshot, e ->
                    if (e != null) {
                        _foodItems.value = emptyList()
                        return@addSnapshotListener
                    }
                    snapshot?.let {
                        _foodItems.value = it.toObjects(Food::class.java)
                    }
                }
        }
    }

    private fun detachFirestoreListener() {
        firestoreListener?.remove()
        firestoreListener = null
    }

    fun addFood(food: Food) {
        auth.currentUser?.uid?.let {
            db.collection("users").document(it).collection("foods")
                .add(food)
        }
    }

    fun deleteFood(food: Food) {
        auth.currentUser?.uid?.let {
            db.collection("users").document(it).collection("foods")
                .document(food.id)
                .delete()
        }
    }

    fun updateFood(food: Food) {
        auth.currentUser?.uid?.let {
            db.collection("users").document(it).collection("foods")
                .document(food.id)
                .set(food)
        }
    }

    override fun onCleared() {
        super.onCleared()
        auth.removeAuthStateListener(authStateListener)
        detachFirestoreListener()
    }
}
