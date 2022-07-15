package com.example.wordle_1A2B.data.local

import android.content.Context
import android.content.SharedPreferences
import javax.inject.Inject

class SharedPreferences constructor(context: Context) {
    companion object {
        private const val Name = "Name"
    }

    private val sp: SharedPreferences = context.getSharedPreferences("1A2B", Context.MODE_PRIVATE)

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