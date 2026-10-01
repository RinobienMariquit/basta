package com.oneguystudios.basta.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.oneguystudios.basta.model.LocalStorage
import com.oneguystudios.basta.model.UserProfile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ProfileViewModel(application: Application) : AndroidViewModel(application) {
    private val storage = LocalStorage(application)
    private val _userProfile = MutableStateFlow(storage.loadUserProfile())
    val userProfile: StateFlow<UserProfile> = _userProfile.asStateFlow()

    fun updateProfile(
        sex: String,
        age: Int,
        height: Float,
        weight: Float,
        calorieGoal: Int,
        allergies: List<String>,
        preferences: List<String>
    ) {
        _userProfile.update {
            val updated = it.copy(
                sex = sex,
                age = age,
                height = height,
                weight = weight,
                calorieGoal = calorieGoal,
                allergies = allergies,
                preferences = preferences
            )
            storage.saveUserProfile(updated)
            updated
        }
    }
}
