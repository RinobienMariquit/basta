package com.oneguystudios.basta.model

data class UserProfile(
    val sex: String = "Male",
    val age: Int = 20,
    val height: Float = 175.0f, // in cm
    val weight: Float = 70.0f,  // in kg
    val calorieGoal: Int = 2000,
    val allergies: List<String> = emptyList(),
    val preferences: List<String> = emptyList()
) {
    val bmi: Float
        get() {
            if (height <= 0f) return 0f
            val heightInMeters = height / 100f
            return weight / (heightInMeters * heightInMeters)
        }

    val bmiCategory: String
        get() = when {
            bmi < 18.5f -> "Underweight"
            bmi < 25.0f -> "Normal weight"
            bmi < 30.0f -> "Overweight"
            else -> "Obese"
        }
}
