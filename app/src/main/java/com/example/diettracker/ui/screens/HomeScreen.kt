import android.util.Log
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.diettracker.data.*
import androidx.activity.compose.BackHandler
import com.example.diettracker.models.UserViewModel
import com.example.diettracker.ui.components.cards.*
import com.example.diettracker.ui.components.dialogs.MealDialog
import com.example.diettracker.ui.components.buttons.AddMealFab
import com.example.diettracker.ui.components.headers.AppHeader
import java.text.SimpleDateFormat
import java.util.*


@Composable
fun HomeScreen(
    meals: MutableList<MealInfo>,
    onNavigateToAllNutrients: () -> Unit = {},
    userViewModel: UserViewModel = viewModel()
) {
    BackHandler(enabled = true) {
        // Do nothing, disables system back & gesture
    }
    // Observe the user once
    val user by userViewModel.user.collectAsState()

    // Extract first name
    val userFirstName = user?.fullName?.split(" ")?.firstOrNull()?.capitalize() ?: "User"

    // ✅ Match user's ageRange with ageRangeData
    val matchedAgeData = ageRangeData.find { it.range == user?.ageRange } ?: ageRangeData[1] // default 19-30

    // Console log to verify
    Log.d("HomeScreen", "User ageRange: ${user?.ageRange}")
    Log.d("HomeScreen", "Matched Age Data: ${matchedAgeData.label}, Calories: ${matchedAgeData.calories}")

    val calorieGoal = matchedAgeData.calories

    var showMealDialog by remember { mutableStateOf(false) }
    var editingMealIndex by remember { mutableStateOf<Int?>(null) }

    val proteinGoal = matchedAgeData.nutrients.protein.value.removeSuffix("g").toInt()
    val carbsGoal = matchedAgeData.nutrients.carbohydrates.value.removeSuffix("g").toInt()
    val fatsGoal = matchedAgeData.nutrients.fats.value.removeSuffix("g").toInt()

    val totalCalories = meals.sumOf { it.calories }
    val totalProtein = meals.sumOf { it.protein }
    val totalCarbs = meals.sumOf { it.carbs }
    val totalFat = meals.sumOf { it.fat }


    val currentDateTime = remember {
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("EEEE, MMM dd", Locale.getDefault())
        val timeFormat = SimpleDateFormat("h:mm a", Locale.getDefault())
        "${dateFormat.format(calendar.time)} • ${timeFormat.format(calendar.time)}"
    }

    val greeting = remember {
        val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        when (hour) {
            in 5..10 -> "Ready for breakfast?"
            in 11..14 -> "Lunch time! Enjoy your meal"
            in 15..17 -> "Grab some healthy snack"
            in 18..21 -> "Keep Dinner light"
            else -> "Late night cravings?"
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            AppHeader(title = "NutriTrack")

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                GreetingCard(
                    greeting = greeting,
                    userName = userFirstName,
                    currentDateTime = currentDateTime
                )

                // ✅ Use calories from matchedAgeData
                CalorieProgressCard(
                    totalCalories = totalCalories,
                    calorieGoal = calorieGoal
                )

                NutrientsPreviewSection(
                    totalProtein = totalProtein,
                    totalCarbs = totalCarbs,
                    totalFats = totalFat,
                    proteinGoal = proteinGoal,
                    carbsGoal = carbsGoal,
                    fatsGoal = fatsGoal,
                    onNavigateToAllNutrients = onNavigateToAllNutrients
                )
                TodaysFoodsSection(
                    meals = meals,
                    onEditMeal = { index ->
                        editingMealIndex = index
                        showMealDialog = true
                    },
                    onDeleteMeal = { index ->
                        meals.removeAt(index)
                    }
                )

                Spacer(modifier = Modifier.height(80.dp))
            }
        }

        AddMealFab(
            onClick = {
                editingMealIndex = null
                showMealDialog = true
            }
        )
    }

    if (showMealDialog) {
        MealDialog(
            meal = editingMealIndex?.let { meals[it] },
            onDismiss = {
                showMealDialog = false
                editingMealIndex = null
            },
            onSave = { mealInfo ->
                if (editingMealIndex != null) {
                    meals[editingMealIndex!!] = mealInfo
                } else {
                    meals.add(mealInfo)
                }
                showMealDialog = false
                editingMealIndex = null
            }
        )
    }
}
