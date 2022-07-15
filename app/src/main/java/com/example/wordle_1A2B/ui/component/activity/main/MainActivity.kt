package com.example.wordle_1A2B.ui.component.activity.main

import androidx.lifecycle.SavedStateViewModelFactory
import androidx.lifecycle.ViewModelProviders
import com.example.wordle_1A2B.data.local.database.DataBase
import com.example.wordle_1A2B.databinding.ActivityMainBinding
import com.example.wordle_1A2B.tools.MyObserver
import com.example.wordle_1A2B.ui.base.BaseActivity

class MainActivity: BaseActivity<MainViewModel, ActivityMainBinding>() {
    override fun initViewBinding() {
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun initViewModel() {
        viewModel = ViewModelProviders.of(this, SavedStateViewModelFactory(application, this)).get(MainViewModel::class.java)
    }

    override fun observeViewModel() {}

    override fun init() {
        DataBase.instance(this)
        //viewModel.repository.name = SharedPreferences(application).getName()
        lifecycle.addObserver(MyObserver())
    }

    override fun setListener() {
    }
}