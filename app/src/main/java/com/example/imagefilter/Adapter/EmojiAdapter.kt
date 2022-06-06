package com.example.imagefilter.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.imagefilter.Interface.FilterListFragmentListener
import com.example.imagefilter.R
import com.zomato.photofilters.utils.ThumbnailItem
import io.github.rockerhieu.emojicon.EmojiconTextView

class EmojiAdapter(private val context: Context,
                   private val emojiItemList: List<String>,
                   private val listener: EmojiAdapterListener): RecyclerView.Adapter<EmojiAdapter.EmojiViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmojiViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.emoji_item,parent,false)
        return EmojiViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: EmojiViewHolder, position: Int) {
        holder.emoji_text_view.setText(emojiItemList[position])
    }

    override fun getItemCount(): Int {
        return emojiItemList.size
    }

    inner class EmojiViewHolder(itemView:View):RecyclerView.ViewHolder(itemView)
    {
        internal var emoji_text_view:EmojiconTextView
        init {
            emoji_text_view = itemView.findViewById(R.id.emoji_text_view) as EmojiconTextView
            itemView.setOnClickListener{
                listener.onEmojiItemSelected(emojiItemList[adapterPosition])
            }
        }
    }


      interface EmojiAdapterListener{
          fun onEmojiItemSelected(emoji:String)
      }


}