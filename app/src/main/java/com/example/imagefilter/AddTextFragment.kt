package com.example.imagefilter

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.imagefilter.Adapter.ColorAdapter
import com.example.imagefilter.Adapter.FontAdapter
import com.example.imagefilter.Interface.AddTextFragmentListener
import com.example.imagefilter.Interface.BrushFragmentListener
import com.example.imagefilter.databinding.FragmentAddTextBinding
import com.example.imagefilter.databinding.FragmentBrushBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.lang.StringBuilder


/**
 * A simple [Fragment] subclass.
 * Use the [AddTextFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddTextFragment : BottomSheetDialogFragment(),ColorAdapter.ColorAdapterClickListener, FontAdapter.FontAdapterClickListener {

    private var _binding: FragmentAddTextBinding? = null
    private val binding get() = _binding!!

    var colorSelected:Int = Color.parseColor("#000000") //Default is black

    internal var listener:AddTextFragmentListener?=null
    fun setListener(listener: AddTextFragmentListener)
    {
        this.listener = listener
    }

    var edit_add_text:EditText?= null
    var btn_done:Button?=null
    var colorAdapter:ColorAdapter?=null
    var fontAdapter:FontAdapter?=null

    var typeFace = Typeface.DEFAULT

    override fun onColorItemSelected(color: Int) {
        colorSelected = color
    }

    override fun onFontSelected(fontName: String) {
        typeFace = Typeface.createFromAsset(requireContext().assets,StringBuilder("fonts/")
                .append(fontName).toString())
    }

    companion object
    {
        internal var instance:AddTextFragment?=null

        fun getInstance():AddTextFragment{
            if (instance == null)
                instance = AddTextFragment()
            return instance!!
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        _binding =  FragmentAddTextBinding.inflate(inflater, container, false)
        val itemView = binding.root
        edit_add_text = itemView.findViewById<EditText>(R.id.edt_add_text);
        btn_done = itemView.findViewById<Button>(R.id.btn_done);

        binding.recyclerColor.setHasFixedSize(true)
        binding.recyclerColor.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL,false)

        binding.recyclerFont.setHasFixedSize(true)
        binding.recyclerFont.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL,false)

        colorAdapter = ColorAdapter(requireContext(),this@AddTextFragment)
        binding.recyclerColor.adapter = colorAdapter

        fontAdapter = FontAdapter(requireContext(),this@AddTextFragment)
        binding.recyclerFont.adapter = fontAdapter

        //Event
        btn_done!!.setOnClickListener{
            listener!!.onAddTextListener(typeFace,edit_add_text!!.text.toString(),colorSelected)
        }


        return itemView
    }



}