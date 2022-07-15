package com.example.wordle_1A2B.ui.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.wordle_1A2B.ui.component.fragment.homepage.HomepageRepository
import com.example.wordle_1A2B.ui.component.fragment.homepage.HomepageViewModel

class BaseModelFactory <T> (private val repository: T) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        when {
            modelClass.isAssignableFrom(HomepageViewModel::class.java) -> return HomepageViewModel(repository as HomepageRepository) as T
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}