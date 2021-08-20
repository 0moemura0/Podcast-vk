package com.example.podcast_vk

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView

class ReactionAdapter : RecyclerView.Adapter<ReactionAdapter.ReactionViewHolder>() {

    val keys = listOf(R.drawable.i1, R.drawable.image2, R.drawable.imag3, R.drawable.image4)

    class ReactionViewHolder(val root: View) : RecyclerView.ViewHolder(root) {

        val back = root.findViewById<View>(R.id.background)
        val image = root.findViewById<ImageView>(R.id.emoji)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReactionViewHolder {
        val holder = ReactionViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_reaction, parent, false)
        )
        return holder
    }

    override fun onBindViewHolder(holder: ReactionViewHolder, position: Int) {
        holder.image.setImageResource(keys[position])
    }

    override fun getItemCount(): Int = keys.size
}