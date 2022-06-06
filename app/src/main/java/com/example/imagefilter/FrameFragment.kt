package com.example.imagefilter

import android.os.Bundle
import android.util.TypedValue
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.Button
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.imagefilter.Adapter.FrameAdapter
import com.example.imagefilter.Interface.AddFrameFragmentListener
import com.example.imagefilter.Interface.FilterListFragmentListener
import com.example.imagefilter.Utils.SpaceItemDecoration
import com.example.imagefilter.databinding.FragmentBrushBinding
import com.example.imagefilter.databinding.FragmentFrameBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

/**
 * A simple [Fragment] subclass.
 * Use the [FrameFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FrameFragment : BottomSheetDialogFragment(), FrameAdapter.FrameAdapterClickListener {

    private var _binding: FragmentFrameBinding? = null
    private val binding get() = _binding!!

    internal  lateinit var recycler_frame:RecyclerView
    internal  lateinit var btn_add_frame:Button
    internal   var listener:AddFrameFragmentListener?=null
    internal  lateinit var adapter: FrameAdapter

    internal var frame_selected = -1

    companion object
    {
        internal var instance:FrameFragment?=null

        fun getInstance():FrameFragment{
            if (instance == null)
                instance = FrameFragment()
            return instance!!
        }
    }

    fun setListener(fragmentListener: AddFrameFragmentListener)
    {
        this.listener = fragmentListener
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        _binding =  FragmentFrameBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.recyclerFrames.layoutManager = LinearLayoutManager(activity,
            LinearLayoutManager.HORIZONTAL,false)
        binding.recyclerFrames.setHasFixedSize(true)
        binding.recyclerFrames.adapter = (FrameAdapter(requireContext(),this))

        binding.btnAddFrame.setOnClickListener{
            listener!!.onFrameSelected(frame_selected)
        }
        return view
    }

    override fun onFrameItemSelected(frame: Int) {
        frame_selected = frame
    }

}