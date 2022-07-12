package com.example.wordle.tools

import android.app.Application
import android.content.Context
import android.content.SharedPreferences

class SharedPreferences(application: Application) {
    private var context: Context = application
    companion object {
        private const val Name = "Name"
    }

    private val sp: SharedPreferences = application.getSharedPreferences("wordle", Context.MODE_PRIVATE)

    fun clear() = sp.edit().clear().apply()

    /*
    Set
     */
    fun setName(name: String) = sp.edit().putString(Name, name).apply()

    /*
    Get
     */
    fun getName(): String = sp.getString(Name,"") ?: ""
}