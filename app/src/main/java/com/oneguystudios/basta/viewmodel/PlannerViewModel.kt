package com.oneguystudios.basta.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.oneguystudios.basta.model.DailyLog
import com.oneguystudios.basta.model.LocalStorage
import com.oneguystudios.basta.model.MealItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class PlannerViewModel(application: Application) : AndroidViewModel(application) {
    private val storage = LocalStorage(application)
    private val _dailyLog = MutableStateFlow(DailyLog(items = storage.loadMealItems()))
    val dailyLog: StateFlow<DailyLog> = _dailyLog.asStateFlow()

    fun addMealItem(item: MealItem) {
        _dailyLog.update { currentLog ->
            val newItems = currentLog.items + item
            storage.saveMealItems(newItems)
            currentLog.copy(items = newItems)
        }
    }

    fun removeMealItem(itemId: String) {
        _dailyLog.update { currentLog ->
            val newItems = currentLog.items.filter { it.id != itemId }
            storage.saveMealItems(newItems)
            currentLog.copy(items = newItems)
        }
    }
}
