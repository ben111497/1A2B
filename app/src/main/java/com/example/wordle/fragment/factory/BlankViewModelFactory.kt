package com.example.wordle.fragment.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.wordle.fragment.repository.BlankRepository
import com.example.wordle.fragment.viewModel.BlankViewModel

class BlankViewModelFactory(private val blankRepository: BlankRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BlankViewModel::class.java)) {
            return BlankViewModel(blankRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}