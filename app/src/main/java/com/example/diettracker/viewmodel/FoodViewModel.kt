package com.example.diettracker.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.diettracker.models.Food
import com.google.firebase.auth.FirebaseAuth
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

    init {
        auth.currentUser?.uid?.let {
            viewModelScope.launch {
                db.collection("users").document(it).collection("foods")
                    .addSnapshotListener { snapshot, e ->
                        if (e != null) {
                            // Handle error
                            return@addSnapshotListener
                        }

                        snapshot?.let {
                            _foodItems.value = it.toObjects(Food::class.java)
                        }
                    }
            }
        }
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
}
