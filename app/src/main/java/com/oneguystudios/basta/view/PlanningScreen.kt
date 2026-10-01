package com.oneguystudios.basta.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.oneguystudios.basta.model.*

@Composable
fun PlanningScreen(
    dailyLog: DailyLog,
    onAddItem: (MealItem) -> Unit,
    onRemoveItem: (String) -> Unit
) {
    var showAddDialog by remember { mutableStateOf(false) }
    var selectedCategoryForAdd by remember { mutableStateOf(MealCategory.BREAKFAST) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                selectedCategoryForAdd = MealCategory.BREAKFAST
                showAddDialog = true
            }) {
                Icon(Icons.Default.Add, contentDescription = "Add Meal Item")
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text(
                    text = "Meal Planner & Logger",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            val categories = listOf(
                MealCategory.BREAKFAST to "Breakfast",
                MealCategory.LUNCH to "Lunch",
                MealCategory.SNACK to "Snacks",
                MealCategory.DINNER to "Dinner"
            )

            items(categories) { (category, title) ->
                MealCategorySection(
                    categoryTitle = title,
                    category = category,
                    items = dailyLog.itemsForCategory(category),
                    onAddClick = {
                        selectedCategoryForAdd = category
                        showAddDialog = true
                    },
                    onRemoveItem = onRemoveItem
                )
            }
        }
    }

    if (showAddDialog) {
        AddMealDialog(
            defaultCategory = selectedCategoryForAdd,
            onDismiss = { showAddDialog = false },
            onConfirm = { newItem ->
                onAddItem(newItem)
                showAddDialog = false
            }
        )
    }
}

@Composable
fun MealCategorySection(
    categoryTitle: String,
    category: MealCategory,
    items: List<MealItem>,
    onAddClick: () -> Unit,
    onRemoveItem: (String) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = categoryTitle,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                IconButton(onClick = onAddClick) {
                    Icon(Icons.Default.Add, contentDescription = "Add to $categoryTitle")
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            if (items.isEmpty()) {
                Text(
                    text = "No items logged yet.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                items.forEach { item ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = item.name, fontWeight = FontWeight.SemiBold)
                            Text(
                                text = "${item.calories} kcal | P: ${item.protein}g, C: ${item.carbs}g, F: ${item.fat}g (${item.itemType})",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        IconButton(onClick = { onRemoveItem(item.id) }) {
                            Icon(
                                Icons.Default.Delete,
                                contentDescription = "Delete",
                                tint = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                    HorizontalDivider(modifier = Modifier.padding(vertical = 2.dp))
                }
            }
        }
    }
}

@Composable
fun AddMealDialog(
    defaultCategory: MealCategory,
    onDismiss: () -> Unit,
    onConfirm: (MealItem) -> Unit
) {
    val context = LocalContext.current
    val storage = remember { LocalStorage(context) }

    var name by remember { mutableStateOf("") }
    var calories by remember { mutableStateOf("") }
    var protein by remember { mutableStateOf("") }
    var carbs by remember { mutableStateOf("") }
    var fat by remember { mutableStateOf("") }
    var category by remember { mutableStateOf(defaultCategory) }
    var itemType by remember { mutableStateOf(ItemType.FOOD) }
    var saveAsPreset by remember { mutableStateOf(false) }

    var showPresetPicker by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Food / Drink Item") },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Button to open Searchable Preset Database Picker
                Button(
                    onClick = { showPresetPicker = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.Search, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Browse Food & Drink Presets")
                }

                Spacer(modifier = Modifier.height(4.dp))

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Item Name") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = calories,
                    onValueChange = { calories = it },
                    label = { Text("Calories (kcal)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = protein,
                        onValueChange = { protein = it },
                        label = { Text("Protein (g)") },
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = carbs,
                        onValueChange = { carbs = it },
                        label = { Text("Carbs (g)") },
                        modifier = Modifier.weight(1f)
                    )
                }
                OutlinedTextField(
                    value = fat,
                    onValueChange = { fat = it },
                    label = { Text("Fat (g)") },
                    modifier = Modifier.fillMaxWidth()
                )

                // Type selection (Food vs Drink)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Type:")
                    RadioButton(
                        selected = itemType == ItemType.FOOD,
                        onClick = { itemType = ItemType.FOOD }
                    )
                    Text("Food")
                    RadioButton(
                        selected = itemType == ItemType.DRINK,
                        onClick = { itemType = ItemType.DRINK }
                    )
                    Text("Drink")
                }

                // Checkbox to save as custom preset
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Checkbox(
                        checked = saveAsPreset,
                        onCheckedChange = { saveAsPreset = it }
                    )
                    Text("Save to Custom Food Presets")
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (name.isNotBlank()) {
                        val cal = calories.toIntOrNull() ?: 0
                        val p = protein.toFloatOrNull() ?: 0f
                        val c = carbs.toFloatOrNull() ?: 0f
                        val f = fat.toFloatOrNull() ?: 0f

                        if (saveAsPreset) {
                            val customPreset = PresetFood(
                                name = name,
                                calories = cal,
                                protein = p,
                                carbs = c,
                                fat = f,
                                category = "Custom",
                                itemType = itemType,
                                isCustom = true
                            )
                            storage.saveCustomPreset(customPreset)
                        }

                        onConfirm(
                            MealItem(
                                name = name,
                                calories = cal,
                                protein = p,
                                carbs = c,
                                fat = f,
                                mealCategory = category,
                                itemType = itemType
                            )
                        )
                    }
                }
            ) {
                Text("Add")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )

    if (showPresetPicker) {
        PresetFoodPickerDialog(
            onDismiss = { showPresetPicker = false },
            onSelectPreset = { preset ->
                name = preset.name
                calories = preset.calories.toString()
                protein = preset.protein.toString()
                carbs = preset.carbs.toString()
                fat = preset.fat.toString()
                itemType = preset.itemType
                showPresetPicker = false
            }
        )
    }
}

@Composable
fun PresetFoodPickerDialog(
    onDismiss: () -> Unit,
    onSelectPreset: (PresetFood) -> Unit
) {
    val context = LocalContext.current
    val storage = remember { LocalStorage(context) }

    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("All") }
    var refreshTrigger by remember { mutableStateOf(0) }

    val categories = listOf("All", "Proteins", "Grains", "Fruits & Veg", "Dairy", "Beverages", "Meals", "Custom")

    val allPresets = remember(refreshTrigger) {
        PresetFoodRepository.getPresets(context)
    }

    val filteredPresets = remember(searchQuery, selectedCategory, allPresets) {
        allPresets.filter { preset ->
            val matchesSearch = preset.name.contains(searchQuery, ignoreCase = true) ||
                    preset.category.contains(searchQuery, ignoreCase = true)
            val matchesCategory = selectedCategory == "All" || preset.category == selectedCategory
            matchesSearch && matchesCategory
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Preset Food Database") },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(450.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    label = { Text("Search food or drink...") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                // Category Filter Chips
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(categories) { cat ->
                        FilterChip(
                            selected = selectedCategory == cat,
                            onClick = { selectedCategory = cat },
                            label = { Text(cat) }
                        )
                    }
                }

                HorizontalDivider()

                // List of matching presets
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    items(filteredPresets) { preset ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onSelectPreset(preset) },
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(text = preset.name, fontWeight = FontWeight.Bold)
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = "${preset.calories} kcal | P: ${preset.protein}g | C: ${preset.carbs}g | F: ${preset.fat}g • [${preset.category}]",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                if (preset.isCustom) {
                                    IconButton(
                                        onClick = {
                                            storage.deleteCustomPreset(preset.name)
                                            refreshTrigger++
                                        }
                                    ) {
                                        Icon(
                                            Icons.Default.Delete,
                                            contentDescription = "Delete Custom Preset",
                                            tint = MaterialTheme.colorScheme.error
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {},
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Close")
            }
        }
    )
}
