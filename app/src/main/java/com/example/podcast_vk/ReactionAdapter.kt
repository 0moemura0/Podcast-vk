package com.example.podcast_vk

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView

class ReactionAdapter : RecyclerView.Adapter<ReactionAdapter.ReactionViewHolder>() {

    val keys = listOf(
        R.drawable.emoji_1,
        R.drawable.emoji_2,
        R.drawable.emoji_3,
        R.drawable.emoji_4,
        R.drawable.emoji_7,
        R.drawable.emoji_6,
        R.drawable.emoji_8,
        R.drawable.emoji_5
    )

    class ReactionViewHolder(root: View) : RecyclerView.ViewHolder(root) {

        val back = root.findViewById<View>(R.id.background)
        val image = root.findViewById<ImageView>(R.id.emoji)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReactionViewHolder {
        return ReactionViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_reaction, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ReactionViewHolder, position: Int) {
        holder.image.setImageResource(keys[position])
    }

    override fun getItemCount(): Int = keys.size
}