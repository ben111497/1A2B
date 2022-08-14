package com.example.app_1A2B.ui.factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.app_1A2B.data.local.LocalData
import com.example.app_1A2B.ui.component.fragment.game.game.GameViewModel
import com.example.app_1A2B.ui.component.fragment.homepage.HomepageViewModel
import kotlinx.coroutines.DelicateCoroutinesApi

@DelicateCoroutinesApi
class BaseModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(HomepageViewModel::class.java) -> HomepageViewModel(LocalData(context)) as T
            modelClass.isAssignableFrom(GameViewModel::class.java) -> GameViewModel(LocalData(context)) as T
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}