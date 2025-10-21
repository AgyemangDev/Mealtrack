package com.example.diettracker.models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.diettracker.models.FoodItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class Meal(val type: String, val foods: List<FoodItem>)
data class Day(val date: String, val meals: List<Meal>)

class DietViewModel : ViewModel() {

    private val firestore = FirebaseFirestore.getInstance()
    private val userId = FirebaseAuth.getInstance().currentUser?.uid

    private val _days = MutableStateFlow<List<Day>>(emptyList())
    val days: StateFlow<List<Day>> = _days

    init {
        println("🔹 DietViewModel initialized for userId: $userId")
        if (userId != null) {
            listenToUserDiet(userId)
        } else {
            println("⚠️ No logged-in user found")
        }
    }

    private fun listenToUserDiet(userId: String) {
        println("🔹 Listening to Firestore for userDiet: $userId")
        firestore.collection("usersDiet")
            .document(userId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    println("❌ Firestore listen error: ${error.message}")
                    return@addSnapshotListener
                }

                if (snapshot != null && snapshot.exists()) {
                    println("✅ Snapshot received for userDiet")

                    val data = snapshot.data
                    println("🔹 Raw data: $data")

                    val daysList = (data?.get("days") as? List<Map<String, Any>>)?.map { dayMap ->
                        val date = dayMap["date"] as? String ?: ""
                        val mealsList = (dayMap["meals"] as? List<Map<String, Any>>)?.map { mealMap ->
                            val type = mealMap["type"] as? String ?: ""
                            val foodsList = (mealMap["foods"] as? List<Map<String, Any>>)?.map { foodMap ->
                                val foodItem = FoodItem(
                                    name = foodMap["name"] as? String ?: "",
                                    calories = (foodMap["calories"] as? Number)?.toInt() ?: 0,
                                    protein = (foodMap["protein"] as? Number)?.toInt() ?: 0,
                                    carbs = (foodMap["carbs"] as? Number)?.toInt() ?: 0,
                                    fats = (foodMap["fats"] as? Number)?.toInt() ?: 0,
                                    imageUrl = foodMap["imageUrl"] as? String
                                )
                                println("   🍽 FoodItem parsed: $foodItem")
                                foodItem
                            } ?: emptyList()
                            val meal = Meal(type = type, foods = foodsList)
                            println(" 🥗 Meal parsed: $meal")
                            meal
                        } ?: emptyList()
                        val day = Day(date = date, meals = mealsList)
                        println(" 📅 Day parsed: $day")
                        day
                    } ?: emptyList()

                    println("🔹 Total days parsed: ${daysList.size}")
                    viewModelScope.launch {
                        _days.value = daysList
                    }
                } else {
                    println("⚠️ Snapshot is null or does not exist")
                    viewModelScope.launch { _days.value = emptyList() }
                }
            }
    }
}
