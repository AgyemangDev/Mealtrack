package com.example.diettracker.models

data class FoodItem(
    val name: String = "",
    val imageUrl: String? = null,
    val unit: String? = null,
    val calories: Int = 0,
    val protein: Int = 0,
    val carbs: Int = 0,
    val fats: Int = 0
)