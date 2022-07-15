package com.example.wordle_1A2B.ui.component.activity.main

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.SavedStateViewModelFactory
import androidx.lifecycle.ViewModelProviders
import com.example.wordle_1A2B.databinding.ActivityMainBinding
import com.example.wordle_1A2B.data.local.database.DataBase
import com.example.wordle_1A2B.tools.MyObserver

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    //建立並延後初始化 viewModel
    private val viewModel: MainViewModel by lazy {
        ViewModelProviders.of(this, SavedStateViewModelFactory(application, this)).get(MainViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        init()
    }

    private fun init() {
        DataBase.instance(this)
        //viewModel.repository.name = SharedPreferences(application).getName()
        lifecycle.addObserver(MyObserver())
    }
}