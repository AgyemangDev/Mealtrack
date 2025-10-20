package com.example.diettracker.models

import com.google.firebase.firestore.DocumentId
import java.util.Date

/**
 * Represents a food item.
 * Note: An empty constructor is required for Firestore deserialization.
 */
data class Food(
    @DocumentId val id: String = "", // Firestore will automatically populate this with the document ID
    val name: String = "",
    val calories: Double = 0.0,
    val protein: Double = 0.0,
    val carbs: Double = 0.0,
    val fat: Double = 0.0,
    val amount: Double = 0.0,
    val unit: String = "",
    val date: Date = Date()
)
