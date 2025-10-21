package com.example.diettracker.data

data class AgeRangeInfo(
    val label: String,
    val range: String,
    val description: String,
    val calories: Int,
    val nutrients: NutrientBreakdown
)

data class NutrientBreakdown(
    val carbohydrates: NutrientDetail,
    val protein: NutrientDetail,
    val fats: NutrientDetail,
    val vitaminsAndMinerals: NutrientDetail
)

data class NutrientDetail(
    val value: String, // Changed to String for weight (e.g., "56g", "Daily Req.")
    val examples: List<String>,
    val icon: String // Added emoji icon
)

val ageRangeData = listOf(
    AgeRangeInfo(
        label = "12–18 Years",
        range = "12-18",
        description = "Teen",
        calories = 2400,
        nutrients = NutrientBreakdown(
            carbohydrates = NutrientDetail(
                value = "300g",
                examples = listOf("Brown Rice 🍚", "Whole-grain Bread 🍞", "Sweet Potatoes 🍠", "Oats 🌾", "Apples 🍎"),
                icon = "🌾"
            ),
            protein = NutrientDetail(
                value = "56g",
                examples = listOf("Grilled Chicken 🍗", "Boiled Eggs 🥚", "Beans 🫘", "Greek Yogurt 🥛", "Salmon (4 oz) 🐟"),
                icon = "⭕"
            ),
            fats = NutrientDetail(
                value = "70g",
                examples = listOf("Avocado 🥑", "Olive Oil 🫒", "Nuts 🥜", "Salmon 🐟"),
                icon = "🧴"
            ),
            vitaminsAndMinerals = NutrientDetail(
                value = "Daily Req.",
                examples = listOf("Spinach (Iron) 🥬", "Oranges (Vit C) 🍊", "Milk (Calcium) 🥛", "Broccoli (Vit K, Vit C) 🥦"),
                icon = "🍎"
            )
        )
    ),
    AgeRangeInfo(
        label = "19–30 Years",
        range = "19-30",
        description = "Young Adult",
        calories = 2600,
        nutrients = NutrientBreakdown(
            carbohydrates = NutrientDetail(
                value = "330g",
                examples = listOf("Pasta 🍝", "Brown Rice 🍚", "Sweet Potatoes 🍠", "Quinoa 🌾"),
                icon = "🌾"
            ),
            protein = NutrientDetail(
                value = "60g",
                examples = listOf("Fish 🐟", "Eggs 🥚", "Meat 🥩", "Tofu 🍱"),
                icon = "⭕"
            ),
            fats = NutrientDetail(
                value = "75g",
                examples = listOf("Olive Oil 🫒", "Nuts 🥜", "Seeds 🌱", "Avocado 🥑"),
                icon = "🧴"
            ),
            vitaminsAndMinerals = NutrientDetail(
                value = "Daily Req.",
                examples = listOf("Leafy Greens 🥬", "Citrus 🍊", "Berries 🫐", "Almonds 🥜"),
                icon = "🍎"
            )
        )
    ),
    AgeRangeInfo(
        label = "31–50 Years",
        range = "31-50",
        description = "Adult",
        calories = 2200,
        nutrients = NutrientBreakdown(
            carbohydrates = NutrientDetail(
                value = "315g",
                examples = listOf("Whole Grains 🌾", "Potatoes 🥔", "Corn 🌽", "Brown Rice 🍚"),
                icon = "🌾"
            ),
            protein = NutrientDetail(
                value = "58g",
                examples = listOf("Lean Meat 🥩", "Eggs 🥚", "Beans 🫘", "Fish 🐟"),
                icon = "⭕"
            ),
            fats = NutrientDetail(
                value = "72g",
                examples = listOf("Fish Oil 🐟", "Nuts 🥜", "Olives 🫒", "Seeds 🌱"),
                icon = "🧴"
            ),
            vitaminsAndMinerals = NutrientDetail(
                value = "Daily Req.",
                examples = listOf("Broccoli 🥦", "Bananas 🍌", "Apples 🍎", "Spinach 🥬"),
                icon = "🍎"
            )
        )
    ),
    AgeRangeInfo(
        label = "Above 51 Years",

        range = "51-70",
        description = "Middle Aged / Seniors",
        calories = 1800,
        nutrients = NutrientBreakdown(
            carbohydrates = NutrientDetail(
                value = "270g",
                examples = listOf("Oats 🌾", "Brown Rice 🍚", "Whole-grain Bread 🍞", "Sweet Potatoes 🍠"),
                icon = "🌾"
            ),
            protein = NutrientDetail(
                value = "54g",
                examples = listOf("Fish 🐟", "Legumes 🫘", "Eggs 🥚", "Chicken 🍗"),
                icon = "⭕"
            ),
            fats = NutrientDetail(
                value = "65g",
                examples = listOf("Avocado 🥑", "Nuts 🥜", "Seeds 🌱", "Olive Oil 🫒"),
                icon = "🧴"
            ),
            vitaminsAndMinerals = NutrientDetail(
                value = "Daily Req.",
                examples = listOf("Leafy Vegetables 🥬", "Fruits 🍎", "Milk 🥛", "Mushrooms 🍄"),
                icon = "🍎"
            )
        )
    )
)