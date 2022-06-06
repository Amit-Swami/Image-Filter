package com.example.imagefilter

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import com.example.imagefilter.Interface.EditImageFragmentListener
import com.example.imagefilter.databinding.FragmentEditImageBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

/**
 * A simple [Fragment] subclass.
 * Use the [EditImageFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EditImageFragment : BottomSheetDialogFragment(), SeekBar.OnSeekBarChangeListener {

    private var _binding: FragmentEditImageBinding? = null
    private val binding get() = _binding!!

    private var listener:EditImageFragmentListener?=null

    companion object{
        internal var instance:EditImageFragment?=null

        fun getInstance():EditImageFragment{
            if (instance == null)
                instance = EditImageFragment()
            return instance!!
        }

    }

    fun setListener(listener: EditImageFragmentListener)
    {
        this.listener = listener
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        _binding = FragmentEditImageBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.seekbarBrightness.max = 200
        binding.seekbarBrightness.progress = 100

        binding.seekbarContrast.max = 20
        binding.seekbarContrast.progress = 0

        binding.seekbarSaturation.max = 30
        binding.seekbarSaturation.progress = 10

        binding.seekbarSaturation.setOnSeekBarChangeListener(this)
        binding.seekbarContrast.setOnSeekBarChangeListener(this)
        binding.seekbarBrightness.setOnSeekBarChangeListener(this)

        return view
    }

    fun resetControls()
    {
        binding.seekbarBrightness.progress = 100
        binding.seekbarContrast.progress = 0
        binding.seekbarSaturation.progress = 10
    }

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        var progress = progress
        if (listener != null)
        {
            if (seekBar!!.id == R.id.seekbar_brightness)
            {
                listener!!.onBrightnessChanged(progress-100)
            }
            else if (seekBar!!.id == R.id.seekbar_contrast)
            {
                progress += 10
                val floatVal = .10f*progress
                listener!!.onConstrantChanged(floatVal)
            }
            else if (seekBar!!.id == R.id.seekbar_saturation)
            {
                val floatVal = .10f*progress
                listener!!.onSaturationChanged(floatVal)
            }

        }
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {
        if (listener != null)
            listener!!.onEditStarted()
    }

    override fun onStopTrackingTouch(seekBar: SeekBar?) {
        if (listener !=null)
            listener!!.onEditCompleted()
    }

}