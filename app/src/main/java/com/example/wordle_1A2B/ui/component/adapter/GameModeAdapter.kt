package com.example.wordle_1A2B.ui.component.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.wordle_1A2B.R
import com.example.wordle_1A2B.data.dto.GameMode
import com.example.wordle_1A2B.databinding.ItemGameModeBinding
import com.example.wordle_1A2B.tools.AsyncImageLoader

class GameModeAdapter (val context: Context, private val list: List<GameMode>): RecyclerView.Adapter<GameModeAdapter.ViewHolder>() {
    private lateinit var listener: Listener
    private var userCoin = 0

    interface Listener {
        fun onEnterClick(data: GameMode)
    }

    fun setListener(l: Listener) { listener = l }

    fun setUserCoin(coin: Int) { userCoin = coin }

    fun getUserCoin() = userCoin

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val clItem: ConstraintLayout = v.findViewById(R.id.clItem)
        val imgModePicture: ImageView = v.findViewById(R.id.imgModePicture)
        val tvModeName: TextView = v.findViewById(R.id.tvModeName)
        val tvStart: TextView = v.findViewById(R.id.tvStart)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = ItemGameModeBinding.inflate(LayoutInflater.from(parent.context), parent, false).root
        return ViewHolder(v)
    }

    override fun getItemCount() = if (getRealSize() > 1) Integer.MAX_VALUE else getRealSize()

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[getRealPosition(position, getRealSize())]

        holder.tvModeName.text = when (item) {
            GameMode.No -> "無提示、數字不重複"
            GameMode.Repeat -> "無提示、數字可重複"
            GameMode.Hint -> "有提示、數字不重複"
            else -> "有提示、數字可重複"
        }

        val (modePic, color, btnPic) = when (item) {
            GameMode.No ->  Triple(R.drawable.no, R.color.green_4ECF6A, R.drawable.rp_rectangle_green4ecf6a_40)
            GameMode.Repeat -> Triple(R.drawable.repeat, R.color.blue_3478FF, R.drawable.rp_rectangle_blue3478ff_40)
            GameMode.Hint -> Triple(R.drawable.hint, R.color.red_FD4C4C, R.drawable.rp_rectangle_redfd4c4c_40)
            else -> Triple(R.drawable.hintandrepeatpng, R.color.purple_884EFC, R.drawable.rp_rectangle_purple884efc_40)
        }

        holder.imgModePicture.setImageResource(modePic)
        holder.tvModeName.setTextColor(context.getColor(color))
        holder.tvStart.setBackgroundResource(btnPic)

        holder.tvStart.setOnClickListener { listener.onEnterClick(item) }
        holder.clItem.setOnClickListener { listener.onEnterClick(item) }
    }

    private fun getRealSize() = if (list.isNullOrEmpty()) 0 else list.size

    private fun getRealPosition(position: Int, real: Int): Int = position % real
}

