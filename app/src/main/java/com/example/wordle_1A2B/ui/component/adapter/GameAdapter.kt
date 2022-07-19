package com.example.wordle_1A2B.ui.component.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.Group
import com.example.wordle_1A2B.R
import com.example.wordle_1A2B.data.dto.GameClass
import com.example.wordle_1A2B.data.dto.GameResultStatus
import com.example.wordle_1A2B.databinding.ItemGameBinding

class GameAdapter(context: Context, private val word: Int, private val list: ArrayList<GameClass.Reply>)
    : ArrayAdapter<GameClass.Reply>(context, 0, list) {
    private var listener: Listener? = null

    interface Listener {
        fun onItemClick(index: Int)
    }

    private class ViewHolder(v: View) {
        val tvAnswer1: TextView = v.findViewById(R.id.tvAnswer1)
        val tvAnswer2: TextView = v.findViewById(R.id.tvAnswer2)
        val tvAnswer3: TextView = v.findViewById(R.id.tvAnswer3)
        val tvAnswer4: TextView = v.findViewById(R.id.tvAnswer4)
        val tvAnswer5: TextView = v.findViewById(R.id.tvAnswer5)
        val tvAnswer6: TextView = v.findViewById(R.id.tvAnswer6)
        val tv1A: TextView = v.findViewById(R.id.tv1A)
        val tv2B: TextView = v.findViewById(R.id.tv2B)
        val gpResult: Group = v.findViewById(R.id.gpResult)
        val clItem: ConstraintLayout = v.findViewById(R.id.clItem)
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View
        val holder: ViewHolder

        if (convertView == null) {
            view = ItemGameBinding.inflate(LayoutInflater.from(parent.context), parent, false).root
            holder = ViewHolder(view)
            view.tag = holder
        } else {
            holder = convertView.tag as ViewHolder
            view = convertView
        }

        val item = list[position]

        holder.clItem.setBackgroundColor(context.getColor(if (position % 2 == 0) R.color.blue_85C9D1 else R.color.blue_59ABB5))

        holder.tvAnswer5.visibility = if (word >= 5) View.VISIBLE else View.GONE
        holder.tvAnswer6.visibility = if (word >= 6) View.VISIBLE else View.GONE

        for (i in 0 until word) {
            when (i) {
                0 -> holder.tvAnswer1.text = if (i >= item.answer.size) "" else "${item.answer[i]}"
                1 -> holder.tvAnswer2.text = if (i >= item.answer.size) "" else "${item.answer[i]}"
                2 -> holder.tvAnswer3.text = if (i >= item.answer.size) "" else "${item.answer[i]}"
                3 -> holder.tvAnswer4.text = if (i >= item.answer.size) "" else "${item.answer[i]}"
                4 -> holder.tvAnswer5.text = if (i >= item.answer.size) "" else "${item.answer[i]}"
                5 -> holder.tvAnswer6.text = if (i >= item.answer.size) "" else "${item.answer[i]}"
            }
        }

        if (item.result.size == word) {
            holder.gpResult.visibility = View.VISIBLE
            holder.tv1A.text = "${item.result.count { it == GameResultStatus.Correct }}"
            holder.tv2B.text = "${item.result.count { it == GameResultStatus.PositionError }}"
        } else holder.gpResult.visibility = View.INVISIBLE

        return view
    }

    fun setListener(l: Listener) { listener = l }
}