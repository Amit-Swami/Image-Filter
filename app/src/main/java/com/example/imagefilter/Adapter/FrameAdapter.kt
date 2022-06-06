package com.example.imagefilter.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.imagefilter.R

class FrameAdapter(internal var context: Context, internal var listener:FrameAdapterClickListener):
        RecyclerView.Adapter<FrameAdapter.FrameViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FrameViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.frame_item,parent,false)
        return FrameViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: FrameViewHolder, position: Int) {
        holder.img_frame.setImageResource(frameList.get(position))

        if (row_selected == position)
            holder.img_check.visibility = View.VISIBLE
        else
            holder.img_check.visibility = View.INVISIBLE
    }

    override fun getItemCount(): Int {
        return frameList.size
    }


    internal var frameList:List<Int>
    internal var row_selected = -1

    init {
        this.frameList = getFrameList()
    }

    private fun getFrameList(): List<Int> {
        val result = ArrayList<Int>()

        result.add(R.drawable.card_1_resize)
        result.add(R.drawable.card_2_resize)
        result.add(R.drawable.card_3_resize)
        result.add(R.drawable.card_4_resize)
        result.add(R.drawable.card_5_resize)
        result.add(R.drawable.card_6_resize)
        result.add(R.drawable.card_7_resize)
        result.add(R.drawable.card_8_resize)
        result.add(R.drawable.card_9_resize)
        result.add(R.drawable.card_10_resize)

        return result
    }

    inner class FrameViewHolder(itemView:View):RecyclerView.ViewHolder(itemView) {
        internal var img_check:ImageView
        internal var img_frame:ImageView

        init {
            img_check = itemView.findViewById(R.id.img_check)
            img_frame = itemView.findViewById(R.id.img_frame)

            itemView.setOnClickListener{
                listener.onFrameItemSelected(frameList.get(adapterPosition))
                row_selected = adapterPosition
                notifyDataSetChanged()
            }
        }
    }

    interface FrameAdapterClickListener {
        fun onFrameItemSelected(frame:Int)

    }


}