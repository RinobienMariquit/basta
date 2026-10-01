package com.oneguystudios.basta.view

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.oneguystudios.basta.model.DailyLog
import com.oneguystudios.basta.model.MealCategory
import com.oneguystudios.basta.model.UserProfile

@Composable
fun OverviewScreen(
    dailyLog: DailyLog,
    userProfile: UserProfile
) {
    val calorieTarget = userProfile.calorieGoal
    val caloriesConsumed = dailyLog.totalCalories
    val calorieProgress = (caloriesConsumed.toFloat() / calorieTarget).coerceIn(0f, 1f)

    val protein = dailyLog.totalProtein
    val carbs = dailyLog.totalCarbs
    val fat = dailyLog.totalFat
    val totalMacros = (protein + carbs + fat).coerceAtLeast(1f)

    val proteinFraction = protein / totalMacros
    val carbsFraction = carbs / totalMacros
    val fatFraction = fat / totalMacros

    // Meal-by-meal calories
    val breakfastCals = dailyLog.itemsForCategory(MealCategory.BREAKFAST).sumOf { it.calories }
    val lunchCals = dailyLog.itemsForCategory(MealCategory.LUNCH).sumOf { it.calories }
    val snackCals = dailyLog.itemsForCategory(MealCategory.SNACK).sumOf { it.calories }
    val dinnerCals = dailyLog.itemsForCategory(MealCategory.DINNER).sumOf { it.calories }

    // Dynamic recommendation based on student data
    val recommendationText = rememberRecommendation(caloriesConsumed, calorieTarget, protein, userProfile)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Daily Dashboard",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        // Recommendation Box
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(text = "💡", fontSize = 20.sp)
                    Text(
                        text = "Smart Dietary Recommendation",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onTertiaryContainer
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = recommendationText,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onTertiaryContainer
                )
            }
        }

        // Calorie Summary Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Calories Consumed",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "$caloriesConsumed / $calorieTarget kcal",
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(12.dp))
                LinearProgressIndicator(
                    progress = { calorieProgress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp),
                    strokeCap = StrokeCap.Round
                )
            }
        }

        // Adjusted Meal Chart (Meal-by-Meal Breakdown)
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Adjusted Meal Chart",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )

                MealBreakdownRow("Breakfast", breakfastCals, 500)
                MealBreakdownRow("Lunch", lunchCals, 600)
                MealBreakdownRow("Snacks", snackCals, 200)
                MealBreakdownRow("Dinner", dinnerCals, 700)
            }
        }

        // Macro Breakdown Card with Pie Chart representation using Canvas
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Macro Nutrient Breakdown",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(16.dp))

                Box(
                    modifier = Modifier.size(160.dp),
                    contentAlignment = Alignment.Center
                ) {
                    val proteinColor = MaterialTheme.colorScheme.primary
                    val carbsColor = MaterialTheme.colorScheme.secondary
                    val fatColor = MaterialTheme.colorScheme.tertiary

                    Canvas(modifier = Modifier.fillMaxSize()) {
                        var startAngle = 0f
                        val strokeWidth = 32f

                        // Protein slice
                        val proteinSweep = proteinFraction * 360f
                        drawArc(
                            color = proteinColor,
                            startAngle = startAngle,
                            sweepAngle = proteinSweep,
                            useCenter = false,
                            style = Stroke(width = strokeWidth, cap = StrokeCap.Butt)
                        )
                        startAngle += proteinSweep

                        // Carbs slice
                        val carbsSweep = carbsFraction * 360f
                        drawArc(
                            color = carbsColor,
                            startAngle = startAngle,
                            sweepAngle = carbsSweep,
                            useCenter = false,
                            style = Stroke(width = strokeWidth, cap = StrokeCap.Butt)
                        )
                        startAngle += carbsSweep

                        // Fat slice
                        val fatSweep = fatFraction * 360f
                        drawArc(
                            color = fatColor,
                            startAngle = startAngle,
                            sweepAngle = fatSweep,
                            useCenter = false,
                            style = Stroke(width = strokeWidth, cap = StrokeCap.Butt)
                        )
                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "Macros", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text(text = "${dailyLog.items.size} Items", fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    MacroLegendItem(label = "Protein", value = "${protein}g", color = MaterialTheme.colorScheme.primary)
                    MacroLegendItem(label = "Carbs", value = "${carbs}g", color = MaterialTheme.colorScheme.secondary)
                    MacroLegendItem(label = "Fat", value = "${fat}g", color = MaterialTheme.colorScheme.tertiary)
                }
            }
        }

        // Quick Profile Summary Card
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "User Profile Summary",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Sex: ${userProfile.sex} | Age: ${userProfile.age}")
                Text(text = "Height: ${userProfile.height} cm | Weight: ${userProfile.weight} kg")
                Text(text = "BMI: %.1f (${userProfile.bmiCategory})".format(userProfile.bmi))
            }
        }
    }
}

@Composable
fun MealBreakdownRow(title: String, consumed: Int, target: Int) {
    val progress = (consumed.toFloat() / target).coerceIn(0f, 1f)
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = title, fontWeight = FontWeight.Medium)
            Text(text = "$consumed / $target kcal", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 14.sp)
        }
        Spacer(modifier = Modifier.height(4.dp))
        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp),
            strokeCap = StrokeCap.Round
        )
    }
}

@Composable
fun MacroLegendItem(label: String, value: String, color: androidx.compose.ui.graphics.Color) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
        Surface(
            modifier = Modifier.size(12.dp),
            shape = MaterialTheme.shapes.extraSmall,
            color = color
        ) {}
        Column {
            Text(text = label, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(text = value, fontWeight = FontWeight.Bold, fontSize = 14.sp)
        }
    }
}

fun rememberRecommendation(consumed: Int, target: Int, protein: Float, profile: UserProfile): String {
    val remaining = target - consumed
    return when {
        consumed == 0 -> "Welcome to Basta! Start logging your meals in the Planning tab to track your daily nutrition and get personalized recommendations."
        remaining > 500 -> "You have $remaining kcal remaining today. Consider a protein-rich meal or healthy snack to hit your daily nutrition goals!"
        remaining in 1..500 -> "You're getting close to your daily calorie target ($remaining kcal left). Balance your dinner with some greens and lean protein."
        remaining < 0 -> "You've exceeded your target by ${-remaining} kcal. Don't worry! Try balancing it out with a light walk or lighter meals tomorrow."
        else -> "Great job balancing your meals today! Your calorie intake is right on target."
    }
}
