package com.example.app_1A2B.ui.component.fragment.homepage

import android.view.View
import androidx.lifecycle.ViewModel
import com.example.app_1A2B.data.dto.SingleLiveData
import com.example.app_1A2B.data.local.LocalData
import com.example.app_1A2B.helper.addAll
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

@DelicateCoroutinesApi
class HomepageViewModel constructor(private val local: LocalData): ViewModel() {
    val timerCount = SingleLiveData<Int>().also { it.value = 0 }
    val viewList = SingleLiveData<ArrayList<View>>().also { it.value = ArrayList<View>() }
    val coin = SingleLiveData<Int>().also { it.value = 0 }

    fun getTimerCountValue() = timerCount.value!!
    fun setCount(value: Int) { timerCount.value = value }
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
        GlobalScope.launch(Dispatchers.Main) {
            local.getCoin().flowOn(Dispatchers.IO).collect { coin.value = it?.coin }
        }
    }
}