package com.example.imagefilter.Adapter

import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.recyclerview.widget.RecyclerView
import com.example.imagefilter.R
import org.w3c.dom.Text
import java.lang.StringBuilder

class FontAdapter (internal var context: Context,
                   internal var listerner: FontAdapter.FontAdapterClickListener):
        RecyclerView.Adapter<FontAdapter.FontViewHolder>(){

    var row_selected = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FontViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.font_item,parent,false)
        return FontViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: FontViewHolder, position: Int) {
        if (row_selected == position)
            holder.img_check.visibility = View.VISIBLE
        else
            holder.img_check.visibility = View.INVISIBLE

        var typeFace = Typeface.createFromAsset(context.assets, StringBuilder("fonts/")
                .append(fontList.get(position)).toString())

        holder.txt_font_name.text = fontList.get(position)
        holder.txt_font_demo.typeface = typeFace
    }

    override fun getItemCount(): Int {
        return fontList.size
    }


    internal var fontList:List<String>

    init {
        this.fontList = loadFontName()!!
    }

    private fun loadFontName(): List<String>? {
        var result = ArrayList<String>()

        result.add("Gist-Light.otf")
        result.add("Sydney-Signature.otf")

        return result
    }

    interface FontAdapterClickListener {
        fun onFontSelected(fontName:String)
    }

    inner class FontViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {

        internal var txt_font_demo:TextView
        internal var txt_font_name:TextView
        internal var img_check:ImageView

        init {
            txt_font_demo = itemView.findViewById(R.id.txt_font_demo) as TextView
            txt_font_name = itemView.findViewById(R.id.txt_font_name) as TextView

            img_check = itemView.findViewById(R.id.img_check) as ImageView

            itemView.setOnClickListener{
                listerner.onFontSelected(fontList[adapterPosition])
                row_selected = adapterPosition
                notifyDataSetChanged()
            }
        }


    }



}