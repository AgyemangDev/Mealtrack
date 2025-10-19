package com.example.diettracker.models

data class FoodItem(
    val name: String,
    val imageUrl: Int? = null, // optional
    val unit: String? = null,  // optional
    val calories: Int,
    val protein: Int,
    val carbs: Int,
    val fats: Int
)
