package com.example.wordle_1A2B.fragment.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.wordle_1A2B.fragment.repository.BlankRepository
import com.example.wordle_1A2B.fragment.viewModel.BlankViewModel

class BlankViewModelFactory(private val blankRepository: BlankRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BlankViewModel::class.java)) {
            return BlankViewModel(blankRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}