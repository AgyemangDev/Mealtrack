package com.example.diettracker.data

import com.example.diettracker.models.FoodItem
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.*

object DietRepository {

    private val firestore = FirebaseFirestore.getInstance()

    private fun getFormattedDate(timestamp: Long): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return sdf.format(Date(timestamp))
    }

    private fun getMealTypeFromHour(timestamp: Long): String {
        val hour = Calendar.getInstance().apply { timeInMillis = timestamp }.get(Calendar.HOUR_OF_DAY)
        return when (hour) {
            in 5..10 -> "breakfast"
            in 11..16 -> "lunch"
            else -> "supper"
        }
    }

    suspend fun addFoodForUser(userId: String, food: FoodItem) {
        val timestamp = System.currentTimeMillis()
        val todayDate = getFormattedDate(timestamp)
        val mealType = getMealTypeFromHour(timestamp)

        val userDocRef = firestore.collection("usersDiet").document(userId)
        val snapshot = userDocRef.get().await()

        // Safely handle missing data
        val existingData = snapshot.data ?: mapOf("days" to listOf<Map<String, Any>>())
        val days = (existingData["days"] as? List<Map<String, Any>>)?.toMutableList() ?: mutableListOf()

        // Find or create today's entry
        val todayEntry = days.find { it["date"] == todayDate }?.toMutableMap()
            ?: mutableMapOf("date" to todayDate, "meals" to mutableListOf<Map<String, Any>>())

        val meals = (todayEntry["meals"] as? MutableList<Map<String, Any>>)?.toMutableList() ?: mutableListOf()

        // Find or create meal type
        val mealEntry = meals.find { it["type"] == mealType }?.toMutableMap()
            ?: mutableMapOf("type" to mealType, "foods" to mutableListOf<Map<String, Any>>())

        val foods = (mealEntry["foods"] as? MutableList<Map<String, Any>>)?.toMutableList() ?: mutableListOf()

        // ✅ Strictly typecast to Map<String, Any>
        val foodMap: Map<String, Any> = mapOf(
            "name" to (food.name ?: ""),
            "calories" to (food.calories ?: 0),
            "protein" to (food.protein ?: 0),
            "carbs" to (food.carbs ?: 0),
            "fats" to (food.fats ?: 0),
            "imageUrl" to (food.imageUrl ?: "")
        )

        foods.add(foodMap)
        mealEntry["foods"] = foods

        // Replace or add meal entry
        val mealIndex = meals.indexOfFirst { it["type"] == mealType }
        if (mealIndex >= 0) meals[mealIndex] = mealEntry else meals.add(mealEntry)

        todayEntry["meals"] = meals

        // Replace or add today's entry
        val dayIndex = days.indexOfFirst { it["date"] == todayDate }
        if (dayIndex >= 0) days[dayIndex] = todayEntry else days.add(todayEntry)

        // ✅ Explicitly cast final structure to Map<String, Any>
        val finalData: Map<String, Any> = mapOf("days" to days)

        userDocRef.set(finalData).await()
    }
}
