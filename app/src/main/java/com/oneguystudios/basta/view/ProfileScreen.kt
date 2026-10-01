package com.oneguystudios.basta.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.oneguystudios.basta.model.UserProfile

@Composable
fun ProfileScreen(
    userProfile: UserProfile,
    onUpdateProfile: (String, Int, Float, Float, Int, List<String>, List<String>) -> Unit
) {
    var sex by remember(userProfile.sex) { mutableStateOf(userProfile.sex) }
    var age by remember(userProfile.age) { mutableStateOf(userProfile.age.toString()) }
    var height by remember(userProfile.height) { mutableStateOf(userProfile.height.toString()) }
    var weight by remember(userProfile.weight) { mutableStateOf(userProfile.weight.toString()) }
    var calorieGoal by remember(userProfile.calorieGoal) { mutableStateOf(userProfile.calorieGoal.toString()) }
    var allergiesText by remember(userProfile.allergies) { mutableStateOf(userProfile.allergies.joinToString(", ")) }
    var preferencesText by remember(userProfile.preferences) { mutableStateOf(userProfile.preferences.joinToString(", ")) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "User Profile & Settings",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        // BMI Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Your Body Mass Index (BMI)", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "%.1f".format(userProfile.bmi),
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.secondary
                )
                Text(
                    text = "Category: ${userProfile.bmiCategory}",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        // Editable Form Card
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Personal Information & Goals",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )

                // Sex selection
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Sex:")
                    RadioButton(selected = sex == "Male", onClick = { sex = "Male" })
                    Text("Male")
                    RadioButton(selected = sex == "Female", onClick = { sex = "Female" })
                    Text("Female")
                }

                OutlinedTextField(
                    value = age,
                    onValueChange = { age = it },
                    label = { Text("Age") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = height,
                    onValueChange = { height = it },
                    label = { Text("Height (cm)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = weight,
                    onValueChange = { weight = it },
                    label = { Text("Weight (kg)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = calorieGoal,
                    onValueChange = { calorieGoal = it },
                    label = { Text("Daily Calorie Goal (kcal)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = allergiesText,
                    onValueChange = { allergiesText = it },
                    label = { Text("Allergies (comma separated)") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = preferencesText,
                    onValueChange = { preferencesText = it },
                    label = { Text("Preferences (comma separated)") },
                    modifier = Modifier.fillMaxWidth()
                )

                Button(
                    onClick = {
                        val parsedAge = age.toIntOrNull() ?: 20
                        val parsedHeight = height.toFloatOrNull() ?: 175f
                        val parsedWeight = weight.toFloatOrNull() ?: 70f
                        val parsedCalorieGoal = calorieGoal.toIntOrNull() ?: 2000
                        val parsedAllergies = allergiesText.split(",").map { it.trim() }.filter { it.isNotEmpty() }
                        val parsedPreferences = preferencesText.split(",").map { it.trim() }.filter { it.isNotEmpty() }

                        onUpdateProfile(
                            sex,
                            parsedAge,
                            parsedHeight,
                            parsedWeight,
                            parsedCalorieGoal,
                            parsedAllergies,
                            parsedPreferences
                        )
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Save Profile")
                }
            }
        }
    }
}
