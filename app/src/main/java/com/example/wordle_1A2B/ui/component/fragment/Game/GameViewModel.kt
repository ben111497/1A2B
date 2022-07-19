package com.example.wordle_1A2B.ui.component.fragment.Game

import android.content.Context
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.wordle_1A2B.data.dto.GameClass
import com.example.wordle_1A2B.data.dto.GameResultStatus
import com.example.wordle_1A2B.utils.showToast

class GameViewModel constructor(private val context: Context, private val repository: GameRepository): ViewModel() {
    var word: Int = 0
    var obj: View.OnClickListener? = null
    var answerList = ArrayList<Int>()

    val viewList = MutableLiveData<ArrayList<GameClass.Reply>>().also { it.value = ArrayList() }
    val replyCount = MutableLiveData<Int>().also { it.value = 0 }

    /**
     * Control Data
     */

    fun setWordNumber(number: Int) { word = number}

    fun getViewList() = viewList.value!!
    fun initViewList() {
        viewList.value?.clear()
        viewList.value?.add(GameClass.Reply(ArrayList<Int>(), ArrayList<GameResultStatus>()))
        viewList.value = getViewList()
    }

    fun getReplyCount() = replyCount.value!!

    fun getClickObject(): View.OnClickListener {
        return obj ?: run {
            val obj = View.OnClickListener { v ->
                v?.let { view ->
                    val number = (view as TextView).text.toString().toInt()
                    when {
                        getViewList()[getReplyCount()].answer.size >= word -> context.showToast("已填滿數字", Toast.LENGTH_SHORT)
                        getViewList()[getReplyCount()].answer.contains(number) -> context.showToast("數字重複！", Toast.LENGTH_SHORT)
                        else -> {
                            getViewList()[0].answer.add(number)
                            viewList.value = getViewList()
                        }
                    }
                }
            }
            this.obj = obj
            obj
        }
    }

    fun setAnswer() {
        answerList.clear()
        for (i in 0 until word) {
            while (answerList.size < i + 1) {
                val number = (0 .. 9).random()
                if (!answerList.contains(number)) answerList.add(number)
            }
        }
        Log.e("answer", "$answerList")
    }

    /**
     * Other Function
     */

    fun clearCurrentAnswer(count: Int = getReplyCount()) {
        getViewList()[count].answer.clear()
        viewList.value = getViewList()
    }
}