package com.example.diettracker.viewmodel

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class SettingsViewModel(application: Application) : AndroidViewModel(application) {

    private val dataStore = application.dataStore
    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    // Preference keys
    private object PreferencesKeys {
        val CALORIE_GOAL = intPreferencesKey("calorie_goal")
        val PROTEIN_GOAL = intPreferencesKey("protein_goal")
        val CARBS_GOAL = intPreferencesKey("carbs_goal")
        val FAT_GOAL = intPreferencesKey("fat_goal")
        val WEIGHT_GOAL = intPreferencesKey("weight_goal")
        val DARK_MODE = booleanPreferencesKey("dark_mode")
        val NOTIFICATIONS = booleanPreferencesKey("notifications")
        val UNIT_SYSTEM = stringPreferencesKey("unit_system")
    }

    // StateFlows for Firebase data
    private val _userName = MutableStateFlow("Loading...")
    val userName: StateFlow<String> = _userName.asStateFlow()

    private val _userEmail = MutableStateFlow("Loading...")
    val userEmail: StateFlow<String> = _userEmail.asStateFlow()

    init {
        loadUserDataFromFirebase()
    }

    private fun loadUserDataFromFirebase() {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            viewModelScope.launch {
                try {
                    // Get user email from Firebase Auth
                    _userEmail.value = currentUser.email ?: "No email"

                    // Get user name from Firestore
                    val userDoc = firestore.collection("users")
                        .document(currentUser.uid)
                        .get()
                        .await()

                    _userName.value = userDoc.getString("name") ?:
                            userDoc.getString("displayName") ?:
                            currentUser.displayName ?:
                            "User"
                } catch (e: Exception) {
                    _userName.value = currentUser.displayName ?: "User"
                    _userEmail.value = currentUser.email ?: "No email"
                }
            }
        } else {
            _userName.value = "Guest User"
            _userEmail.value = "Not logged in"
        }
    }

    // StateFlows for local preferences
    val calorieGoal: StateFlow<Int> = dataStore.data
        .map { it[PreferencesKeys.CALORIE_GOAL] ?: 2000 }
        .stateIn(viewModelScope, SharingStarted.Eagerly, 2000)

    val proteinGoal: StateFlow<Int> = dataStore.data
        .map { it[PreferencesKeys.PROTEIN_GOAL] ?: 150 }
        .stateIn(viewModelScope, SharingStarted.Eagerly, 150)

    val carbsGoal: StateFlow<Int> = dataStore.data
        .map { it[PreferencesKeys.CARBS_GOAL] ?: 250 }
        .stateIn(viewModelScope, SharingStarted.Eagerly, 250)

    val fatGoal: StateFlow<Int> = dataStore.data
        .map { it[PreferencesKeys.FAT_GOAL] ?: 70 }
        .stateIn(viewModelScope, SharingStarted.Eagerly, 70)

    val weightGoal: StateFlow<Int> = dataStore.data
        .map { it[PreferencesKeys.WEIGHT_GOAL] ?: 0 }
        .stateIn(viewModelScope, SharingStarted.Eagerly, 0)

    val darkModeEnabled: StateFlow<Boolean> = dataStore.data
        .map { it[PreferencesKeys.DARK_MODE] ?: false }
        .stateIn(viewModelScope, SharingStarted.Eagerly, false)

    val notificationsEnabled: StateFlow<Boolean> = dataStore.data
        .map { it[PreferencesKeys.NOTIFICATIONS] ?: true }
        .stateIn(viewModelScope, SharingStarted.Eagerly, true)

    val unitSystem: StateFlow<String> = dataStore.data
        .map { it[PreferencesKeys.UNIT_SYSTEM] ?: "Metric (kg, cm)" }
        .stateIn(viewModelScope, SharingStarted.Eagerly, "Metric (kg, cm)")

    // Update functions
    fun updateProfile(name: String, email: String) {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            viewModelScope.launch {
                try {
                    // Update name in Firestore
                    firestore.collection("users")
                        .document(currentUser.uid)
                        .set(mapOf(
                            "name" to name,
                            "email" to email,
                            "updatedAt" to com.google.firebase.Timestamp.now()
                        ), com.google.firebase.firestore.SetOptions.merge())
                        .await()

                    // Update email in Firebase Auth if different
                    if (email != currentUser.email) {
                        currentUser.updateEmail(email).await()
                    }

                    // Update local state
                    _userName.value = name
                    _userEmail.value = email
                } catch (e: Exception) {
                    // Handle error (you might want to show a message to user)
                    e.printStackTrace()
                }
            }
        }
    }

    fun updateNutritionGoals(calories: Int, protein: Int, carbs: Int, fat: Int) {
        viewModelScope.launch {
            dataStore.edit { preferences ->
                preferences[PreferencesKeys.CALORIE_GOAL] = calories
                preferences[PreferencesKeys.PROTEIN_GOAL] = protein
                preferences[PreferencesKeys.CARBS_GOAL] = carbs
                preferences[PreferencesKeys.FAT_GOAL] = fat
            }

            // Also save to Firebase
            val currentUser = auth.currentUser
            if (currentUser != null) {
                try {
                    firestore.collection("users")
                        .document(currentUser.uid)
                        .set(mapOf(
                            "goals" to mapOf(
                                "calories" to calories,
                                "protein" to protein,
                                "carbs" to carbs,
                                "fat" to fat
                            )
                        ), com.google.firebase.firestore.SetOptions.merge())
                        .await()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    fun setWeightGoal(weight: Int) {
        viewModelScope.launch {
            dataStore.edit { preferences ->
                preferences[PreferencesKeys.WEIGHT_GOAL] = weight
            }

            // Also save to Firebase
            val currentUser = auth.currentUser
            if (currentUser != null) {
                try {
                    firestore.collection("users")
                        .document(currentUser.uid)
                        .set(mapOf(
                            "weightGoal" to weight
                        ), com.google.firebase.firestore.SetOptions.merge())
                        .await()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    fun setDarkMode(enabled: Boolean) {
        viewModelScope.launch {
            dataStore.edit { preferences ->
                preferences[PreferencesKeys.DARK_MODE] = enabled
            }
        }
    }

    fun setNotifications(enabled: Boolean) {
        viewModelScope.launch {
            dataStore.edit { preferences ->
                preferences[PreferencesKeys.NOTIFICATIONS] = enabled
            }
        }
    }

    fun setUnitSystem(system: String) {
        viewModelScope.launch {
            dataStore.edit { preferences ->
                preferences[PreferencesKeys.UNIT_SYSTEM] = system
            }
        }
    }

    fun clearAllData() {
        viewModelScope.launch {
            // Clear local DataStore
            dataStore.edit { it.clear() }

            // Optionally clear Firebase data
            val currentUser = auth.currentUser
            if (currentUser != null) {
                try {
                    // Delete user's meals collection
                    firestore.collection("users")
                        .document(currentUser.uid)
                        .collection("meals")
                        .get()
                        .await()
                        .documents
                        .forEach { it.reference.delete() }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    fun exportData() {
        viewModelScope.launch {
            // Implementation for exporting data to CSV or JSON
            // This would typically create a file and share it
        }
    }

    fun logout() {
        viewModelScope.launch {
            auth.signOut()
            _userName.value = "Guest User"
            _userEmail.value = "Not logged in"
        }
    }
}