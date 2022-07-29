package com.example.wordle_1A2B.ui.component.fragment.homepage

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.wordle_1A2B.data.local.LocalData
import com.example.wordle_1A2B.utils.addAll
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@DelicateCoroutinesApi
class HomepageViewModel constructor(private val local: LocalData): ViewModel() {
    val timerCount = MutableLiveData<Int>().also { it.value = 0 }
    val viewList = MutableLiveData<ArrayList<View>>().also { it.value = ArrayList<View>() }
    val coin = MutableLiveData<Int>().also { it.value = 0 }

    fun getTimerCountValue() = timerCount.value!!
    fun addCount() { timerCount.value = if (getTimerCountValue() in 0 until getViewList().size) getTimerCountValue() + 1 else 0 }

    fun addViews(views: ArrayList<View>) { viewList.addAll(views) }
    fun getViewList() = viewList.value!!

    fun getCoin() = coin.value ?: 0
    fun setCoin(userID: String, coin: Int) {
        this.coin.value = coin
        setLocalCoin(userID, coin)
    }

    /**
     * Local & Api
     */

    fun setLocalCoin(userID: String, coin: Int) = local.setCoin(userID, coin)
    fun getLocalCoin() {
        GlobalScope.launch(Dispatchers.IO) {
            local.getCoin().collect { GlobalScope.launch(Dispatchers.Main) { coin.value = it?.coin } }
        }
    }
}