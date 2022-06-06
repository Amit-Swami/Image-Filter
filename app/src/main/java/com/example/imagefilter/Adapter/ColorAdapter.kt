package com.example.imagefilter.Adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.imagefilter.R

class ColorAdapter(internal var context: Context,
                   internal var listerner: ColorAdapterClickListener):
                    RecyclerView.Adapter<ColorAdapter.ColorViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColorViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.color_item, parent, false)
        return ColorViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ColorViewHolder, position: Int) {
        holder.color_section.setCardBackgroundColor(colorList[position])
    }

    override fun getItemCount(): Int {
        return colorList.size
    }

    internal var colorList:List<Int>
    init {
        this.colorList = genColorList()!!
    }
    interface ColorAdapterClickListener {
        fun onColorItemSelected(color: Int)
    }

    private fun genColorList(): List<Int>?
    {

        val colorList = ArrayList<Int>()

        colorList.add(Color.parseColor("#131722"))
        colorList.add(Color.parseColor("#ff545e"))
        colorList.add(Color.parseColor("#57bb82"))
        colorList.add(Color.parseColor("#dbeeff"))
        colorList.add(Color.parseColor("#ba5796"))
        colorList.add(Color.parseColor("#bb349b"))
        colorList.add(Color.parseColor("#6e557c"))
        colorList.add(Color.parseColor("#5e40b2"))

        colorList.add(Color.parseColor("#8051cf"))
        colorList.add(Color.parseColor("#895adc"))
        colorList.add(Color.parseColor("#935da0"))
        colorList.add(Color.parseColor("#7a5e93"))
        colorList.add(Color.parseColor("#6c4475"))
        colorList.add(Color.parseColor("#403890"))
        colorList.add(Color.parseColor("#1b36eb"))
        colorList.add(Color.parseColor("#10d6a2"))

        colorList.add(Color.parseColor("#45b9d3"))
        colorList.add(Color.parseColor("#0c6483"))
        colorList.add(Color.parseColor("#487995"))
        colorList.add(Color.parseColor("#428fb9"))
        colorList.add(Color.parseColor("#a183b3"))
        colorList.add(Color.parseColor("#210333"))
        colorList.add(Color.parseColor("#99ffcc"))
        colorList.add(Color.parseColor("#b2b2b2"))

        colorList.add(Color.parseColor("#c0fff4"))
        colorList.add(Color.parseColor("#c0fff4"))
        colorList.add(Color.parseColor("#97ffff"))
        colorList.add(Color.parseColor("#ff1493"))
        colorList.add(Color.parseColor("#caff70"))
        colorList.add(Color.parseColor("#dab420"))
        colorList.add(Color.parseColor("#aa5511"))
        colorList.add(Color.parseColor("#314159"))

        return colorList

    }

    inner class ColorViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {

        internal  var color_section:CardView
        init {
            color_section = itemView.findViewById(R.id.color_section) as CardView

            itemView.setOnClickListener{
                listerner.onColorItemSelected(colorList[adapterPosition])
            }
        }
    }


}