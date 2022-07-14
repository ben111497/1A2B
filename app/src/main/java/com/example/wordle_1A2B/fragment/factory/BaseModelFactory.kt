package com.example.wordle_1A2B.fragment.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.wordle_1A2B.fragment.repository.HomepageRepository
import com.example.wordle_1A2B.fragment.viewModel.HomepageViewModel

class BaseModelFactory <T> (private val repository: T) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        when {
            modelClass.isAssignableFrom(HomepageViewModel::class.java) -> return HomepageViewModel(repository as HomepageRepository) as T
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}