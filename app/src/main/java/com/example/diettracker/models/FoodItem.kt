package com.example.diettracker.models

data class FoodItem(
    val name: String,
    val imageUrl: String? = null,
    val unit: String? = null,  // optional
    val calories: Int,
    val protein: Int,
    val carbs: Int,
    val fats: Int
)
