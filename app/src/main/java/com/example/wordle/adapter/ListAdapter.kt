package com.example.wordle.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.wordle.R
import com.example.wordle.databinding.ItemListBinding
import com.example.wordle.api.AttractionsClassRes
import com.example.wordle.tools.AsyncImageLoader

class ListAdapter(context: Context, list: ArrayList<AttractionsClassRes.Data>)
    : ArrayAdapter<AttractionsClassRes.Data>(context, 0, list) {
    private var listener: Listener? = null

    interface Listener {
        fun onItemClick(index: Int)
    }

    private class ViewHolder(v: View) {
        val tvName: TextView = v.findViewById(R.id.tvName)
        val tvAddress: TextView = v.findViewById(R.id.tvAddress)
        val tvInfo: TextView = v.findViewById(R.id.tvInfo)
        val tvDetail: TextView = v.findViewById(R.id.tvDetail)
        val imgPicture: ImageView = v.findViewById(R.id.imgPicture)
        val clItem: ConstraintLayout = v.findViewById(R.id.clItem)
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View
        val holder: ViewHolder

        if (convertView == null) {
            view = ItemListBinding.inflate(LayoutInflater.from(parent.context), parent, false).root
            holder = ViewHolder(view)
            view.tag = holder
        } else {
            holder = convertView.tag as ViewHolder
            view = convertView
        }

        val item = getItem(position) ?: return view

        holder.tvName.text = item.name ?: ""
        holder.tvAddress.text = item.address ?: ""
        holder.tvInfo.text = item.introduction ?: ""

        holder.clItem.setOnClickListener { listener?.onItemClick(position) }

        if (item.images.isNullOrEmpty())
            holder.imgPicture.setImageDrawable(context.getDrawable(R.drawable.image_error))
        else
            AsyncImageLoader.loadImage(holder.imgPicture, item.images[0].src ?: "")

        return view
    }

    fun setListener(l: Listener) { listener = l }
}