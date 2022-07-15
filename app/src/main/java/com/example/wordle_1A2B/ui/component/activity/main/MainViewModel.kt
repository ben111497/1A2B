package com.example.wordle_1A2B.ui.component.activity.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

//ViewModel
//定義一個 viewModel，用來跟 Repository 溝通取得資料，一般會配合 LiveData 使用
class MainViewModel(handle: SavedStateHandle) : ViewModel() {
    private val number = MutableLiveData<Int>().apply { value = 0 }
    val repository = MainRepository()   //存放與 UI 較無關聯的資料

    init {
        handle.get<Int>("KEY")?.let {
            number.value = it
            repository.setSaveState(handle)
        } ?: repository.setSaveState(SavedStateHandle().also { it.set("KEY", 0) })
    }

    fun getNumber(): MutableLiveData<Int> = number
    fun addNumber(value: Int) {
        number.value = number.value?.plus(value) ?: 0 + value
    }
}

//AndroidViewModel
//class MainViewModel(application: Application, val handle: SavedStateHandle) : AndroidViewModel(application) {
//    private val number = MutableLiveData<Int>().apply { value = 0 }
//    val repository: MainRepository by lazy { MainRepository() }
//    val sp = SharedPreferences(application)
//
//    fun getNumber(): MutableLiveData<Int> = number
//    fun addNumber(value: Int) {
//        number.value = number.value?.plus(value) ?: 0 + value
//    }
//
//    fun load() = sp.getName()
//}