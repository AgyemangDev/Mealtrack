package com.example.diettracker.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.diettracker.data.UserPreferencesManager
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class SettingsViewModel(application: Application) : AndroidViewModel(application) {
    private val userPreferences = UserPreferencesManager(application.applicationContext)
    private val auth = FirebaseAuth.getInstance()

    private val _userName = MutableStateFlow("User")
    val userName: StateFlow<String> = _userName

    private val _userEmail = MutableStateFlow("")
    val userEmail: StateFlow<String> = _userEmail

    private val _calorieGoal = MutableStateFlow(2000)
    val calorieGoal: StateFlow<Int> = _calorieGoal

    private val _proteinGoal = MutableStateFlow(150)
    val proteinGoal: StateFlow<Int> = _proteinGoal

    private val _carbsGoal = MutableStateFlow(250)
    val carbsGoal: StateFlow<Int> = _carbsGoal

    private val _fatGoal = MutableStateFlow(70)
    val fatGoal: StateFlow<Int> = _fatGoal

    private val _weightGoal = MutableStateFlow(0)
    val weightGoal: StateFlow<Int> = _weightGoal

    private val _darkModeEnabled = MutableStateFlow(false)
    val darkModeEnabled: StateFlow<Boolean> = _darkModeEnabled

    private val _notificationsEnabled = MutableStateFlow(true)
    val notificationsEnabled: StateFlow<Boolean> = _notificationsEnabled

    private val _unitSystem = MutableStateFlow("Metric")
    val unitSystem: StateFlow<String> = _unitSystem

    private val _isLoggingOut = MutableStateFlow(false)
    val isLoggingOut: StateFlow<Boolean> = _isLoggingOut

    init {
        loadUserData()
    }

    private fun loadUserData() {
        viewModelScope.launch {
            userPreferences.userName.collect { name ->
                _userName.value = name
            }
        }
        viewModelScope.launch {
            userPreferences.userEmail.collect { email ->
                _userEmail.value = email
            }
        }

        // Load current user from Firebase
        auth.currentUser?.let { user ->
            if (_userEmail.value.isEmpty()) {
                _userEmail.value = user.email ?: ""
            }
            if (_userName.value == "User") {
                _userName.value = user.displayName ?: "User"
            }
        }
    }

    fun updateProfile(name: String, email: String) {
        viewModelScope.launch {
            userPreferences.saveUserName(name)
            _userName.value = name
        }
    }

    fun updateNutritionGoals(calories: Int, protein: Int, carbs: Int, fat: Int) {
        viewModelScope.launch {
            _calorieGoal.value = calories
            _proteinGoal.value = protein
            _carbsGoal.value = carbs
            _fatGoal.value = fat

            // Optionally save to preferences
            // userPreferences.saveNutritionGoals(calories, protein, carbs, fat)
        }
    }

    fun setWeightGoal(weight: Int) {
        viewModelScope.launch {
            _weightGoal.value = weight
            // Optionally save to preferences
            // userPreferences.saveWeightGoal(weight)
        }
    }

    fun setDarkMode(enabled: Boolean) {
        viewModelScope.launch {
            _darkModeEnabled.value = enabled
            // Optionally save to preferences
            // userPreferences.saveDarkMode(enabled)
        }
    }

    fun setNotifications(enabled: Boolean) {
        viewModelScope.launch {
            _notificationsEnabled.value = enabled
            // Optionally save to preferences
            // userPreferences.saveNotifications(enabled)
        }
    }

    suspend fun logoutSuspend() {
        try {
            _isLoggingOut.value = true
            Log.d("SettingsViewModel", "Starting logout process")

            // Sign out from Firebase (await if using Tasks API)
            auth.signOut()
            Log.d("SettingsViewModel", "Firebase sign out complete")

            // Clear local data
            userPreferences.clearAllData()
            Log.d("SettingsViewModel", "Local data cleared")

            // Reset all values
            _userName.value = "User"
            _userEmail.value = ""
            _calorieGoal.value = 2000
            _proteinGoal.value = 150
            _carbsGoal.value = 250
            _fatGoal.value = 70
            _weightGoal.value = 0

            Log.d("SettingsViewModel", "Logout complete")
        } catch (e: Exception) {
            Log.e("SettingsViewModel", "Logout failed", e)
            throw e
        } finally {
            _isLoggingOut.value = false
        }
    }

    fun logout() {
        viewModelScope.launch {
            logoutSuspend()
        }
    }
}