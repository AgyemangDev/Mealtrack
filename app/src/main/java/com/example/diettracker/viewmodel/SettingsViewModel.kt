package com.example.diettracker.viewmodel

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SettingsViewModel : ViewModel() {

    private val auth = FirebaseAuth.getInstance()

    // Initialize with current Firebase user data, or placeholders if not available
    private val _userName = MutableStateFlow(auth.currentUser?.displayName ?: "User")
    val userName: StateFlow<String> = _userName

    private val _userEmail = MutableStateFlow(auth.currentUser?.email ?: "email@example.com")
    val userEmail: StateFlow<String> = _userEmail

    // In a real app, these would be loaded from Firestore
    private val _calorieGoal = MutableStateFlow(2000)
    val calorieGoal: StateFlow<Int> = _calorieGoal

    private val _proteinGoal = MutableStateFlow(150)
    val proteinGoal: StateFlow<Int> = _proteinGoal

    private val _carbsGoal = MutableStateFlow(250)
    val carbsGoal: StateFlow<Int> = _carbsGoal

    private val _fatGoal = MutableStateFlow(70)
    val fatGoal: StateFlow<Int> = _fatGoal

    private val _weightGoal = MutableStateFlow(75)
    val weightGoal: StateFlow<Int> = _weightGoal

    private val _darkModeEnabled = MutableStateFlow(false)
    val darkModeEnabled: StateFlow<Boolean> = _darkModeEnabled

    private val _notificationsEnabled = MutableStateFlow(true)
    val notificationsEnabled: StateFlow<Boolean> = _notificationsEnabled

    private val _unitSystem = MutableStateFlow("Metric")
    val unitSystem: StateFlow<String> = _unitSystem

    fun updateProfile(name: String, email: String) {
        // Here you would update Firebase Auth and/or Firestore
        _userName.value = name
    }

    fun updateNutritionGoals(calories: Int, protein: Int, carbs: Int, fat: Int) {
        // Here you would save these to Firestore
        _calorieGoal.value = calories
        _proteinGoal.value = protein
        _carbsGoal.value = carbs
        _fatGoal.value = fat
    }

    fun setWeightGoal(weight: Int) {
        _weightGoal.value = weight
    }

    fun setDarkMode(enabled: Boolean) {
        _darkModeEnabled.value = enabled
    }

    fun setNotifications(enabled: Boolean) {
        _notificationsEnabled.value = enabled
    }

    fun setUnitSystem(unit: String) {
        _unitSystem.value = unit
    }

    fun exportData() {
        // Placeholder for data export logic
    }

    fun clearHistory() {
        // Placeholder for clearing history (e.g., deleting all documents in a Firestore collection)
    }

    fun logout() {
        auth.signOut()
    }
}
