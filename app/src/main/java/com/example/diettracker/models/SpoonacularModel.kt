package com.example.diettracker.models

data class RecipeResponse(
    val results: List<RecipeResult>
)

data class RecipeResult(
    val id: Int,
    val title: String,
    val image: String,
    val nutrition: Nutrition? = null
)

data class Nutrition(
    val nutrients: List<Nutrient>

)

data class Nutrient(
    val name: String,
    val amount: Float,
    val unit: String
)
