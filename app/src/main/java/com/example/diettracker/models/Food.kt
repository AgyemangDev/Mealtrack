package com.example.diettracker.models

import java.util.Date
import java.util.UUID

/**
 * Represents a food item with a locally unique ID.
 */
data class Food(
    val id: String = UUID.randomUUID().toString(), // A unique ID for local operations
    val name: String,
    val calories: Double,
    val protein: Double,
    val carbs: Double,
    val fat: Double,
    val amount: Double,
    val unit: String,
    val date: Date = Date() // Default to current date
)
