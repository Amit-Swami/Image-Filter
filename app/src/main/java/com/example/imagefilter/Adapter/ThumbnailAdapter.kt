package com.example.imagefilter.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.imagefilter.Interface.FilterListFragmentListener
import com.example.imagefilter.R
import com.zomato.photofilters.utils.ThumbnailItem

class ThumbnailAdapter (private val context: Context,
                        private val thumbnailItemList: List<ThumbnailItem>,
                        private val listener:FilterListFragmentListener): RecyclerView.Adapter<ThumbnailAdapter.MyViewHolder>() {

    private var selectedIndex = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.thumbnail_list_item,parent,false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val thumbnailItem = thumbnailItemList[position]
        holder.thumbNail.setImageBitmap(thumbnailItem.image)
        holder.thumbNail.setOnClickListener {
            listener.onFilterSelected(thumbnailItem.filter)
            selectedIndex = position
            notifyDataSetChanged()
        }
        holder.filterName.text = thumbnailItem.filterName

        if (selectedIndex == position)
            holder.filterName.setTextColor(ContextCompat.getColor(context,R.color.filter_label_selected))
        else
            holder.filterName.setTextColor(ContextCompat.getColor(context,R.color.filter_label_noemal))
    }

    override fun getItemCount(): Int {
        return thumbnailItemList.size
    }

    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var thumbNail:ImageView
        var filterName:TextView
        init {
            thumbNail = itemView.findViewById(R.id.thumbnail)
            filterName = itemView.findViewById(R.id.filter_name)
        }

    }


}