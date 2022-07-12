package com.example.wordle.activity.repository

import androidx.lifecycle.SavedStateHandle

class MainRepository {
    private var saveState: SavedStateHandle? = null
    var name: String = ""

    interface MainListener {
        fun onNameChange(name: String)
    }

    fun setSaveState(value: SavedStateHandle) { saveState = value }
    fun getSaveState(): SavedStateHandle? = saveState
}