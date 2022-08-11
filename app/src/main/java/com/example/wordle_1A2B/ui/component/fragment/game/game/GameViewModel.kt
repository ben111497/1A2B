package com.example.wordle_1A2B.ui.component.fragment.game.game

import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.lifecycle.ViewModel
import com.example.wordle_1A2B.data.dto.GameClass
import com.example.wordle_1A2B.data.dto.GameMode
import com.example.wordle_1A2B.data.dto.GameResultStatus
import com.example.wordle_1A2B.data.dto.SingleLiveData
import com.example.wordle_1A2B.data.local.LocalData
import com.example.wordle_1A2B.helper.add
import com.google.gson.Gson
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.flowOn
import kotlin.random.Random

@DelicateCoroutinesApi
class GameViewModel constructor(private val local: LocalData): ViewModel() {
    var word: Int = 0
    var waitingDisplay: Boolean = false
    var gameMode: GameMode = GameMode.No
    var answerList = ArrayList<Int>()
    val isGameWin: Boolean get() = getViewList()[getReplyCount()].result.count { it == GameResultStatus.Correct } == word

    val viewList = SingleLiveData<ArrayList<GameClass.Reply>>().also { it.value = ArrayList() }
    val replyCount = SingleLiveData<Int>().also { it.value = 0 }
    val message = SingleLiveData<String>().also { it.value = "" }
    val isDialogOn = SingleLiveData<Boolean>().also { it.value = false }
    val coin = SingleLiveData<Int>().also { it.value = 0 }
    val hintList = SingleLiveData<ArrayList<Hint>>().also { it.value = ArrayList<Hint>() }

    class Hint(val position: Int, val answer: Int)

    /**
     * Control Data
     */

    fun setWordNumber(number: Int) { word = number }

    fun getViewList() = viewList.value!!
    fun initViewList() {
        viewList.value?.clear()
        viewList.value?.add(GameClass.Reply(ArrayList<Int>(), ArrayList<GameResultStatus>()))
        viewList.value = getViewList()
    }
    fun addViewList() {
        viewList.value?.add(GameClass.Reply(ArrayList<Int>(), ArrayList<GameResultStatus>()))
        viewList.value = getViewList()
    }

    fun setReplyCount(count: Int) = count.also { replyCount.value = it }
    fun getReplyCount() = replyCount.value!!

    fun setMessage(msg: String) = msg.also { message.value = it }
    
    fun setIsDialogOn(isOn: Boolean) = isOn.also { isDialogOn.value = it }
    fun getIsDialogOn() = isDialogOn.value!!

    fun getCoin() = coin.value ?: 0
    fun addCoin(coin: Int) {
        val currentCoin = (getCoin() + coin).takeIf { it > 0 } ?: 0
        this.coin.value = currentCoin
        local.setCoin("", currentCoin)
    }
    fun setReduceCoin(coin: Int) {
        val currentCoin = (getCoin() - coin).takeIf { it > 0 } ?: 0
        this.coin.value = currentCoin
        local.setCoin("", currentCoin)
    }

    fun initHintList() = hintList.value?.clear()
    fun getHintList() = hintList.value!!

    /**
     * Local & Api
     */

    suspend fun getLocalDataByNam(resOfT: Class<*>) {
        local.getAPIByName(resOfT).collect {
            val json = Gson().fromJson(it.json, resOfT)
            Log.e("Local: ${resOfT.simpleName}", json.toString())

            //when (resOfT) { }
        }
    }

    fun setLocalCoin(userID: String, coin: Int) = local.setCoin(userID, coin)
    fun getLocalCoin() {
        GlobalScope.launch(Dispatchers.Main) {
            local.getCoin().flowOn(Dispatchers.IO).collect { coin.value = it?.coin }
        }
    }

    /**
     * Other Function
     */

    fun initViewModelData() {
        initViewList()
        setReplyCount(0)
        setAnswer()
        setIsDialogOn(false)
        setMessage("")
        initHintList()
    }

    fun getClickObject(): View.OnClickListener {
        return View.OnClickListener { v ->
            if (waitingDisplay || isGameWin) return@OnClickListener

            v?.let { view ->
                val number = (view as TextView).text.toString().toInt()
                when {
                    getViewList()[getReplyCount()].answer.size >= word -> setMessage("已填滿數字")
                    getViewList()[getReplyCount()].answer.contains(number)
                            && (gameMode == GameMode.Hint || gameMode == GameMode.No) -> setMessage("數字重複")
                    else -> {
                        getViewList()[getReplyCount()].answer.add(number)
                        viewList.value = getViewList()
                    }
                }
            }
        }
    }

    fun setAnswer() {
        answerList.clear()
        for (i in 0 until word) {
            while (answerList.size < i + 1) {
                val number = Random(System.currentTimeMillis() + answerList.size).nextInt(0, 10)
                when (gameMode) {
                    GameMode.Repeat, GameMode.RepeatAndHint -> answerList.add(number)
                    else -> if (!answerList.contains(number)) answerList.add(number)
                }
            }
        }
        Log.e("word", "$word")
        Log.e("answer", "$answerList")
    }

    fun backAnswer(count: Int = getReplyCount()) {
        if (isGameWin) {
            setMessage("遊戲已結束")
            return
        }
        if (waitingDisplay) return

        getViewList()[count].answer.let { if (it.size > 0) it.remove(it[it.lastIndex]) }
        viewList.value = getViewList()
    }

    fun clearAnswer(count: Int = getReplyCount()) {
        if (isGameWin) {
            setMessage("遊戲已結束")
            return
        }
        if (waitingDisplay) return

        getViewList()[count].answer.clear()
        viewList.value = getViewList()
    }

    fun confirmAnswer() {
        if (isGameWin) {
            setMessage("遊戲已結束")
            return
        }
        if (waitingDisplay) return

        val data = getViewList()[getReplyCount()]
        val list = ArrayList<Int>().also { it.addAll(answerList) }

        if (data.answer.size != word) {
            setMessage("請填寫完整！")
            return
        }

        //A
        for (i in 0 until word) {
            data.result.add(if (data.answer[i] == answerList[i]) {
                list.remove(answerList[i])
                GameResultStatus.Correct
            } else GameResultStatus.NotComparison)
        }
        //B
        for (i in 0 until word) {
            if (data.result[i] == GameResultStatus.NotComparison) {
                if (list.contains(data.answer[i])) {
                    list.remove(data.answer[i])
                    data.result[i] = GameResultStatus.PositionError
                } else  data.result[i] = GameResultStatus.Error
            }
        }

        viewList.value = getViewList()
        when {
            isGameWin -> {
                setWinCoin()
                setMessage("遊戲勝利！")
                setIsDialogOn(true)
            }
            gameMode == GameMode.Hint || gameMode == GameMode.RepeatAndHint -> {
                waitingDisplay = true
                Handler(Looper.getMainLooper()).postDelayed({
                    setReplyCount(getReplyCount() + 1)
                    addViewList()
                    waitingDisplay = false
                }, ((word - 1) * 250 + 500).toLong())
            }
            else -> {
                setReplyCount(getReplyCount() + 1)
                addViewList()
            }
        }
    }

    fun isCoinEnough(coin: Int) = getCoin() >= coin

    fun useHint(position: Int) {
        val hint = answerList[position]
        hintList.add(Hint(position, hint))
    }

    private fun setWinCoin() {
        val power: Double = when (word) {
            4 -> 1.0
            5 -> 1.25
            else -> 1.5
        }

        val score = when (gameMode) {
            GameMode.Repeat -> 200 * power - 20 * power * getReplyCount() / (word + 1)
            GameMode.No -> 150 * power- 15 * power * getReplyCount() / (word + 1)
            GameMode.Hint -> 100 * power - 10 * power * getReplyCount() / (word + 1)
            else -> 50 * power - 5 * power * getReplyCount() / (word + 1)
        }.toInt().takeIf { it > 0 } ?: 0

        addCoin(score)
    }
}