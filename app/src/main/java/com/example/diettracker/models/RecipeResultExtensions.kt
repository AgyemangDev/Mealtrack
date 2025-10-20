package com.example.diettracker.models

fun RecipeResult.toFoodItem(): FoodItem {
    val calories = nutrition?.nutrients?.find { it.name.equals("Calories", true) }?.amount?.toInt() ?: 0
    val protein = nutrition?.nutrients?.find { it.name.equals("Protein", true) }?.amount?.toInt() ?: 0
    val carbs = nutrition?.nutrients?.find { it.name.equals("Carbohydrates", true) }?.amount?.toInt() ?: 0
    val fats = nutrition?.nutrients?.find { it.name.equals("Fat", true) }?.amount?.toInt() ?: 0

    return FoodItem(
        name = title,
        imageUrl = image,
        calories = calories,
        protein = protein,
        carbs = carbs,
        fats = fats
    )
}
