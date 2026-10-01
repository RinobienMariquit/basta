package com.oneguystudios.basta.model

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject

class LocalStorage(context: Context) {
    private val prefs = context.getSharedPreferences("basta_prefs", Context.MODE_PRIVATE)

    // UserProfile Persistence
    fun saveUserProfile(profile: UserProfile) {
        prefs.edit().apply {
            putString("profile_sex", profile.sex)
            putInt("profile_age", profile.age)
            putFloat("profile_height", profile.height)
            putFloat("profile_weight", profile.weight)
            putInt("profile_calorie_goal", profile.calorieGoal)
            putString("profile_allergies", profile.allergies.joinToString(","))
            putString("profile_preferences", profile.preferences.joinToString(","))
            apply()
        }
    }

    fun loadUserProfile(): UserProfile {
        val sex = prefs.getString("profile_sex", "Male") ?: "Male"
        val age = prefs.getInt("profile_age", 20)
        val height = prefs.getFloat("profile_height", 175.0f)
        val weight = prefs.getFloat("profile_weight", 70.0f)
        val calorieGoal = prefs.getInt("profile_calorie_goal", 2000)
        val allergiesStr = prefs.getString("profile_allergies", "") ?: ""
        val preferencesStr = prefs.getString("profile_preferences", "") ?: ""

        val allergies = if (allergiesStr.isBlank()) emptyList() else allergiesStr.split(",").map { it.trim() }
        val preferences = if (preferencesStr.isBlank()) emptyList() else preferencesStr.split(",").map { it.trim() }

        return UserProfile(sex, age, height, weight, calorieGoal, allergies, preferences)
    }

    // DailyLog / MealItems Persistence
    fun saveMealItems(items: List<MealItem>) {
        val jsonArray = JSONArray()
        for (item in items) {
            val obj = JSONObject().apply {
                put("id", item.id)
                put("name", item.name)
                put("calories", item.calories)
                put("protein", item.protein.toDouble())
                put("carbs", item.carbs.toDouble())
                put("fat", item.fat.toDouble())
                put("mealCategory", item.mealCategory.name)
                put("itemType", item.itemType.name)
            }
            jsonArray.put(obj)
        }
        prefs.edit().putString("meal_items", jsonArray.toString()).apply()
    }

    fun loadMealItems(): List<MealItem> {
        val jsonStr = prefs.getString("meal_items", null) ?: return emptyList()
        val list = mutableListOf<MealItem>()
        try {
            val jsonArray = JSONArray(jsonStr)
            for (i in 0 until jsonArray.length()) {
                val obj = jsonArray.getJSONObject(i)
                val item = MealItem(
                    id = obj.getString("id"),
                    name = obj.getString("name"),
                    calories = obj.getInt("calories"),
                    protein = obj.getDouble("protein").toFloat(),
                    carbs = obj.getDouble("carbs").toFloat(),
                    fat = obj.getDouble("fat").toFloat(),
                    mealCategory = MealCategory.valueOf(obj.getString("mealCategory")),
                    itemType = ItemType.valueOf(obj.getString("itemType"))
                )
                list.add(item)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return list
    }

    // Custom Preset Foods Persistence
    fun saveCustomPreset(preset: PresetFood) {
        val existing = loadCustomPresets().toMutableList()
        existing.removeIf { it.name.equals(preset.name, ignoreCase = true) }
        existing.add(preset.copy(isCustom = true))

        val jsonArray = JSONArray()
        for (p in existing) {
            val obj = JSONObject().apply {
                put("name", p.name)
                put("calories", p.calories)
                put("protein", p.protein.toDouble())
                put("carbs", p.carbs.toDouble())
                put("fat", p.fat.toDouble())
                put("category", p.category)
                put("itemType", p.itemType.name)
            }
            jsonArray.put(obj)
        }
        prefs.edit().putString("custom_presets", jsonArray.toString()).apply()
    }

    fun deleteCustomPreset(presetName: String) {
        val existing = loadCustomPresets().toMutableList()
        existing.removeIf { it.name.equals(presetName, ignoreCase = true) }

        val jsonArray = JSONArray()
        for (p in existing) {
            val obj = JSONObject().apply {
                put("name", p.name)
                put("calories", p.calories)
                put("protein", p.protein.toDouble())
                put("carbs", p.carbs.toDouble())
                put("fat", p.fat.toDouble())
                put("category", p.category)
                put("itemType", p.itemType.name)
            }
            jsonArray.put(obj)
        }
        prefs.edit().putString("custom_presets", jsonArray.toString()).apply()
    }

    fun loadCustomPresets(): List<PresetFood> {
        val jsonStr = prefs.getString("custom_presets", null) ?: return emptyList()
        val list = mutableListOf<PresetFood>()
        try {
            val jsonArray = JSONArray(jsonStr)
            for (i in 0 until jsonArray.length()) {
                val obj = jsonArray.getJSONObject(i)
                val preset = PresetFood(
                    name = obj.getString("name"),
                    calories = obj.getInt("calories"),
                    protein = obj.getDouble("protein").toFloat(),
                    carbs = obj.getDouble("carbs").toFloat(),
                    fat = obj.getDouble("fat").toFloat(),
                    category = obj.getString("category"),
                    itemType = ItemType.valueOf(obj.getString("itemType")),
                    isCustom = true
                )
                list.add(preset)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return list
    }
}
