package com.example.imagefilter

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.RadioGroup
import android.widget.SeekBar
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.imagefilter.Adapter.ColorAdapter
import com.example.imagefilter.Interface.BrushFragmentListener
import com.example.imagefilter.databinding.FragmentBrushBinding
import com.example.imagefilter.databinding.FragmentEditImageBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


/**
 * A simple [Fragment] subclass.
 * Use the [BrushFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class BrushFragment : BottomSheetDialogFragment(), ColorAdapter.ColorAdapterClickListener {

    private var _binding: FragmentBrushBinding? = null
    private val binding get() = _binding!!
    var colorAdapter: ColorAdapter? = null

    companion object
    {
        internal var instance:BrushFragment?=null

        fun getInstance():BrushFragment{
            if (instance == null)
                instance = BrushFragment()
            return instance!!
        }
    }

    internal var listener:BrushFragmentListener?=null

    fun setListener(listener:BrushFragmentListener)
    {
        this.listener = listener
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        _binding =  FragmentBrushBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.recyclerColor.setHasFixedSize(true)
        binding.recyclerColor.layoutManager = LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL,false)

        colorAdapter = ColorAdapter(requireContext(),this@BrushFragment)
        binding.recyclerColor.adapter = colorAdapter

        //Event
        binding.seekbarBrushSize.setOnSeekBarChangeListener(object:SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                listener!!.onBrushSizeChangedListener(progress.toFloat())
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                TODO("Not yet implemented")
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                TODO("Not yet implemented")
            }

        })
        binding.seekbarBrushOpacity.setOnSeekBarChangeListener(object:SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                listener!!.onBrushOpacityChangedListener(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                TODO("Not yet implemented")
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                TODO("Not yet implemented")
            }

        })
        binding.btnBrushState.setOnCheckedChangeListener(object :CompoundButton.OnCheckedChangeListener{
            override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
                listener!!.onBrushStateChangedListener(isChecked)
            }


        })


        return view
    }

    override fun onColorItemSelected(color: Int) {
        listener!!.onBrushColorChangedListener(color)
    }


}