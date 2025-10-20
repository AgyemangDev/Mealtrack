package com.example.diettracker.viewmodel

import androidx.lifecycle.ViewModel
import com.example.diettracker.models.Food
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class FoodViewModel : ViewModel() {
    private val _foodItems = MutableStateFlow<List<Food>>(emptyList())
    val foodItems: StateFlow<List<Food>> = _foodItems

    fun addFood(food: Food) {
        _foodItems.value = _foodItems.value + food
    }

    fun deleteFood(food: Food) {
        _foodItems.value = _foodItems.value - food
    }

    fun updateFood(oldFood: Food, newFood: Food) {
        _foodItems.value = _foodItems.value.map {
            if (it == oldFood) newFood else it
        }
    }
}
