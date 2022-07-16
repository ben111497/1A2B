package com.example.wordle_1A2B.ui.component.fragment.homepage

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.wordle_1A2B.utils.addAll

class HomepageViewModel constructor(private val repository: HomepageRepository): ViewModel() {
    val timerCount = MutableLiveData<Int>().also { it.value = 0 }
    val viewList = MutableLiveData<ArrayList<View>>().also { it.value = ArrayList<View>() }

    fun getTimerCountValue() = timerCount.value!!
    fun addCount() { timerCount.value = if (getTimerCountValue() in 0 until getViewList().size) getTimerCountValue() + 1 else 0 }

    fun addViews(views: ArrayList<View>) { viewList.addAll(views) }
    fun getViewList() = viewList.value!!
}