package com.example.app_1A2B.ui.component.activity.main

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