package com.example.wordle_1A2B.ui.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.wordle_1A2B.ui.component.fragment.game.GameRepository
import com.example.wordle_1A2B.ui.component.fragment.game.GameViewModel
import com.example.wordle_1A2B.ui.component.fragment.game.game_mode_select.GameModeSelectViewModel
import com.example.wordle_1A2B.ui.component.fragment.homepage.HomepageRepository
import com.example.wordle_1A2B.ui.component.fragment.homepage.HomepageViewModel

class BaseModelFactory <T> (private val repository: T) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(HomepageViewModel::class.java) -> HomepageViewModel(repository as HomepageRepository) as T
            modelClass.isAssignableFrom(GameViewModel::class.java) -> GameViewModel(repository as GameRepository) as T
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}