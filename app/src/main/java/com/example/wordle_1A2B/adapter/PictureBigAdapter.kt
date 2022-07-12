package com.example.wordle_1A2B.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.wordle_1A2B.R
import com.example.wordle_1A2B.api.AttractionsClassRes
import com.example.wordle_1A2B.databinding.ItemPictureBigBinding
import com.example.wordle_1A2B.tools.AsyncImageLoader

class PictureBigAdapter(private val context: Context, private val list: ArrayList<AttractionsClassRes.Images>):
    RecyclerView.Adapter<PictureBigAdapter.ViewHolder>() {
    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val imgPicture = v.findViewById<ImageView>(R.id.imgPicture)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = ItemPictureBigBinding.inflate(LayoutInflater.from(parent.context), parent, false).root
        return ViewHolder(v)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        AsyncImageLoader.loadImage(holder.imgPicture, item.src ?: "")
    }
}

