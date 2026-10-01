package com.oneguystudios.basta.model

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class DailyLog(
    val date: String = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date()),
    val items: List<MealItem> = emptyList()
) {
    val totalCalories: Int
        get() = items.sumOf { it.calories }

    val totalProtein: Float
        get() = items.sumOf { it.protein.toDouble() }.toFloat()

    val totalCarbs: Float
        get() = items.sumOf { it.carbs.toDouble() }.toFloat()

    val totalFat: Float
        get() = items.sumOf { it.fat.toDouble() }.toFloat()

    fun itemsForCategory(category: MealCategory): List<MealItem> {
        return items.filter { it.mealCategory == category }
    }
}
