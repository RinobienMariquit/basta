package com.oneguystudios.basta.model

enum class MealCategory {
    BREAKFAST,
    LUNCH,
    SNACK,
    DINNER
}

enum class ItemType {
    FOOD,
    DRINK
}

data class MealItem(
    val id: String = java.util.UUID.randomUUID().toString(),
    val name: String,
    val calories: Int,
    val protein: Float = 0f, // in grams
    val carbs: Float = 0f,   // in grams
    val fat: Float = 0f,     // in grams
    val mealCategory: MealCategory = MealCategory.BREAKFAST,
    val itemType: ItemType = ItemType.FOOD
)
