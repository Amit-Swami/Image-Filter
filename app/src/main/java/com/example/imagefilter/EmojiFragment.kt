package com.example.imagefilter

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.imagefilter.Adapter.EmojiAdapter
import com.example.imagefilter.Interface.EmojiFragmentListener
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import ja.burhanrashid52.photoeditor.PhotoEditor


/**
 * A simple [Fragment] subclass.
 * Use the [EmojiFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EmojiFragment : BottomSheetDialogFragment(),EmojiAdapter.EmojiAdapterListener {

    internal var emojiRecycler:RecyclerView?=null
    internal var listener:EmojiFragmentListener?=null

    fun setListener(listener: EmojiFragmentListener)
    {
        this.listener=listener
    }

    override fun onEmojiItemSelected(emoji: String) {
        listener!!.onEmojiItemSelected(emoji)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val itemView =  inflater.inflate(R.layout.fragment_emoji, container, false)

        emojiRecycler = itemView.findViewById(R.id.recycler_emoji) as RecyclerView
        emojiRecycler!!.setHasFixedSize(true)
        emojiRecycler!!.layoutManager = GridLayoutManager(activity,5)

        val adapter = EmojiAdapter(requireContext(),PhotoEditor.getEmojis(context),this)
        emojiRecycler!!.adapter = adapter
        return itemView
    }


    companion object
    {
        internal var instance:EmojiFragment?=null

        fun getInstance():EmojiFragment{
            if (instance == null)
                instance = EmojiFragment()
            return instance!!
        }
    }


}