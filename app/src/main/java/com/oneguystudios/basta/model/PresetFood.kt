package com.oneguystudios.basta.model

import android.content.Context

data class PresetFood(
    val name: String,
    val calories: Int,
    val protein: Float,
    val carbs: Float,
    val fat: Float,
    val category: String, // e.g., "Proteins", "Grains", "Fruits & Veg", "Dairy", "Beverages", "Meals", "Custom"
    val itemType: ItemType = ItemType.FOOD,
    val isCustom: Boolean = false
)

object PresetFoodRepository {
    private val basePresets = listOf(
        // Proteins
        PresetFood("Grilled Chicken Breast (100g)", 165, 31f, 0f, 3.6f, "Proteins", ItemType.FOOD),
        PresetFood("Salmon Fillet (100g)", 206, 22f, 0f, 12f, "Proteins", ItemType.FOOD),
        PresetFood("Tofu (100g)", 76, 8f, 2f, 4.8f, "Proteins", ItemType.FOOD),
        PresetFood("Eggs (2 large)", 143, 12.6f, 0.8f, 9.5f, "Proteins", ItemType.FOOD),
        PresetFood("Lean Beef (100g)", 250, 26f, 0f, 15f, "Proteins", ItemType.FOOD),
        PresetFood("Shrimp (100g)", 99, 24f, 0.2f, 0.3f, "Proteins", ItemType.FOOD),
        
        // Grains & Carbs
        PresetFood("Oatmeal (1 cup cooked)", 166, 6f, 28f, 3.6f, "Grains", ItemType.FOOD),
        PresetFood("White Rice (1 cup cooked)", 205, 4.2f, 45f, 0.4f, "Grains", ItemType.FOOD),
        PresetFood("Brown Rice (1 cup cooked)", 218, 4.5f, 46f, 1.6f, "Grains", ItemType.FOOD),
        PresetFood("Whole Wheat Bread (1 slice)", 81, 4f, 14f, 1f, "Grains", ItemType.FOOD),
        PresetFood("Pasta (1 cup cooked)", 220, 8f, 43f, 1.3f, "Grains", ItemType.FOOD),
        PresetFood("Sweet Potato (1 medium)", 112, 2f, 26f, 0.1f, "Grains", ItemType.FOOD),

        // Fruits & Vegetables
        PresetFood("Banana (1 medium)", 105, 1.3f, 27f, 0.3f, "Fruits & Veg", ItemType.FOOD),
        PresetFood("Apple (1 medium)", 95, 0.5f, 25f, 0.3f, "Fruits & Veg", ItemType.FOOD),
        PresetFood("Avocado (1/2 medium)", 114, 1.3f, 6f, 10.5f, "Fruits & Veg", ItemType.FOOD),
        PresetFood("Broccoli (1 cup)", 31, 2.5f, 6f, 0.3f, "Fruits & Veg", ItemType.FOOD),
        PresetFood("Mixed Salad (1 bowl)", 50, 2f, 10f, 1f, "Fruits & Veg", ItemType.FOOD),

        // Dairy
        PresetFood("Greek Yogurt (1 cup)", 130, 20f, 9f, 0.4f, "Dairy", ItemType.FOOD),
        PresetFood("Whole Milk (1 cup)", 150, 8f, 12f, 8f, "Dairy", ItemType.DRINK),
        PresetFood("Cheddar Cheese (1 oz)", 113, 7f, 0.4f, 9f, "Dairy", ItemType.FOOD),
        PresetFood("Almond Milk (1 cup)", 30, 1f, 1f, 2.5f, "Dairy", ItemType.DRINK),

        // Beverages & Drinks
        PresetFood("Protein Shake (1 scoop + water)", 160, 30f, 3f, 2f, "Beverages", ItemType.DRINK),
        PresetFood("Black Coffee / Tea (1 cup)", 2, 0.3f, 0.4f, 0f, "Beverages", ItemType.DRINK),
        PresetFood("Orange Juice (1 cup)", 112, 1.7f, 26f, 0.5f, "Beverages", ItemType.DRINK),
        PresetFood("Green Smoothie (1 glass)", 180, 4f, 38f, 1.5f, "Beverages", ItemType.DRINK),
        PresetFood("Water (500ml)", 0, 0f, 0f, 0f, "Beverages", ItemType.DRINK),

        // Complete Meals
        PresetFood("Chicken Salad Bowl", 450, 35f, 25f, 22f, "Meals", ItemType.FOOD),
        PresetFood("Beef Burrito", 650, 28f, 75f, 26f, "Meals", ItemType.FOOD),
        PresetFood("Avocado Toast with Egg", 320, 14f, 28f, 18f, "Meals", ItemType.FOOD)
    )

    fun getPresets(context: Context): List<PresetFood> {
        val storage = LocalStorage(context)
        val customPresets = storage.loadCustomPresets()
        return basePresets + customPresets
    }
}
