package com.example.wordle_1A2B.ui.component.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.wordle_1A2B.R
import com.example.wordle_1A2B.api.AttractionsClassRes
import com.example.wordle_1A2B.databinding.ItemPictureSmallBinding
import com.example.wordle_1A2B.tools.AsyncImageLoader

class PictureSmallAdapter (val context: Context, private val list: ArrayList<AttractionsClassRes.Images>)
    : RecyclerView.Adapter<PictureSmallAdapter.ViewHolder>() {
    private lateinit var listener: Listener

    interface Listener {
        fun onClick(number: Int)
    }

    fun setListener(l: Listener) { listener = l }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val imgPicture: ImageView = v.findViewById(R.id.imgPicture)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = ItemPictureSmallBinding.inflate(LayoutInflater.from(parent.context), parent, false).root
        return ViewHolder(v)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        AsyncImageLoader.loadImage(holder.imgPicture, item.src ?: "")
        holder.imgPicture.setOnClickListener { listener.onClick(position) }
    }
}
