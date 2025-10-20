package com.example.diettracker.models

import java.util.Date

data class Food(
    val name: String,
    val calories: Double,
    val protein: Double,
    val carbs: Double,
    val fat: Double,
    val amount: Double,
    val unit: String,
    val date: Date = Date() // Default to current date
)
